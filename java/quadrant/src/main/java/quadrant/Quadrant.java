package quadrant;

import static common.MathUtils.clamp;

/**
 * The Quadrant class consists of the following operations:
 * - Get klingons (gets the number of klingons in a quadrant)  √
 * - Get bases (gets the number of bases in a quadrant)        √
 * - Get stars (gets the number of stars in a quadrant)        √
 * - Set a new raw kbs value (with clamping) ? public - API?
 * - Get the raw kbs value ? public API?
 * - Set a new value for each type (set a new klingon number, base number, or stars number) √
 * 
 * The Quadrant class can be constructed from:
 * - Nothing, will use a RNG to make a new random quadrant √
 * - From an initial value, which content is set to        √
 * - From klingons, bases, and stars, with the correct clamping  √
 * 
 * There can be between 0-3 klingons per quadrant, 0-1 bases per quadrant, and 1-9 stars per quadrant [1 -> 319]
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

        kbs = k * 100 + b * 10 + s;
    }

    /**
     * Constructs the Quadrant based off the number of klingons, bases, and stars wanted
     * 
     * @param klingons
     * @param bases
     * @param stars
     */
    public Quadrant(int klingons, int bases, int stars) {
        kbs = clampKBS(klingons * 100 + bases * 10 + stars);    // correct formula for encoding contents
    }
    
    /**
     * Constructs the Quadrant based off a raw KBS value

         When you expose the contents value as part of the API you break encapsulation!
         
     * 
     * @param initValue
     */
    private Quadrant(int initValue) {
        kbs = clampKBS(initValue);
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
     * You should NEVER require access to the encoded value.
     
     * @return Quadrant's raw KBS value
     */
    private int _raw() {
        return kbs;
    }

    /**
     * Sets the raw KBS value to a new value (clamps it)

         This should bot be part of the public API
         
     * 
     * @param newValue
     */
    private void setContent(int newValue) {
        kbs = clampKBS(newValue);
    }

    /**
     *    Mutator (set) Methods need to ensure that this SHOULD be part
         of the public? API

          Further, whenever you see the same formula or piece of code used
          (repeated) in code, you need to factor that code so there is ONLY
          one place for it to be maintains and tested!
     */
    
    /**
     * Sets the klingon to a new value, and clamps it.
     * It ensures none of the other values are changed √
         !! This last point is very important! -- Well done!
     * 
     * @param newValue
     */
    void setKlingons(int newValue) {
        newValue = clamp(newValue, KLINGON_MIN, KLINGON_MAX);
        
        kbs = newValue * 100 + bases() * 10 + stars();    // second copy of encoding formula
    }

    /**
     * Sets the base to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
     */
    void setBases(int newValue) {
        newValue = clamp(newValue, BASE_MIN, BASE_MAX);
        
        kbs = klingons() * 100 + newValue * 10 + stars();    // third copy of encoding formula
    }

    /**
     * Sets the stars to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
     */
    void setStars(int newValue) {
        newValue = clamp(newValue, STAR_MIN, STAR_MAX);
        
        kbs = klingons() * 100 + bases() * 10 + newValue;    // fourth copy of encoding formula
    }

    /**
     * Tests the clampKBS function to ensure it clamps the value correctly

         -> other methods remain un-tested!
         
     */
    void whiteBoxTest() {
        int value = clampKBS(319);    // 319 incorrect --> 318
        assert value == 319  : "Value did not clamp correctly";
    
        value = clampKBS(500);
        assert value == 301 : "Value did not clamp correctly";

        value = clampKBS(257);
        assert value == 217 : "Value did not clamp correctly";

        value = clampKBS(233);
        assert value == 213  : "Value did not clamp correctly";
    }

    /**
     * Returns a string value constructed from the number of klingons, bases, and stars
     */
    @Override
    public String toString() {
        return String.format("Klingons: %d, Bases: %d, Stars: %d", klingons(), bases(), stars());
    }

    /**
     * This is a good example where clamping may not be the correct choice.
         -> we would prefer not to use the KBS values directly (via parameters)
         -> if the user (programmers) attempt to set an invalid KBS value, they
             should be notified that it is invalid!; as this is an error in programming!
         -> silent correction of values need to be done with careful thought. 
             It can lead to very difficult to trace/debug.
             
     * @param kbs
     * @return kbs formatted to ensure that none of the values exceed its limit
     */
    private int clampKBS(int kbs) {
        int k = kbs / 100; // gets the number of klingons
        int b = (kbs / 10) % 10; // gets the number of bases
        int s = kbs % 10; // gets the number of stars

        k = clamp(k, KLINGON_MIN, KLINGON_MAX);
        b = clamp(b, BASE_MIN, BASE_MAX);
        s = clamp(s, STAR_MIN, STAR_MAX);

        // for example: 581 becomes 311
        // another example: 000 becomes 001

        return k * 100 + b * 10 + s;        // fifth encoding formula
    }

    // Data
    private int kbs;        // this could (eventually) be a char type saving 50% storage
                            // but it is correct that it is not so now! but should be commented
    
    // Constants
    static final int KLINGON_MAX = 3;
    static final int BASE_MAX = 1;
    static final int STAR_MAX = 9;        // 8!
    static final int KLINGON_MIN = 0;
    static final int BASE_MIN = 0;
    static final int STAR_MIN = 1;
}
