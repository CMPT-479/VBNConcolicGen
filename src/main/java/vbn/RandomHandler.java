package vbn;

import java.util.Random;

public class RandomHandler {
    static long seed;
    static Random random;

    public RandomHandler(long seed) {
        this.seed = seed;
        this.random = new Random(this.seed);
    }

    public int getRandomNumber() {
        return random.nextInt();
    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public double getRandomReal() {
        return random.nextDouble();
    }
}
