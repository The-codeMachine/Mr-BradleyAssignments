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
        
        System.out.printf("Klingons: %d\n", m.klingons());
        System.out.printf("Bases: %d\n", m.bases());
        System.out.printf("Stars: %d\n", m.stars());

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
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   | * |   |   |   |
 * --------------------------------
 *    |   |   |   |<*>|   |   | * |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   | * |
 * --------------------------------
 *    |   |   |   |   |   |   | * |
 * --------------------------------
 *  * |   |   |   |   |   |   |   |
 * Klingons: 0, Bases: 0, Stars: 5
 * (5, 3): <  *  >
 * Is (5, 3) empty: false
 * Klingons: 0
 * Bases: 0
 * Stars: 5
 * QuadrantMap test success
 * 
 */