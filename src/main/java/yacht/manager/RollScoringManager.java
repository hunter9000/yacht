package yacht.manager;

import yacht.model.DieRoll;
import yacht.model.RollType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RollScoringManager {

    private static final Integer FULL_HOUSE_SCORE = 25;
    private static final Integer SMALL_STRAIGHT_SCORE = 30;
    private static final Integer LARGE_STRAIGHT_SCORE = 40;
    private static final Integer YAHTZEE_SCORE = 50;

    private List<DieRoll> dieRolls;

    // the total of all dice faces in the list
    private Integer grandTotal = 0;

    // Map of face values to count of that value's occurrence
    private Map<Integer, Integer> valueCountMap;

    public RollScoringManager(List<DieRoll> dieRolls) {
        this.dieRolls = dieRolls;
        this.valueCountMap = new HashMap<>();

        // populate map with zeroes
        for (int i=1; i<=6; i++) {
            this.valueCountMap.put(i, 0);
        }

        // sort the die rolls by face value
        dieRolls.sort(Comparator.comparingInt(DieRoll::getValue));

        // sum total, and get counts
        for (DieRoll dieRoll : dieRolls) {
            grandTotal += dieRoll.getValue();
            // map the values by their count
            Integer val = this.valueCountMap.get(dieRoll.getValue());
            this.valueCountMap.put(dieRoll.getValue(), val+1);
        }

    }

    public Integer calculateScore(RollType rollType) {
        switch (rollType) {
            case ACES:
            case TWOS:
            case THREES:
            case FOURS:
            case FIVES:
            case SIXES:
                return countDieFaces(rollType);
            case THREE_OF_A_KIND:
                return hasXOfAKind(3) ? grandTotal : 0;
            case FOUR_OF_A_KIND:
                return hasXOfAKind(4) ? grandTotal : 0;
            case FULL_HOUSE:
                return hasFullHouse() ? FULL_HOUSE_SCORE : 0;
            case SMALL_STRAIGHT:
                return hasStraight(4) ? SMALL_STRAIGHT_SCORE : 0;
            case LARGE_STRAIGHT:
                return hasStraight(5) ? LARGE_STRAIGHT_SCORE : 0;
            case YAHTZEE:
                return hasXOfAKind(5) ? YAHTZEE_SCORE : 0;
            case CHANCE:
                return grandTotal;
            default:
                return null;
        }
    }

    /** Returns sum of the die faces that match the roll type's number */
    private Integer countDieFaces(RollType rollType) {
        if (rollType.getDieNumber() == null) {
            throw new RuntimeException();
        }
        Integer face = rollType.getDieNumber();
        Integer total = 0;
        for (DieRoll dieRoll : dieRolls) {
            if (dieRoll.getValue().equals(face)) {
                total += face;
            }
        }
        return total;
    }

    /** Checks if "X of a kind" is met, and returns the grand total if so. */
    private boolean hasXOfAKind(Integer minRunLength) {
        Integer currValue = dieRolls.get(0).getValue();
        int run = 0;        // track length of the run of same values
        for (DieRoll dieRoll : dieRolls) {
            // run continues
            if (currValue.equals(dieRoll.getValue())) {
                run++;
                if (run >= minRunLength) {
                    return true;
                }
            }
            // run ended
            else {
                currValue = dieRoll.getValue();
                run = 1;
            }

        }
        // no run of sufficient length, 0 points
        return false;
    }

    private boolean hasFullHouse() {
        // for each face value, check to see if one has count == 3, and another has count == 2
        boolean hasThree = false;
        boolean hasTwo = false;
        for (int i=1; i<=6; i++) {
            if (this.valueCountMap.get(i).equals(3)) {
                hasThree = true;
            }
            if (this.valueCountMap.get(i).equals(2)) {
                hasTwo = true;
            }
        }
        return hasThree && hasTwo;
    }

    private boolean hasStraight(Integer straightLength) {
        // loop through each die face, seeing if the count is >= 1
        int runLength = 0;
        for (int i=1; i<=6; i++) {
            if (this.valueCountMap.get(i) >= 1) {
                runLength++;
            }
            else {
                if (runLength >= straightLength) {
                    return true;
                }
                runLength = 0;
            }
        }
        return runLength >= straightLength;
    }

}
