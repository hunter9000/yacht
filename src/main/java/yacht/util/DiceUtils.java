package yacht.util;

import java.util.Random;

public class DiceUtils {

    static Random random = new Random();

    public static Integer rollD6() {
        return random.nextInt(6) + 1;
    }
}
