package quadrant;

/**
 * The Quadrant class consists of the following operations:
 * - Get klingons (gets the number of klingons in a quadrant)  √
 * - Get bases (gets the number of bases in a quadrant)        √
 * - Get stars (gets the number of stars in a quadrant)        √
 * - Set a new value for each type (set a new klingon number, base number, or stars number) √
 * 
 * The Quadrant class can be constructed from:
 * - Nothing, will use a RNG to make a new random quadrant √
 * - From klingons, bases, and stars, with the correct clamping  √
 * 
 * There can be between 0-3 klingons per quadrant, 0-1 bases per quadrant, and 1-8 stars per quadrant [1 -> 318]
 * NB: Stars cap out at 8. [0..318]
 * 
 */

import java.util.Random;

public class Quadrant {
    
    /**
     * Generates a quadrant using a RNG

         -> there are specific rules for creating quadrants
             we will look at them in future sessions
     */
    public Quadrant() {
        Random rand = new Random();

        int k = rand.nextInt(KLINGON_MIN, KLINGON_MAX);
        int b = rand.nextInt(BASE_MIN, BASE_MAX);
        int s = rand.nextInt(STAR_MIN, STAR_MAX);

        setContent(k, b, s);
    }

    /**
     * Constructs the Quadrant based off the number of klingons, bases, and stars wanted
     * 
     * @param klingons
     * @param bases
     * @param stars
     */
    public Quadrant(int klingons, int bases, int stars) {
        setContent(klingons, bases, stars);
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
     * Sets the klingon to a new value, and clamps it.
     * It ensures none of the other values are changed √
     * !! This last point is very important! -- Well done!
     * 
     * @param newValue
     */
    public void setKlingons(int newValue) {
      setContent(newValue, bases(), stars());
    }
    
    /**
     * Sets the base to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
    */
    public void setBases(int newValue) {
       setContent(klingons(), newValue, stars());
    }
    
    /**
     * Sets the stars to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
    */
    public void setStars(int newValue) {
       setContent(klingons(), bases(), newValue);
    }
    
    /**
     * Tests the setContent function 
    */
    public void whiteBoxTest() {
       setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

       assert kbs == 318 : "setContent did not set the content correctly";

       setContent(2, 1, 2);

       assert kbs == 212 : "setContent did not set the content correctly";

       try {
        setContent(0, 129, 1233);

       } catch (RuntimeException e) {
        System.out.println("There was a runtime exception, success");
       }
    }
    
    /**
     * Returns a string value constructed from the number of klingons, bases, and stars
    */
   @Override
   public String toString() {
       return String.format("Klingons: %d, Bases: %d, Stars: %d", klingons(), bases(), stars());
    }
    
    /**
     * Sets the raw KBS value to a new value (ensures it is in a valid range)
     * 
     * @param newValue
     */
    private void setContent(int klingons, int bases, int stars) {
        if (klingons > KLINGON_MAX || klingons < KLINGON_MIN)
            throw new RuntimeException("Klingon exceed MAX, or dropped under MIN");

        if (bases > BASE_MAX || bases < BASE_MIN)
            throw new RuntimeException("Base exceed MAX, or dropped under MIN");

        if (stars > STAR_MAX || stars < STAR_MIN)
            throw new RuntimeException("Star exceed MAX, or dropped under MIN");

        kbs = klingons * 100 + bases * 10 + stars;
    }
    
    // Data
    private int kbs;        // this could (eventually) be a char type saving 50% storage
    // but it is correct that it is not so now! but should be commented
    
    // Constants
    static final int KLINGON_MAX = 3;
    static final int BASE_MAX = 1;
    static final int STAR_MAX = 8;
    static final int KLINGON_MIN = 0;
    static final int BASE_MIN = 0;
    static final int STAR_MIN = 1;
}
