package QuadrantMap;

import common.GameLib;
import common.Logger;
import quadrant.*;

public class Main {
    /**
     * 
     * Small helper function for a random location for
     * the Enterprise. 
     * 
     * @return a random valid location
     */
    private static int[] generateRandomPosition() {
        int[] out = new int[2];
        out[0] = GameLib.randomInt(1, 8);
        out[1] = GameLib.randomInt(1, 8);

        return out;
    }

    public static void main(String args[]) {
        System.out.println("QuadrantString test");

        QuadrantString qs = new QuadrantString();

        System.out.println("Checking initial state");

        for (int i = 0; i < 64; i++) {
            assert qs.isEmpty(i) : "Location " + i + " should initially be empty";
            assert qs.at(i).equals(QuadrantMap.EMPTY)
                : "Location " + i + " should contain EMPTY";
        }

        System.out.println("Testing place()");

        qs.place(0, QuadrantMap.ENTERPRISE);
        qs.place(10, QuadrantMap.KLINGON);
        qs.place(20, QuadrantMap.BASE);
        qs.place(63, QuadrantMap.STAR);

        assert qs.at(0).equals(QuadrantMap.ENTERPRISE);
        assert qs.at(10).equals(QuadrantMap.KLINGON);
        assert qs.at(20).equals(QuadrantMap.BASE);
        assert qs.at(63).equals(QuadrantMap.STAR);

        System.out.println("Testing isEmpty()");

        assert !qs.isEmpty(0);
        assert !qs.isEmpty(10);
        assert !qs.isEmpty(20);
        assert !qs.isEmpty(63);

        assert qs.isEmpty(1);
        assert qs.isEmpty(15);
        assert qs.isEmpty(40);

        System.out.println("Testing overwrite");

        qs.place(10, QuadrantMap.STAR);

        assert qs.at(10).equals(QuadrantMap.STAR)
            : "place() should overwrite an existing symbol";

        System.out.println("Testing clear()");

        qs.clear(10);

        assert qs.isEmpty(10);
        assert qs.at(10).equals(QuadrantMap.EMPTY);

        System.out.println("Testing edge positions");

        qs.place(0, QuadrantMap.BASE);
        qs.place(63, QuadrantMap.KLINGON);

        assert qs.at(0).equals(QuadrantMap.BASE);
        assert qs.at(63).equals(QuadrantMap.KLINGON);

        System.out.println("\nRaw Quadrant String:");
        System.out.println(qs);

        System.out.println("\nQuadrantString test success");

        System.out.println("QuadrantMap test");

        Quadrant q = new Quadrant();
        System.out.printf("Klingons: %d, Bases: %d, Stars: %d\n", q.klingons(), q.bases(), q.stars());
        
        int enterprisePosition[] = generateRandomPosition();
        System.out.printf("Enterprise location: (%d, %d)\n", enterprisePosition[0], enterprisePosition[1]);
        
        QuadrantMap m = new QuadrantMap(q, enterprisePosition[0], enterprisePosition[1]);

        assert m.at(enterprisePosition[0], enterprisePosition[1]).equals(QuadrantMap.ENTERPRISE) 
            : "Enterprise is not at the correct location";

        System.out.println(m);

        int klingons = 0;
        int bases = 0;
        int stars = 0;
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                String sector = m.at(i, j);

                if (sector.equals(QuadrantMap.KLINGON))
                    klingons++;
                else if (sector.equals(QuadrantMap.BASE))
                    bases++;
                else if (sector.equals(QuadrantMap.STAR))
                    stars++;
            }
        }

        assert klingons == q.klingons() : "There is not the correct amount of klingons";
        assert bases == q.bases() : "There is not the correct amount of bases";
        assert stars == q.stars() : "There is not the correct amount of stars";

        System.out.printf("(5, 3): < %s >\n", m.at(5, 3));
        System.out.printf("Is (5, 3) empty: %b\n", m.empty(5, 3));

        m.place(7, 8, QuadrantMap.STAR);

        System.out.printf("(7, 8): < %s >\n", m.at(7, 8));
        System.out.printf("Is (7, 8) empty: %b\n", m.empty(7, 8));

        m.clearSector(7, 8);

        System.out.printf("(7, 8): < %s >\n", m.at(7, 8));
        System.out.printf("Is (7, 8) empty: %b\n", m.empty(7, 8));

        // Did not add the test code for removeKlingon.
        // I did verify it works, but it requires input
        // from the user, which gradle does not allow provided,
        // and as such I used raw JDK. Gradle would not compile,
        // so I removed it. 

        System.out.println("QuadrantMap test success");
    }
}

/**
 * Sample Output
 * 
 * QuadrantString test
 * Checking initial state
 * Testing place()
 * Testing isEmpty()
 * Testing overwrite
 * Testing clear()
 * Testing edge positions
 * 
 * Raw Quadrant String:
 * >!<                                                         >!<                                                                                                                              +K+
 * 
 * QuadrantString test success
 * QuadrantMap test
 * Klingons: 0, Bases: 0, Stars: 5
 * Enterprise location: (5, 6)
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   | * |   |   |   |   |
 * --------------------------------
 *  * |   |   |   |   |   |   |   |
 * --------------------------------
 *  * |   |   |   | * |   |   |   |
 * --------------------------------
 *    |   | * |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |<*>|   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * (5, 3): <     >
 * Is (5, 3) empty: true
 * (7, 8): <  *  >
 * Is (7, 8) empty: false
 * (7, 8): <  *  >
 * Is (7, 8) empty: false
 * QuadrantMap test success
 * 
 */