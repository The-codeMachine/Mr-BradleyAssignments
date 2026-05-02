package quadrant;

import common.GameLib;

public class Main {

    // ensures all the getters and working correctly
    private static void testQuadrantGetters() {
        System.out.println("Testing Quadrant getters");

        Quadrant q = new Quadrant(3, 1, 8);

        assert (q.klingons() == 3 && q.bases() == 1 && q.stars() == 8);
        System.out.printf("Got %d klingons, expected 3\n", q.klingons());
        System.out.printf("Got %d bases, expected 1\n", q.bases());
        System.out.printf("Got %d stars, expected 8\n", q.stars());

        System.out.println("Quadrant getters success");
    }

    // generate a quadrant with all the constructors, and ensures they work
    private static void testQuadrantConstructors() {
        System.out.println("Testing Quadrant constructors");

        Quadrant q = new Quadrant();

        System.out.printf("Got %d klingons, expected between 0-3\n", q.klingons());

        System.out.printf("Got %d bases, expected between 0-1\n", q.bases());

        System.out.printf("Got %d stars, expected between 1-8\n", q.stars());

        Quadrant qw = new Quadrant(3, 1, 2);

        assert (qw.klingons() == 3 && qw.bases() == 1 && qw.stars() == 2);
        System.out.printf(
                "Got Quadrant: Klingons(%d), Bases(%d), Stars(%d), expected Quadrant: Klingons(3), Bases(1), Stars(2)\n",
                qw.klingons(), qw.bases(), qw.stars());

        System.out.println("Quadrant constructor success");
    }

    // tests that the reduceKlingon function removes klingons correctly
    private static void testReduceKlingons() {
        System.out.println("Testing Quadrant reduceKlingons");

        Quadrant q = new Quadrant(3, 0, 1);

        q.reduceKlingons();

        assert (q.klingons() == 2);
        System.out.printf("Got %d klingons, expected 2\n", q.klingons());

        q.reduceKlingons();

        assert (q.klingons() == 1);
        System.out.printf("Got %d klingons, expected 1\n", q.klingons());

        q.reduceKlingons();

        assert (q.klingons() == 0);
        System.out.printf("Got %d klingons, expected 0\n", q.klingons());

        q.reduceKlingons();

        assert (q.klingons() == 0);
        System.out.printf("Got %d klingons, expected 0\n", q.klingons());

        System.out.println("Quadrant reduceKlingons success");
    }

    // generates a new galaxy (tests that the quadrant's constraints are correct)
    private static void generatesAGalaxy() {
        System.out.println("Quadrant stress test");

        long start = System.nanoTime();

        int numOf1Klingons = 0;
        int numOf2Klingons = 0;
        int numOf3Klingons = 0;
        int numOfBases = 0; 
        for (long i = 0; i < ITERATIONS; ++i) {
            Quadrant q = new Quadrant();

            if (q.klingons() == 1)
                numOf1Klingons++;
            else if (q.klingons() == 2)
                numOf2Klingons++;
            else if (q.klingons() == 3)
                numOf3Klingons++;

            if (q.bases() == 1)
                numOfBases++;
        }
        
        long end = System.nanoTime();
        long duration = (end - start) / 1000000;

        assert numOfBases >= 1 && numOfBases <= 2 : "Bases did not generate with the correct range";

        float percent1 = numOf1Klingons * 100 / ITERATIONS;
        float percent2 = numOf2Klingons * 100 / ITERATIONS;
        float percent3 = numOf3Klingons * 100 / ITERATIONS;
        float percent4 = numOfBases * 100 / ITERATIONS;

        System.out.printf("Number of quadrants with 1 klingon: %.2f%% \n", percent1);
        System.out.printf("Number of quadrants with 2 klingon: %.2f%% \n", percent2);
        System.out.printf("Number of quadrants with 3 klingon: %.2f%% \n", percent3);
        System.out.printf("Number of quadrants with 1 base: %.2f%% \n", percent4);
        
        System.out.printf("Time taken: %d ms\n", duration);
        
        System.out.println("Quadrant stress test success");
    }
    
    public static void main(String args[]) {
        testQuadrantGetters();
        testQuadrantConstructors();
        testReduceKlingons();
        generatesAGalaxy();
        
        Quadrant.whiteBoxTest();
        GameLib.testDriver();
    }

    private static final int ITERATIONS = 1000000;
}

/*
 * Sample Output
 * 
 * Testing Quadrant getters
 * Got 3 klingons, expected 3
 * Got 1 bases, expected 1
 * Got 8 stars, expected 8
 * Quadrant getters success
 * Testing Quadrant constructors
 * Got 2 klingons, expected between 0-3
 * Got 0 bases, expected between 0-1
 * Got 1 stars, expected between 1-8
 * Got Quadrant: Klingons(3), Bases(1), Stars(2), expected Quadrant: Klingons(3), Bases(1), Stars(2)
 * Quadrant constructor success
 * Testing Quadrant reduceKlingons
 * Got 2 klingons, expected 2
 * Got 1 klingons, expected 1
 * Got 0 klingons, expected 0
 * Got 0 klingons, expected 0
 * Quadrant reduceKlingons success
 * Quadrant stress test
 * Number of quadrants with 1 klingon: 20.00% <- this one may change by 1% due to noise
 * Number of quadrants with 2 klingon: 5.00% <- this one may change by 1% due to noise
 * Number of quadrants with 3 klingon: 1.00% <- this one may change by 1% due to noise
 * Number of quadrants with 1 base: 0.00% <- this one may change by 1% due to noise
 * Time taken: 56 ms <- this may change due to system hardware
 * Quadrant stress test success
 * Quadrant whitebox test
 * Got 318, expected 318
 * Got 212, expected 212
 * Got 318, expected "318"
 * Got 001, expected "001"
 * Quadrant whitebox test success
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
 * 
 */
