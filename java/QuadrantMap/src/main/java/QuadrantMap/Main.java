package QuadrantMap;

import common.GameLib;
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

        m.insert(7, 8, QuadrantMap.STAR);

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
 * QuadrantMap test
 * Klingons: 0, Bases: 0, Stars: 8
 * Enterprise location: (7, 1)
 * --------------------------------
 *  * |   |   |   |   | * |<*>| * |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   | * |   |   |   |
 * --------------------------------
 *    | * |   |   |   |   | * |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *  * |   | * |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * (5, 3): <  *  >
 * Is (5, 3) empty: false
 * (7, 8): <  *  >
 * Is (7, 8) empty: false
 * (7, 8): <     >
 * Is (7, 8) empty: true
 * QuadrantMap test success
 * 
 */