package common;

import java.util.Random;
import java.util.Arrays;

public class GameLib {

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
        assert min > max : "Max must be greater than min";

        final Random RAND = new Random();
        
        // min is inclusive, but max is exclusive, so +1 makes it inclusive as well 
        return RAND.nextInt(min, max + 1);
    }

    /**
     * 
     * Makes a weighted choice outputting the array's index for that chance
     * 
     * @apiNote returns 0 for an error
     * 
     * @param weights
     * @return an int representing the array's index for that chance
     */
    public static int weightedChoice(double[] weights) {
        double r = random();

        for (int i = 0; i < weights.length; ++i) {
            if (r < weights[i])
                return i;
        }

        return 0; 
    }




    /**
     * Generates the number of klingons in a quadrant using the following rules:
     * - 20% for 1 klingon to generate 
     * - 5% for 2 klingons to generate
     * - 2% for 3 klingons to generate
     * 
     * @return the number of klingons for 1 quadrant
     */
    public static int genKlingons() {
        //                                    0     1      2    3
        //                                   73%   20%    5%    2%
        int r = weightedChoice(new double[] {0.73, 0.2, 0.05, 0.02});

        return r;
    }

    /**
     * Generates the number of bases in a quadrant using the following rules:
     * - 4% chance for one base inside the quadrant
     *  - No more than 2 per galaxy
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

        // checks if there has not been any bases generated yet, 
        // if not then add one to the last quadrant
        if (totalQuadrants == 64 && totalBases == 0) {
            totalBases++;
            return 1;
        }

        return 0;
    }

    /**
     * Randomly generates a random number of stars between 1-8
     *  
     * @return number of stars for 1 quadrant
     */
    public static int genStars() {
        //              star min, star max
        return randomInt(1, 8);
    }

    private static int totalBases = 0;
    private static int totalQuadrants = 0;
}
