/**
 * A galaxy holds 64 Quadrants, using an 8 by 8 grid. 
 * A galaxy can only have 2 bases at most, and a minimum
 * of 1 base.
 * 
 * Operations include:
 *  - Constructing a galaxy (creates 64 quadrants, and ensures it has at least 1 base, and at most 2)
 *  - index the quadrant using a 2d map 
 *  - printing the map 
 *  - formats the map into a string
 * 
 */

package galaxy;

import quadrant.Quadrant;
import common.GameLib;

public class Galaxy {
    public Galaxy() {
        totalBases = 0;
        populateGalaxy();
    }

    /**
     * Gets the quadrant located at [index][index2]
     * 
     * @param index 
     * @param index2
     * 
     * @apiNote index, and index2 must be between 0-7 (referencing an 8 by 8 grid)
     * @apiNote index represents the row
     * @apiNote index2 represents the column
     * 
     * @return a Quadrant located at [index][index2]
     */
    public Quadrant getQuadrant(int index, int index2) {
        assert index >= 0 && index <= 7 && index2 >= 0 && index2 <= 7 : "Index must be within given parameters (an 8, by 8 grid)";

        return map[index][index2];
    } 

    /**
     * Prints the map into the console
     */
    public void printMap() {
        System.out.println(this.toString());
    }

    /**
     * turns the galaxy into a string, similar to 
     * 
     * "
     * 004 104 014 006 008 005 002 001 
     * 002 105 002 206 007 002 102 008 
     * 008 004 008 005 103 004 005 008 
     * 001 205 104 003 003 004 018 007 
     * 001 006 003 003 108 005 001 005 
     * 008 006 106 006 002 003 002 006 
     * 005 001 001 105 001 006 008 004 
     * 004 003 204 002 108 002 205 106 
     * "
     * 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                sb.append(map[i][j]).append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * This populates the galaxy with quadrants.
     * It ensures it has at least one base, and a max of two bases
     */
    private void populateGalaxy() {
        totalBases = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map[i][j] = new Quadrant();

                if (map[i][j].hasBase()) {
                    if (totalBases < 2) {
                        totalBases++;
                    } else {
                        map[i][j].removeBase();
                    }
                }
            }
        }

        // Ensure at least 1 base exists
        if (totalBases < 1) {
            int i = GameLib.randomInt(0, 7);
            int j = GameLib.randomInt(0, 7);

            map[i][j].putBase();
            totalBases = 1;
        }
    }

    /**
     * tests the internal private functions
     */
    public static void whiteBoxTest() {
        System.out.println("White box test");

        Galaxy g = new Galaxy();

        g.printMap();

        System.out.printf("\n\n"); // padding between the maps

        System.out.println(g);

        // verifies there is the correct number of klingons, and bases
        int klingon1 = 0;
        int klingon2 = 0;
        int klingon3 = 0;
        int bases = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                Quadrant q = g.getQuadrant(i, j);
                
                if (q.hasBase()) {
                    bases++;
                }

                int klingons = q.klingons();
                if (klingons == 1) {
                    klingon1++;
                } else if (klingons == 2) {
                    klingon2++;
                } else if (klingons == 3) {
                    klingon3++;
                }
            }
        }

        double klingon1Percent = klingon1 * 100.0 / 64;
        double klingon2Percent = klingon2 * 100.0 / 64;
        double klingon3Percent = klingon3 * 100.0 / 64;
        double basePercent = bases * 100.0 / 64;

        System.out.printf("Percent of 1 Klingons: %.2f%%\n", klingon1Percent);
        System.out.printf("Percent of 2 Klingons: %.2f%%\n", klingon2Percent);
        System.out.printf("Percent of 3 Klingons: %.2f%%\n", klingon3Percent);
        System.out.printf("Percent of Bases: %.2f%%\n", basePercent);

        System.out.println("White box test success");
    }

    // Data
    private Quadrant[][] map = new Quadrant[8][8];
    private int totalBases;
}

/**
 * Sample Output
 * 
 * White box test
 * 006 105 004 202 005 107 002 008 <- maps may vary 
 * 002 007 015 105 003 108 002 007 
 * 005 106 106 107 115 106 103 008 
 * 002 006 003 008 002 003 101 005 
 * 006 001 304 008 005 008 002 001 
 * 001 002 003 005 105 005 102 108 
 * 005 107 003 006 005 101 108 002 
 * 004 005 102 007 001 002 001 301 
 * 
 * 
 * 006 105 004 202 005 107 002 008 
 * 002 007 015 105 003 108 002 007 
 * 005 106 106 107 115 106 103 008 
 * 002 006 003 008 002 003 101 005 
 * 006 001 304 008 005 008 002 001 
 * 001 002 003 005 105 005 102 108 
 * 005 107 003 006 005 101 108 002 
 * 004 005 102 007 001 002 001 301 
 * 
 * Percent of 1 Klingons: 28.00% <- may vary due to only generating 64 quadrants
 * Percent of 2 Klingons: 1.00%
 * Percent of 3 Klingons: 3.00%
 * Percent of Bases: 3.00% 
 * White box test success
 * 
 */