package galaxy;

import quadrant.Quadrant;

public class Main {
    private static void testIndex() {
        System.out.println("Index test");

        Galaxy g = new Galaxy();

        Quadrant q = g.getQuadrant(1, 3);

        System.out.println(q);

        g.printMap();

        System.out.println("Index test success");
    }

    public static void main(String[] args) {
        testIndex();
        Galaxy.whiteBoxTest();
    }
}

/**
 * Sample Output
 * 
 * Index test
 * 006
 * 004 006 018 011 003 102 002 108 <- maps may vary (suppose to be random)
 * 005 303 001 006 008 003 201 001 
 * 003 107 007 206 003 004 103 002 
 * 104 001 005 005 004 004 004 007 
 * 005 003 007 004 001 006 105 001 
 * 002 006 003 005 005 004 105 002 
 * 003 004 105 008 005 003 003 104 
 * 007 005 003 003 101 102 002 005 
 * 
 * Index test success
 * White box test
 * 007 007 005 003 105 008 007 003 
 * 008 008 002 202 008 004 007 006 
 * 106 105 004 007 101 003 004 208 
 * 004 106 007 005 008 102 107 008 
 * 004 003 003 004 008 102 007 005 
 * 008 006 102 208 002 302 106 202 
 * 002 201 004 001 001 006 007 006 
 * 008 003 004 008 006 203 107 103 
 * 
 * 
 * 
 * 007 007 005 003 105 008 007 003 
 * 008 008 002 202 008 004 007 006 
 * 106 105 004 007 101 003 004 208 
 * 004 106 007 005 008 102 107 008 
 * 004 003 003 004 008 102 007 005 
 * 008 006 102 208 002 302 106 202 
 * 002 201 004 001 001 006 007 006 
 * 008 003 004 008 006 203 107 103 
 * 
 * Percent of 1 Klingons: 18.00% <- may vary due to it only generating 64 quadrants
 * Percent of 2 Klingons: 9.00%
 * Percent of 3 Klingons: 1.00%
 * Percent of Bases: 0.00%
 * White box test success
 * 
 */