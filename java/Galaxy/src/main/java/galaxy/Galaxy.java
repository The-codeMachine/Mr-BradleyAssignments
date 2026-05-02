/**
 * A galaxy holds 64 Quadrants, using an 8 by 8 grid. 
 * A galaxy can only have 2 bases at most, and a minimum
 * of one base.
 * 
 * Operations include:
 *  - Constructing a galaxy (creates 64 quadrants, and ensures it has at least 1 base, and at most 2)
 *  - index the quadrant using a 2d map 
 *  - printing the map 
 *  - formats the map into a string
 * 
 * Contains a Quadrant variable named map which is a 8 by 8 grid of quadrants. 
 * 
 */

package galaxy;

import quadrant.Quadrant;
import common.GameLib;

public class Galaxy {
    public Galaxy() {
        populateGalaxy();
    }

    /**
     * Gets the quadrant located at [index][index2]
     * 
     * @param index
     * @param index2
     * 
     * @return a Quadrant located at [@index][@index2]
     */
    public Quadrant getQuadrant(int index, int index2) {
        return map[index][index2];
    } 

    /**
     * Prints this map into the console
     */
    public void printMap() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                System.out.printf(map[i][j].toString() + " ");
            }

            System.out.printf("\n");
        }
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
        String s = "";

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                s += map[i][j].toString() + " ";
            }

            s += "\n";
        }

        return s;
    }

    /**
     * This populates the galaxy with quadrants.
     * It ensures it has at least one base, and a max of two bases
     */
    private void populateGalaxy() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map[i][j] = new Quadrant(); 
            }
        }

        if (GameLib.totalBases < 1) {
            int i = GameLib.randomInt(0, 7);
            int j = GameLib.randomInt(0, 7);

            map[i][j].putBase();
        }
    }

    public static void whiteBoxTest() {
        Galaxy g = new Galaxy();

        g.printMap();
    }

    // Data
    private Quadrant[][] map = new Quadrant[8][8];
}
