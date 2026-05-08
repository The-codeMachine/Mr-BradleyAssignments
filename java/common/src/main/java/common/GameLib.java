package common;

/**
 * 
 * GameLib holds a bunch of random number generation functions, 
 * as well as a few test drivers, and verification tests. 
 * 
 * Operations include:
 *  - Generate a random number between 0, and 1
 *  - Generate a random int between min, and max
 *  - Generate a random number between min, and max
 *  - Make a weighted choice 
 *  - Check if a number is between two
 * 
 */

public class GameLib {

    // Random helper functions

    /**
     * 
     * Generates a random number between 0, and 1. With double precision.
     * Checks if the random number is <= percent
     * 
     * @return if the random number between 0 and 1 is <= percent
     */
    public static boolean chanceOf(double percent) {
        double r = Math.random();
        
        return r <= percent;
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

        return (int)(min + (max - min + 1) * Math.random());
    }

    /**
     * 
     * Generates a random double between min, and max
     * 
     * @apiNote max must be greater than min (checks via assert)
     * 
     * @param min (inclusive)
     * @param max (inclusive)
     * @return a random double between min, and max
     */
    public static double randomInRange(double min, double max) {
        assert min < max : "Max must be greater than min";

        return min + (max - min) * Math.random();
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

        double r = chanceOf() * total;
        double cumulative = 0.0;

        for (int i = 0; i < weights.length; ++i) {
            cumulative += weights[i];
            if (r < cumulative)
                return i;
        }

        // Due to floating point precision, fallback to last index
        return weights.length - 1;
    }

    private static double _rnd;
    /**
     * This function is the MS BASIC function of the same name
     * Specifically, if RND is called with a non-zero value a new random
     * value will be generated and stored in _rnd static variable (retaining its value)
     * across calls. If RND is called with a zero value, the last random value
     * generated is returned.
     *  
     * NB: support for setting the seed ( < 0 parameter value) has been omitted.
     */
    private static double RND(int n) {
        if(n != 0) 
            _rnd = Math.random();  // generates a new rand
        
        return _rnd;                        // otherwise returns last
    }

    private static double RND() { 
        return RND(1); 
    }

    /**
     * 
     * Determines if a value is between low, and high
     * 
     * @param value
     * @param low (inclusive)
     * @param high (inclusive)
     * @return determines if a value is between low, and high
     */
    private static boolean isBetween(double value, double low, double high) {
        return low <= value && value <= high;
    }

    // tests the random number generators functions work
    private static void randomTestDriver() {
        System.out.println("Random test");

        double r = chanceOf();
        assert r >= 0 && r <= 1 : "New random number is not between 1, and 0";
        System.out.printf("New random number: %.2f\n", r);

        double ra = randomInt(1, 100);
        assert ra >= 1 && ra <= 100 : "New random int is not generated within parameters";
        System.out.printf("New random number (between 1, and 100): %.2f\n", ra);

        double rb = randomInt(1, 100);
        assert rb >= 1 && rb <= 100 : "Second new random int is not generated within parameters";
        System.out.printf("Second new random number (between 1, and 100): %.2f\n", rb);

        double rc = randomInRange(1, 100);
        assert rc >= 1 && rc <= 100 : "Third new random double is not generated within parameters";
        System.out.printf("Third new random number (between 1, and 100): %.2f\n", rc);

        int i = weightedChoice(new double[] { 0.73, 0.2, 0.05, 0.02 });
        assert i >= 0 && i <= 3 : "Weighted choice did not generate within parameters";
        System.out.printf("Weighted choice output: %d\n", i);

        int ii = weightedChoice(new double[] { 0.43, 0.4, 0.17 });
        assert ii >= 0 && ii <= 2 : "Weighted choice did not generate within parameters";
        System.out.printf("Weighted choice output: %d\n", ii);

        System.out.println("Random test success");
    }

    // tests that the isBetween function works correctly
    private static void isBetweenTest() {
        System.out.println("Is between test");

        assert isBetween(4, 1, 10) : "Is between has false positive";

        assert !isBetween(-4, 1, 10) : "Is between did not detect an invalid value";

        assert !isBetween(14, 1, 10) : "Is between did not detect an invalid value";

        System.out.println("Is between test success");
    }

    public static void testDriver() {
        System.out.println("GameLib test driver run");

        randomTestDriver();
        isBetweenTest();

        System.out.println("GameLib test driver run success");
    }
}

/*
 * Sample Output
 * 
 * Random test
 * New random number: 0.43 <- random numbers, so they may change
 * New random number (between 1, and 100): 25.00
 * Second new random number (between 1, and 100): 43.00
 * Second new random number (between 1, and 100): 63.84
 * Weighted choice output: 0
 * Weighted choice output: 2
 * Random test success
 * Is between test
 * Is between test success
 * GameLib test driver run success
 */
