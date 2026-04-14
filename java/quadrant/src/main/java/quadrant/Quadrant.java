package quadrant;

/**
 * A Quadrant holds the contents of the quadrant in terms
 * of the number of each item present. Each Quadrant may
 * hold Klingons [0..3], Bases [0..1], and Stars [1..8].
 *
 * The contents are packed into a 32-bit data type (int)
 * whose valid range is [1..318].
 *
 * The Klingons  value is stored in the 100's position
 * The StarBases value is stored in the  10's position
 * and the Stars value is stored in the   1's position
 *
 * The number of Klingons in the Quadrant is the only
 * value that may change, at this time.
 *
 * Operations
 *
 *  o Construct a Quadrant( k, b, s )
 *  o return the current number of Klingons
 *  o return the number of Star Bases
 *  o return the number of Stars
 *  o provide a textual presentation of the Quadrant for
 *      use with displaying the Galactic Map eg, "318", or "001"
 *      
 *  o provides the ability to decrement the number of Klingons    
 *
 * @author Mr. Bradley
 * @version SPRING 2026
 */

import java.util.Random;

public class Quadrant { 
    /**
     * Generates a quadrant using a RNG

         -> there are specific rules for creating quadrants
             we will look at them in future sessions
     */
    public Quadrant() {
        int k = RAND.nextInt(KLINGON_MIN, KLINGON_MAX + 1);
        int b = RAND.nextInt(BASE_MIN, BASE_MAX + 1);
        int s = RAND.nextInt(STAR_MIN, STAR_MAX + 1); // upper bound adjusted to make range inclusive

        kbs = setContent(k, b, s);
    }

    /**
     * Constructs the Quadrant based off the number of klingons, bases, and stars wanted
     * 
     * @param klingons
     * @param bases
     * @param stars
     */
    public Quadrant(int klingons, int bases, int stars) {
        kbs = setContent(klingons, bases, stars);
    }

    /**
     * 
     * @return the number of klingons in the quadrant
     */
    public int klingons() {
        return kbs / 100;
    }

    /**
     * 
     * @return the number of bases in the quadrant
     */
    public int bases() {
        return (kbs / 10) % 10;
    }

    /**
     * 
     * @return the number of stars in the quadrant
     */
    public int stars() {
        return kbs % 10;
    }
   
    /**
     * Removes one Klingon from this quadrant if one exists
     */
    public void reduceKlingons() {
        if (klingons() >= 1)
            kbs -= 100;
    }
    
    /**
     * Tests the setContent function 
    */
    public static void whiteBoxTest() {
       System.out.println("Quadrant whitebox test");

       int kbs = 0; // testing kbs
        
       kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

       assert kbs == 318 : "setContent did not set the content correctly";

       kbs = setContent(2, 1, 2);

       assert kbs == 212 : "setContent did not set the content correctly";
       
       kbs = setContent(3, 1, 8);

       // would be the toString method, however this is a static test
       assert String.format("%03d", kbs).equals("318") : "toString does not set the quadrant to a string correctly";

       kbs = setContent(0, 0, 1);

       assert String.format("%03d", kbs).equals("001") : "toString does not set the quadrant to a string correctly";

       kbs = setContent(-43, 129, -123);
    }
    
    /**
     * Returns a string value constructed from the number of klingons, bases, and stars
    */
    @Override
    public String toString() {
       return String.format("%03d", kbs);
    }
    
    /**
     * Encodes klingons, bases, and stars into a single integer (KBS format)
     * 
     * @param klingons
     * @param bases
     * @param stars
     * 
     * @return Returns a formatted KBS value
     */
    static private int setContent(int klingons, int bases, int stars) {
        assert klingons >= KLINGON_MIN && klingons <= KLINGON_MAX : "Klingon out of range";
        assert bases >= BASE_MIN && bases <= BASE_MAX : "Base out of range";
        assert stars >= STAR_MIN && stars <= STAR_MAX : "Star out of range";

        return klingons * 100 + bases * 10 + stars;
    }
    
    // Data
    private int kbs; // this could (eventually) be a char type saving 50% storage
    
    // Constants
    private static final int KLINGON_MAX = 3;
    private static final int BASE_MAX = 1;
    private static final int STAR_MAX = 8;
    private static final int KLINGON_MIN = 0;
    private static final int BASE_MIN = 0;
    private static final int STAR_MIN = 1;

    private static final Random RAND = new Random();
}
