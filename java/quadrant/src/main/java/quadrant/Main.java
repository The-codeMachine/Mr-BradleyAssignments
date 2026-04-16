package quadrant;

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

        assert (q.klingons() <= 3 && q.klingons() >= 0);
        System.out.printf("Got %d klingons, expected between 0-3\n", q.klingons());

        assert (q.bases() <= 1 && q.bases() >= 0);
        System.out.printf("Got %d bases, expected between 0-1\n", q.bases());

        assert (q.stars() <= 9 && q.stars() >= 1);
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

    // stress tests the random number generator
    private static void stressTestRandomGenerator() {
        System.out.println("Quadrant stress test");

        long start = System.nanoTime();

        for (long j = 0; j < 1000000; ++j) {
            new Quadrant();
        }

        long end = System.nanoTime();

        long duration = (end - start) / 1000000;

        System.out.printf("Time taken: %d ms\n", duration);

        System.out.println("Quadrant stress test success");
    }

    public static void main(String args[]) {
        testQuadrantGetters();
        testQuadrantConstructors();
        testReduceKlingons();
        stressTestRandomGenerator();

        Quadrant.whiteBoxTest();
    }
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
 * Got 1 klingons, expected between 0-3
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
 * Time taken: 14 ms
 * Quadrant stress test success
 * Quadrant whitebox test
 * Got 318, expected 318
 * Got 212, expected 212
 * Got 318, expected "318"
 * Got 001, expected "001"
 * Quadrant whitebox test success
 * 
 */
