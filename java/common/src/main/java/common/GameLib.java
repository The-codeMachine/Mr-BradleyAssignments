package common;

import java.util.Random;

public class GameLib {

    // Random helper functions

    /**
     * 
     * Generates a random number between 0, and 1. With double precision.
     * 
     * @return a random number between 0 and 1
     */
    public static double random() {
        return Math.random();
    }

    /**
     * 
     * Generates a random int between min, and max (inclusive)
     * 
     * @apiNote max must be greater than min (checks via assert)
     * 
     * @param min
     * @param max
     * @return a number between min, and max (inclusive)
     */
    public static int randomInt(int min, int max) {
        assert min < max : "Max must be greater than min";

        final Random RAND = new Random();

        // min is inclusive, but max is exclusive, so +1 makes it inclusive as well
        return RAND.nextInt(min, max + 1);
    }

    /**
     * 
     * Makes a weighted choice outputting the array's index for that chance
     * 
     * @apiNote throws an IllegalArgumentException if there are any errors
     * 
     * @param weights
     * @return an int representing the array's index for that chance
     */
    public static int weightedChoice(double[] weights) {
        if (weights == null || weights.length == 0)
            throw new IllegalArgumentException("Weights array is empty");

        double total = 0.0;
        for (double w : weights) {
            if (w < 0)
                throw new IllegalArgumentException("Weights must be non-negative");
            total += w;
        }

        if (total <= 0)
            throw new IllegalArgumentException("Total weight must be > 0");

        double r = random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < weights.length; ++i) {
            cumulative += weights[i];
            if (r < cumulative)
                return i;
        }

        // Due to floating point precision, fallback to last index
        return weights.length - 1;
    }

    // Quadrant generation functions

    /**
     * Generates the number of klingons in a quadrant using the following rules:
     * - 20% for 1 klingon to generate
     * - 5% for 2 klingons to generate
     * - 2% for 3 klingons to generate
     * 
     * @return the number of klingons for 1 quadrant
     */
    public static int genKlingons() {
        // klingons: 0 1 2 3
        // 73% 20% 5% 2%
        int r = weightedChoice(new double[] { 0.73, 0.2, 0.05, 0.02 });

        return r;
    }

    /**
     * Generates the number of bases in a quadrant using the following rules:
     * - 4% chance for one base inside the quadrant
     * - No more than 2 per galaxy
     * 
     * @return the number of bases for 1 quadrant
     */
    public static int genBases() {
        if (totalBases < 2) {
            // 4% chance of a quadrant having a base
            if (random() <= 0.04) {
                totalBases++;
                return 1;
            }
        }

        return 0;
    }

    /**
     * Randomly generates a random number of stars between 1-8
     * 
     * @return number of stars for 1 quadrant
     */
    public static int genStars() {
        // star min, star max
        return randomInt(1, 8);
    }

    // tests the random number generators functions work
    private static void randomTestDriver() {
        System.out.println("Random test");

        double r = random();
        assert r >= 0 && r <= 1 : "New random number is not between 1, and 0";
        System.out.printf("New random number: %.2f\n", r);

        double ra = randomInt(1, 100);
        assert ra >= 1 && ra <= 100 : "New random int is not generated within parameters";
        System.out.printf("New random number (between 1, and 100): %.2f\n", ra);

        double rb = randomInt(1, 100);
        assert rb >= 1 && rb <= 100 : "Second new random int is not generated within parameters";
        System.out.printf("Second new random number (between 1, and 100): %.2f\n", rb);

        int i = weightedChoice(new double[] { 0.73, 0.2, 0.05, 0.02 });
        assert i >= 0 && i <= 3 : "Weighted choice did not generate within parameters";
        System.out.printf("Weighted choice output: %d\n", i);

        int ii = weightedChoice(new double[] { 0.43, 0.4, 0.17 });
        assert ii >= 0 && ii <= 2 : "Weighted choice did not generate within parameters";
        System.out.printf("Weighted choice output: %d\n", ii);

        System.out.println("Random test success");
    }

    // tests that the generation functions work properly
    private static void genTestDriver() {
        System.out.println("Generation test");

        long start = System.nanoTime();

        int numOf1Klingons = 0;
        int numOf2Klingons = 0;
        int numOf3Klingons = 0;
        for (long i = 0; i < 1000000; ++i) {
            int r = genKlingons();

            if (r == 1)
                numOf1Klingons++;
            else if (r == 2)
                numOf2Klingons++;
            else if (r == 3)
                numOf3Klingons++;

            genBases();
        }

        long end = System.nanoTime();
        long duration = (end - start) / 1000000;

        assert totalBases >= 1 && totalBases <= 2 : "Bases did not generate with the correct range";

        float percent1 = numOf1Klingons * 100 / 1000000;
        float percent2 = numOf2Klingons * 100 / 1000000;
        float percent3 = numOf3Klingons * 100 / 1000000;
        float percent4 = totalBases * 100 / 1000000;

        System.out.printf("Number of quadrants with 1 klingon: %.2f%% \n", percent1);
        System.out.printf("Number of quadrants with 2 klingon: %.2f%% \n", percent2);
        System.out.printf("Number of quadrants with 3 klingon: %.2f%% \n", percent3);
        System.out.printf("Number of quadrants with 1 base: %.2f%% \n", percent4);

        System.out.printf("Time taken: %d ms\n", duration);
        totalBases = 0;

        System.out.println("Generation test success");
    }

    public static void testDriver() {
        System.out.println("GameLib test driver run");

        randomTestDriver();
        genTestDriver();

        System.out.println("GameLib test driver run success");
    }

    // Will be made part of galaxy once galaxy is made
    public static int totalBases = 0;
}

/*
 * Sample Output
 * 
 * GameLib test driver run
 * Random test
 * New random number: 0.74 <- these may change due to randomness
 * New random number (between 1, and 100): 93.00
 * Second new random number (between 1, and 100): 31.00
 * Weighted choice output: 0 
 * Weighted choice output: 1
 * Random test success
 * Generation test
 * Number of quadrants with 1 klingon: 19.00% <- this one may change by 1% due to noise
 * Number of quadrants with 2 klingon: 4.00% <- this one may change by 1% due to noise
 * Number of quadrants with 3 klingon: 2.00% <- this one may change by 1% due to noise
 * Number of quadrants with 1 base: 0.00% <- this one may change by 1% due to noise
 * Time taken: 14 ms <- this one may change due to system hardware
 * Generation test success
 * GameLib test driver run success
 */