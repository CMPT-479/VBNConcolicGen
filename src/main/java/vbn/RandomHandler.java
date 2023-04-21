package vbn;

import java.util.Random;

public class RandomHandler {
    static Random random = new Random(System.currentTimeMillis());

    public static int getRandomNumber() {
        return random.nextInt();
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static double getRandomReal() {
        return random.nextDouble();
    }
}
