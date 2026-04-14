package quadrant;

public class Main {

    // generate a quadrant with all the constructors, and ensures they work
    private static void testQuadrantConstructors() {
        Quadrant q = new Quadrant();

        System.out.println(q);

        Quadrant qw = new Quadrant(3, 1, 2);

        System.out.println(qw);

        Quadrant qe = new Quadrant(3, 1, 2);

        System.out.println(qe);
    }

    // ensures all the getters and working correctly
    private static void testQuadrantGetters() {
        Quadrant q = new Quadrant(3, 1, 8);

        System.out.printf("Klingons: %d\n", q.klingons());
        System.out.printf("Bases: %d\n", q.bases());
        System.out.printf("Stars: %d\n", q.stars());
    }

    // tests that the reduceKlingon function removes klingons correctly
    private static void testReduceKlingons() {
        Quadrant q = new Quadrant(3, 0, 1);

        q.reduceKlingons();
        
        System.out.println(q);

        q.reduceKlingons();
                    
        System.out.println(q);

        q.reduceKlingons();
        
        System.out.println(q);

        q.reduceKlingons();
    }

    // stress tests the random number generator
    private static void stressTestRandomGenerator() {
        long start = System.nanoTime();

        for (long j = 0; j < 1000000; ++j) {
            new Quadrant();
        }

        long end = System.nanoTime();

        long duration = (end - start) / 1000000;

        System.out.printf("Time taken: %d ms\n", duration);
    }

    public static void main(String args[]) {
        testQuadrantConstructors();
        testQuadrantGetters();
        testReduceKlingons();
        stressTestRandomGenerator();

        Quadrant.whiteBoxTest();
    }
}

/*
 * Sample Output
 * 
 * Task :quadrant:run FAILED
 * 111 <- this one may change
 * 312
 * 312
 * Klingons: 3
 * Bases: 1
 * Stars: 8
 * 201
 * 101
 * 001
 * Time taken: 14 ms <- this one may change
 * Exception in thread "main" java.lang.AssertionError: Klingon out of range
        at quadrant.Quadrant.setContent(Quadrant.java:137)
        at quadrant.Quadrant.whiteBoxTest(Quadrant.java:116)
        at quadrant.Main.main(Main.java:69)
 * 
 */