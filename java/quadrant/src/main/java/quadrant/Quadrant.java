package quadrant;

import static common.MathUtils.clamp;

/**
 * The Quadrant class consists of the following operations:
 * - Get klingons (gets the number of klingons in a quadrant)
 * - Get bases (gets the number of bases in a quadrant)
 * - Get stars (gets the number of stars in a quadrant)
 * - Set a new raw kbs value (with clamping)
 * - Get the raw kbs value
 * - Set a new value for each type (set a new klingon number, base number, or stars number)
 * 
 * The Quadrant class can be constructed from:
 * - Nothing, will use a RNG to make a new random quadrant
 * - From an initial value, which content is set to
 * - From klingons, bases, and stars, with the correct clamping
 * 
 * There can be between 0-3 klingons per quadrant, 0-1 bases per quadrant, and 1-9 stars per quadrant [1 -> 319]
 * 
 */

import java.util.Random;

public class Quadrant {
    
    /**
     * Generates a quadrant using a RNG
     */
    public Quadrant() {
        Random rand = new Random();

        int k = rand.nextInt(KLINGON_MIN, KLINGON_MAX);
        int b = rand.nextInt(BASE_MIN, BASE_MAX);
        int s = rand.nextInt(STAR_MIN, STAR_MAX);

        kbs = k * 100 + b * 10 + s;
    }

    /**
     * Constructs the Quadrant based off a raw KBS value
     * 
     * @param initValue
     */
    public Quadrant(int initValue) {
        kbs = clampKBS(initValue);
    }

    /**
     * Constructs the Quadrant based off the number of klingons, bases, and stars wanted
     * 
     * @param klingons
     * @param bases
     * @param stars
     */
    public Quadrant(int klingons, int bases, int stars) {
        kbs = clampKBS(klingons * 100 + bases * 10 + stars);
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
     * 
     * @return Quadrant's raw KBS value
     */
    int raw() {
        return kbs;
    }

    /**
     * Sets the raw KBS value to a new value (clamps it)
     * 
     * @param newValue
     */
    void setContent(int newValue) {
        kbs = clampKBS(newValue);
    }

    /**
     * Sets the klingon to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
     */
    void setKlingons(int newValue) {
        newValue = clamp(newValue, KLINGON_MIN, KLINGON_MAX);
        
        kbs = newValue * 100 + bases() * 10 + stars();
    }

    /**
     * Sets the base to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
     */
    void setBases(int newValue) {
        newValue = clamp(newValue, BASE_MIN, BASE_MAX);
        
        kbs = klingons() * 100 + newValue * 10 + stars();
    }

    /**
     * Sets the stars to a new value, and clamps it.
     * It ensures none of the other values are changed
     * 
     * @param newValue
     */
    void setStars(int newValue) {
        newValue = clamp(newValue, STAR_MIN, STAR_MAX);
        
        kbs = klingons() * 100 + bases() * 10 + newValue;
    }

    /**
     * Tests the clampKBS function to ensure it clamps the value correctly
     */
    void whiteBoxTest() {
        int value = clampKBS(319);
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
     * 
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

        return k * 100 + b * 10 + s;
    }

    // Data
    private int kbs;
    
    // Constants
    static final int KLINGON_MAX = 3;
    static final int BASE_MAX = 1;
    static final int STAR_MAX = 9;
    static final int KLINGON_MIN = 0;
    static final int BASE_MIN = 0;
    static final int STAR_MIN = 1;
}
