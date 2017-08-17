package yacht.manager;

import yacht.model.*;
import yacht.model.user.User;
import yacht.security.BadRequestException;
import yacht.util.DiceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class YahtzeeGameManager {

    public static YahtzeeGame createNewGame(User user) {
        YahtzeeGame yahtzeeGame = new YahtzeeGame();
        yahtzeeGame.setUser(user);

        GameSheet gameSheet = new GameSheet();
        gameSheet.setYahtzeeGame(yahtzeeGame);
        gameSheet.setRollScores(getNewRollScores(gameSheet));
        yahtzeeGame.setGameSheet(gameSheet);

        yahtzeeGame.setDieRolls(getNewDiceRolls(yahtzeeGame));

        return yahtzeeGame;
    }

    /** Returns set of new die rolls, setting them to belong to the given game. */
    public static Set<DieRoll> getNewDiceRolls(YahtzeeGame yahtzeeGame) {
        // create die rolls
        Set<DieRoll> dieRolls = new HashSet<>();
        for (int i=0; i<5; i++) {
            DieRoll dieRoll = new DieRoll();
            dieRoll.setYahtzeeGame(yahtzeeGame);
            dieRoll.setMarked(true);
            dieRoll.setOrder(i);
            dieRoll.setValue(DiceUtils.rollD6());
            dieRolls.add(dieRoll);
        }
        return dieRolls;
    }

    /** Returns Map of new roll scores, setting them to the given sheet. */
    static Map<RollType, RollScore> getNewRollScores(GameSheet gameSheet) {
        Map<RollType, RollScore> map = new HashMap<>();
        for (RollType rollType : RollType.values()) {
            RollScore rollScore = new RollScore();
            rollScore.setGameSheet(gameSheet);
            rollScore.setRollType(rollType);
            map.put(rollType, rollScore);
        }
        return map;
    }

    public static void reroll(YahtzeeGame yahtzeeGame) {
        for (DieRoll dieRoll : yahtzeeGame.getDieRolls()) {
            if (!dieRoll.getMarked()) {
                dieRoll.setValue(DiceUtils.rollD6());
            }
        }
    }

    public static void updateMarked(YahtzeeGame yahtzeeGame, List<DieRoll> requestDieRolls) {
        Map<Long, DieRoll> dieRollMap = yahtzeeGame.getDieRolls().stream().collect(Collectors.toMap(DieRoll::getId, dieRoll -> dieRoll));
        Map<Long, DieRoll> requestDieRollMap = requestDieRolls.stream().collect(Collectors.toMap(DieRoll::getId, dieRoll -> dieRoll));

        // update each
        requestDieRollMap.forEach((id, v) -> {
            DieRoll match = dieRollMap.get(id);
            if (match == null) {
                throw new BadRequestException();
            }
            match.setMarked(v.getMarked());
        });
    }

    /** Figure out the what each score box would get from teh current roll, and put in the score's potential score field */
    public static void calculatePotentialScores(YahtzeeGame yahtzeeGame) {
        List<DieRoll> dieRolls = new ArrayList<>(yahtzeeGame.getDieRolls());
        RollScoringManager scoringManager = new RollScoringManager(dieRolls);

        for (Map.Entry<RollType, RollScore> entry : yahtzeeGame.getGameSheet().getRollScores().entrySet()) {
            if (!entry.getValue().getChosen()) {
                entry.getValue().setPotentialScore(scoringManager.calculateScore(entry.getKey()));
            }
        }
    }


}
