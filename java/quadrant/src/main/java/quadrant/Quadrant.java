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

public class Quadrant {
    
    // Constructs a quadrant using an initial value
    public Quadrant(int initValue) {
        kbs = clampKBS(initValue);
    }

    // Constructs a new quadrant based off the number of klingons, bases, and stars
    public Quadrant(int klingons, int bases, int stars) {
        kbs = clampKBS(klingons * 100 + bases * 10 + stars);
    }

    public int klingons() {
        return kbs / 100;
    }

    public int bases() {
        return (kbs / 10) % 10;
    }

    public int stars() {
        return kbs % 10;
    }

    // Returns the Quadrant's raw kbs value
    int raw() {
        return kbs;
    }

    // Sets the raw kbs value to a new value
    void setContent(int newValue) {
        kbs = clampKBS(newValue);
    }

    // Sets a new klingon value (does not affect the other)
    void setKlingons(int newValue) {
        newValue = clamp(newValue, KLINGON_MIN, KLINGON_MAX);
        
        kbs = newValue * 100 + bases() * 10 + stars();
    }

    // Sets a new base value (does not affect the other)
    void setBases(int newValue) {
        newValue = clamp(newValue, BASE_MIN, BASE_MAX);
        
        kbs = klingons() * 100 + newValue * 10 + stars();
    }

    // Sets a new star value (does not affect the other)
    void setStars(int newValue) {
        newValue = clamp(newValue, STAR_MIN, STAR_MAX);
        
        kbs = klingons() * 100 + bases() * 10 + newValue;
    }

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
