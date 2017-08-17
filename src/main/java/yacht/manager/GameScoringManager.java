package yacht.manager;


import org.apache.commons.collections4.IterableUtils;
import yacht.model.GameSheet;
import yacht.model.RollScore;
import yacht.model.RollType;

public class GameScoringManager {

    private static final int UPPER_SECTION_BONUS_THRESHOLD = 63;
    private static final int UPPER_SECTION_BONUS = 35;

    private GameSheet gameSheet;

    public GameScoringManager(GameSheet gameSheet) {
        this.gameSheet = gameSheet;
    }

    public boolean isGameCompleted() {
        return IterableUtils.matchesAll(gameSheet.getRollScores().values(), (RollScore rollScore) -> rollScore.getChosen() );
    }

    /** Sets the score values for the sheet. */
    public void recalculateGameSheetScore() {
        // Upper section
        int upperSubtotal = 0;
        boolean hasUpperSectionBonus = false;
        int upperTotal = 0;
        for (RollType rollType : RollType.UPPER_SECTION) {
            RollScore score = gameSheet.getRollScores().get(rollType);
            if (score.getChosen()) {
                upperSubtotal += score.getScore();
            }
        }
        hasUpperSectionBonus = upperSubtotal >= UPPER_SECTION_BONUS_THRESHOLD;
        upperTotal = (hasUpperSectionBonus) ? UPPER_SECTION_BONUS + upperSubtotal : upperSubtotal;

        this.gameSheet.setHasTopSectionBonus(hasUpperSectionBonus);
        this.gameSheet.setTopSectionSubtotal(upperSubtotal);
        this.gameSheet.setTopSectionScore(upperTotal);

        // Lower section
        int lowerTotal = 0;
        for (RollType rollType : RollType.LOWER_SECTION) {
            RollScore score = gameSheet.getRollScores().get(rollType);
            if (score.getChosen()) {
                lowerTotal += score.getScore();
            }
        }
        // TODO yahtzee bonuses
        this.gameSheet.setBottomSectionScore(lowerTotal);
        this.gameSheet.setTotalScore(upperTotal + lowerTotal);
    }
}
