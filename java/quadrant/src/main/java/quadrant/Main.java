package quadrant;

public class Main {
    
    // ensures all the getters and working correctly
    private static void testQuadrantGetters() {
        System.out.println("Testing Quadrant getters");

        Quadrant q = new Quadrant(3, 1, 8);

        assert(q.klingons() == 3 && q.bases() == 1 && q.stars() == 8);

        System.out.println("Quadrant getters success");
    }

    // generate a quadrant with all the constructors, and ensures they work
    private static void testQuadrantConstructors() {
        System.out.println("Testing Quadrant constructors");

        Quadrant q = new Quadrant();

        assert(q.klingons() <= 3 && q.klingons() >= 0);
        assert(q.bases() <= 1 && q.bases() >= 0);
        assert(q.stars() <= 9 && q.stars() >= 1);

        Quadrant qw = new Quadrant(3, 1, 2);

        assert(qw.klingons() == 3 && qw.bases() == 1 && qw.stars() == 2);

        System.out.println("Quadrant constructor success");
    }

    // tests that the reduceKlingon function removes klingons correctly
    private static void testReduceKlingons() {
        System.out.println("Testing Quadrant reduceKlingons");

        Quadrant q = new Quadrant(3, 0, 1);

        q.reduceKlingons();
        
        assert(q.klingons() == 2);

        q.reduceKlingons();
                    
        assert(q.klingons() == 1);

        q.reduceKlingons();
        
        assert(q.klingons() == 0);

        q.reduceKlingons();

        assert(q.klingons() == 0);

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
 * Quadrant getters success
 * Testing Quadrant constructors
 * Quadrant constructor success
 * Testing Quadrant reduceKlingons
 * Quadrant reduceKlingons success
 * Quadrant stress test
 * Time taken: 13 ms
 * Quadrant stress test success
 * Quadrant whitebox test
 * Exception in thread "main" java.lang.AssertionError: Klingon out of range <- this is expected
        at quadrant.Quadrant.setContent(Quadrant.java:139)
        at quadrant.Quadrant.whiteBoxTest(Quadrant.java:118)
        at quadrant.Main.main(Main.java:83)
 * 
 */