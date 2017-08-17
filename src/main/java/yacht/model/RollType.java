package yacht.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RollType {
    ACES("Aces", 1),
    TWOS("Twos", 2),
    THREES("Threes", 3),
    FOURS("Fours", 4),
    FIVES("Fives", 5),
    SIXES("Sixes", 6),

    THREE_OF_A_KIND("Three of a Kind", null),
    FOUR_OF_A_KIND("Four of a Kind", null),
    FULL_HOUSE("Full House", null),
    SMALL_STRAIGHT("Small Straight", null),
    LARGE_STRAIGHT("Large Straight", null),
    YAHTZEE("Yahtzee", null),
    CHANCE("Chance", null);

    private String name;
    private Integer dieNumber;

    public Integer getDieNumber() {
        return dieNumber;
    }

    public String getName() {
        return name;
    }

    public static final RollType[] UPPER_SECTION = {ACES, TWOS, THREES, FOURS, FIVES, SIXES};
    public static final RollType[] LOWER_SECTION = {THREE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, SMALL_STRAIGHT, LARGE_STRAIGHT, YAHTZEE, CHANCE};

    RollType(String name, Integer dieNumber) {
        this.name = name;
        this.dieNumber = dieNumber;
    }

}
