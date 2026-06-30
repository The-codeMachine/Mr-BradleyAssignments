package QuadrantMap;

import java.util.Scanner;

import quadrant.*;

public class Main {
    public static void main(String args[]) {
        System.out.println("QuadrantMap test");

        Quadrant q = new Quadrant();
        QuadrantMap m = new QuadrantMap(q, 4, 3);

        Scanner s = new Scanner(System.in);
        
        System.out.println(m);
        
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

        s.close();
    }
}

/**
 * Sample Output
 * 
 * QuadrantMap test
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |>!<|
 * --------------------------------
 *    |   |   |<*>|   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   | * |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * (5, 3): <     >
 * Is (5, 3) empty: true
 * (7, 8): <  *  >
 * Is (7, 8) empty: false
 * (7, 8): <     >
 * Is (7, 8) empty: true
 * QuadrantMap test success
 * 
 */