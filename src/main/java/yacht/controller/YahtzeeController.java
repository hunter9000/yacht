package yacht.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yacht.interceptor.YahtzeeGameOwnerRequired;
import yacht.manager.GameScoringManager;
import yacht.manager.RollScoringManager;
import yacht.manager.YahtzeeGameManager;
import yacht.model.DieRoll;
import yacht.model.RollScore;
import yacht.model.YahtzeeGame;
import yacht.model.user.User;
import yacht.repository.YahtzeeGameRepository;
import yacht.request.PlaceScoreRequest;
import yacht.security.BadRequestException;
import yacht.util.AuthUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class YahtzeeController {

    @Autowired
    private YahtzeeGameRepository yahtzeeGameRepository;

    @Autowired
    private HttpServletRequest request;

    /** Get all games for the user */
    @RequestMapping(value="/api/game/", method=RequestMethod.GET)
    public List<YahtzeeGame> getAllGamesForUser() {
        long userId = AuthUtils.getUserId(request);
        return yahtzeeGameRepository.findByUserId(userId);
    }

    /** Get the speficied game, if it belongs to the logged in user. */
    @RequestMapping(value = "/api/game/{gameId}/", method = RequestMethod.GET)
    @YahtzeeGameOwnerRequired
    public YahtzeeGame getYahtzeeGame() {
        YahtzeeGame yahtzeeGame = AuthUtils.getGame(request);

        YahtzeeGameManager.calculatePotentialScores(yahtzeeGame);

        return yahtzeeGame;
    }

    /** Create new game for user. Returns the id of the newly created game */
    @RequestMapping(value = "/api/game/", method = RequestMethod.POST)
    public Long createGame() {
        User user = AuthUtils.getLoggedInUser(request);

        YahtzeeGame yahtzeeGame = YahtzeeGameManager.createNewGame(user);

        yahtzeeGameRepository.save(yahtzeeGame);
        return yahtzeeGame.getId();
    }

    @RequestMapping(value = "/api/game/{gameId}/", method = RequestMethod.DELETE)
    @YahtzeeGameOwnerRequired
    public void deleteGame() {
        YahtzeeGame yahtzeeGame = AuthUtils.getGame(request);

        yahtzeeGameRepository.delete(yahtzeeGame);
    }

    /** reroll with the unselected die */
    @RequestMapping(value = "/api/game/{gameId}/rolls/", method = RequestMethod.PUT)
    @YahtzeeGameOwnerRequired
    public void rerollDie(@RequestBody List<DieRoll> requestDieRolls) {
        YahtzeeGame yahtzeeGame = AuthUtils.getGame(request);

        // if third roll, error
        if (yahtzeeGame.getNumRolls() >= 3) {
            throw new BadRequestException();
        }

        // update marked flag for each die roll
        YahtzeeGameManager.updateMarked(yahtzeeGame, requestDieRolls);

        // reroll each unmarked die
        YahtzeeGameManager.reroll(yahtzeeGame);

        yahtzeeGame.setNumRolls(yahtzeeGame.getNumRolls()+1);

        yahtzeeGameRepository.save(yahtzeeGame);
    }

    /** put the current roll in the given score box */
    @RequestMapping(value = "/api/game/{gameId}/score/", method = RequestMethod.PUT)
    @YahtzeeGameOwnerRequired
    public void placeScore(@RequestBody PlaceScoreRequest placeScoreRequest) {
        YahtzeeGame yahtzeeGame = AuthUtils.getGame(request);

        if (placeScoreRequest.rollType == null) {
            throw new BadRequestException();
        }

        RollScore rollScore = yahtzeeGame.getGameSheet().getRollScores().get(placeScoreRequest.rollType);
        if (rollScore.getChosen()) {
            throw new BadRequestException();
        }

        RollScoringManager scoringManager = new RollScoringManager(new ArrayList<>(yahtzeeGame.getDieRolls()));
        Integer score = scoringManager.calculateScore(rollScore.getRollType());

        rollScore.setChosen(true);
        rollScore.setScore(score);

        // if game is completed
        GameScoringManager gameScoringManager = new GameScoringManager(yahtzeeGame.getGameSheet());

        // rescore the gamesheet
        gameScoringManager.recalculateGameSheetScore();

        if (gameScoringManager.isGameCompleted()) {
            yahtzeeGame.setCompleted(true);
        }
        // game continues, roll new dice
        else {
            yahtzeeGame.setNumRolls(1);
            // make new rolls
            yahtzeeGame.setDieRolls(YahtzeeGameManager.getNewDiceRolls(yahtzeeGame));
        }

        yahtzeeGameRepository.save(yahtzeeGame);
    }

}
