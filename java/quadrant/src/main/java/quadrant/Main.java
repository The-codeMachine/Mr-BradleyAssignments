package quadrant;

public class Main {

    // generate a quadrant with all the constructors, and ensures they work
    private static void testQuadrantConstructors() {
        Quadrant q = new Quadrant();

        System.out.println(q);

        Quadrant qw = new Quadrant(312);

        System.out.println(qw);

        Quadrant qe = new Quadrant(3, 1, 2);

        System.out.println(qe);

        Quadrant qr = new Quadrant(493);

        System.out.println(qr);

        Quadrant qt = new Quadrant(5, 4, 3);

        System.out.println(qt);
    }

    // ensures all the getters and working correctly
    private static void testQuadrantGetters() {
        Quadrant q = new Quadrant(319);

        System.out.printf("Klingons: %d\n", q.klingons());
        System.out.printf("Bases: %d\n", q.bases());
        System.out.printf("Stars: %d\n", q.stars());
    }

    // ensures all the setters are working correctly
    private static void testQuadrantSetters() {
        Quadrant q = new Quadrant(319);

        System.out.println(q);

        q.setKlingons(1);
        q.setBases(0);
        q.setStars(7);

        System.out.println(q);
    }

    // stress tests the random number generator
    private static void stressTestRandomGenerator() {
        long start = System.nanoTime();
        
        for (long j = 0; j < 1000000; ++j) {
            Quadrant q = new Quadrant();
        }

        long end = System.nanoTime();

        long duration = (end - start) / 1000000;

        System.out.printf("Time taken: %d ms", duration);
    }

    public static void main(String args[]) {
        testQuadrantConstructors();
        testQuadrantGetters();
        testQuadrantSetters();
        stressTestRandomGenerator();

        Quadrant q = new Quadrant();
        q.whiteBoxTest();
    } 
}

/* Sample Output

Klingons: 1, Bases: 1, Stars: 8 <- this one can change
Klingons: 3, Bases: 1, Stars: 2
Klingons: 3, Bases: 1, Stars: 2
Klingons: 3, Bases: 1, Stars: 3
Klingons: 3, Bases: 1, Stars: 3
Klingons: 3
Bases: 1
Stars: 9
Klingons: 3, Bases: 1, Stars: 9
Klingons: 1, Bases: 0, Stars: 7
Time taken: 51 ns <- this one can change

 */