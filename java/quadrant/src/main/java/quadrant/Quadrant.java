package quadrant;

/**
 * A Quadrant_00 holds the contents of the quadrant in terms
 * of the number of each item present. Each Quadrant_00 may
 * hold Klingons [0..3], Bases [0..1], and Stars [1..8].
 *
 * The contents are packed into a 32-bit data type (int)
 * whose valid range is [1..318].
 *
 * The Klingons  value is stored in the 100's position
 * The StarBases value is stored in the  10's position
 * and the Stars value is stored in the   1's position
 *
 * The number of Klingons in the Quadrant_00 is the only
 * value that may change, at this time.
 *
 * Operations
 *
 *  o Construct a Quadrant_00( k, b, s )
 *  o return the current number of Klingons
 *  o return the number of Star Bases
 *  o return the number of Stars
 *  o provide a textual presentation of the Quadrant_00 for
 *      use with displaying the Galactic Map eg, "318", or "001"
 *      
 *  o provides the ability to decrement the number of Klingons    
 *
 * @author Mr. Bradley
 * @version SPRING 2026
 */

import java.util.Random;

public class Quadrant {
    Random rand = new Random();
    
    /**
     * Generates a quadrant using a RNG

         -> there are specific rules for creating quadrants
             we will look at them in future sessions
     */
    public Quadrant() {
        int k = rand.nextInt(KLINGON_MIN, KLINGON_MAX);
        int b = rand.nextInt(BASE_MIN, BASE_MAX);
        int s = rand.nextInt(STAR_MIN, STAR_MAX);

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
     * Removes one klingon from each quadrant
     */
    public void reduceKlingons() {
        if (klingons() >= 1)
            kbs -= 100;
    }
    
    /**
     * Tests the setContent function 
    */
    public void whiteBoxTest() {
       kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

       assert kbs == 318 : "setContent did not set the content correctly";

       kbs = setContent(2, 1, 2);

       assert kbs == 212 : "setContent did not set the content correctly";

       try {
        kbs = setContent(0, 129, 1233);

       } catch (RuntimeException e) {
        System.out.println("There was a runtime exception, success");
       }

       try {
        kbs = setContent(-43, -432, -123);

       } catch (RuntimeException e) {
        System.out.println("There was a runtime exception, success");
       }

       kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

       assert toString() == "318" : "toString does not set the quadrant to a string correctly";
    }
    
    /**
     * Returns a string value constructed from the number of klingons, bases, and stars
    */
    @Override
    public String toString() {
       return String.format("%d%d%d", klingons(), bases(), stars());
    }
    
    /**
     * Sets the raw KBS value to a new value (ensures it is in a valid range)
     * 
     * @param newValue
     * 
     * @return Returns a formatted KBS value
     */
    static private int setContent(int klingons, int bases, int stars) {
        if (klingons > KLINGON_MAX || klingons < KLINGON_MIN)
            throw new RuntimeException("Klingon exceed MAX, or dropped under MIN");

        if (bases > BASE_MAX || bases < BASE_MIN)
            throw new RuntimeException("Base exceed MAX, or dropped under MIN");

        if (stars > STAR_MAX || stars < STAR_MIN)
            throw new RuntimeException("Star exceed MAX, or dropped under MIN");

        return klingons * 100 + bases * 10 + stars;
    }
    
    // Data
    private int kbs; // this could (eventually) be a char type saving 50% storage
    
    // Constants
    static final int KLINGON_MAX = 3;
    static final int BASE_MAX = 1;
    static final int STAR_MAX = 8;
    static final int KLINGON_MIN = 0;
    static final int BASE_MIN = 0;
    static final int STAR_MIN = 1;
}
