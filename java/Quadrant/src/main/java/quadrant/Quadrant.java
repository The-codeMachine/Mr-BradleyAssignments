package quadrant;

/**
 * A Quadrant holds the contents of the quadrant in terms
 * of the number of each item present. Each Quadrant may
 * hold Klingons [0..3], Bases [0..1], and Stars [1..8].
 *
 * The contents are packed into a 16-bit data type (uint16_t)
 * whose valid range is [1..318].
 *
 * The Klingons  value is stored in the 100's position
 * The StarBases value is stored in the  10's position
 * and the Stars value is stored in the   1's position
 *
 * The number of Klingons in the Quadrant is the only
 * value that may change, at this time.
 * 
 * There can only be 2 bases for the entire galaxy,
 * and there must be at least 1 base for the galaxy. 
 * There is a 4% chance for a base to generate inside a quadrant. 
 *
 * There is a 20% chance for there to be 1 klingon in a quadrant,
 * 5% chance for 2 klingons, and 2% chance for 3 klingons in the quadrant. 
 * 
 * Operations
 *
 *  o Construct a Quadrant( k, b, s )
 *  o Construct a Quadrant() // uses an RNG to generate a random quadrant
 *  o return the current number of Klingons
 *  o return the number of Star Bases
 *  o return the number of Stars
 *  o provide a textual presentation of the Quadrant for
 *      use with displaying the Galactic Map eg, "318", or "001"
 *      
 *  o provides the ability to decrement the number of Klingons    
 *  o ability to check if there is a base inside the quadrant
 *  o ability to remove, or add a base 
 *
 * @author Mr. Bradley
 * @version SPRING 2026
 */

import common.*;

public class Quadrant {
    /**
     * Generates a quadrant using a RNG
     */
    public Quadrant() {
        this(genKlingons(), genBases(), genStars());
    }

    /**
     * Constructs the Quadrant based off the number of klingons, bases, and stars
     * wanted
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
        if (klingons() > 0)
            kbs = setContent(klingons() - 1, bases(), stars());
    }

    /**
     * @return true if this quadrant has a base
     */
    public boolean hasBase() {
        return bases() >= 1;
    }

    /*
    Could not put the bases functions inside Galaxy 
    because they cannot effect the Quadrant's private 
    values. I cannot put it as default visibility because
    Galaxy and Quadrant are in different packages.
    */

    /**
     * Puts a base inside this quadrant, to ensure that there are at least 1
     * base inside a galaxy
     */
    public void putBase() {
        kbs = setContent(klingons(), bases() + 1, stars());
    }

    /**
     * Removes a base from a quadrant (to ensure it does not exceed 2 per galaxy)
     */
    public void removeBase() {
        kbs = setContent(klingons(), 0, stars());
    }

    
    /**
     * Generates the number of klingons in a quadrant using the following rules:
     * - 20% for 1 klingon to generate
     * - 5% for 2 klingons to generate
     * - 2% for 3 klingons to generate
     * 
     * @return the number of klingons for 1 quadrant
     */
    private static int genKlingons() {
        // klingons: 0 1 2 3
        // 73% 20% 5% 2%
        int r = GameLib.weightedChoice(new double[] { 0.73, 0.2, 0.05, 0.02 });

        return r;
    }

    /**
     * Generates the number of bases in a quadrant using the following rules:
     * - 4% chance for one base inside the quadrant
     * - No more than 2 per galaxy
     * 
     * @return the number of bases for 1 quadrant
     */
    private static int genBases() {
        // 4% chance of a quadrant having a base
        if (GameLib.chanceOf(4)) {
            return 1;
        }

        return 0;
    }

    /**
     * Randomly generates a random number of stars between 1-8
     * 
     * @return number of stars for 1 quadrant
     */
    private static int genStars() {
        // star min, star max
        return GameLib.randomInt(1, 8);
    }

    /**
     * Tests the setContent function, and the toString method
     */
    static void whiteBoxTest() {
        System.out.println("Quadrant whitebox test");

        Quadrant q = new Quadrant();

        q.kbs = setContent(3, 1, 8);

        assert q.kbs == 318 : "setContent did not set the content correctly";
        System.out.printf("Got %d, expected 318\n", (int) (q.kbs));

        q.kbs = setContent(2, 1, 2);

        assert q.kbs == 212 : "setContent did not set the content correctly";
        System.out.printf("Got %d, expected 212\n", (int) (q.kbs));

        q.kbs = setContent(3, 1, 8);

        // would be the toString method, however this is a static test
        assert "318".equals(q.toString())
        : "toString does not set the quadrant to a string correctly";
        System.out.printf("Got %s, expected \"318\"\n", q.toString());

        q.kbs = setContent(0, 0, 1);

        assert "001".equals(q.toString())
                : "toString does not set the quadrant to a string correctly";
        System.out.printf("Got %s, expected \"001\"\n", q.toString());

        System.out.println("Quadrant whitebox test success");

        // NOTE:
        // Invalid input cases are not programmatically tested here because setContent()
        // uses assertions. Assertion failures terminate the program and cannot be
        // caught or verified within the same execution flow.
        //
        // These cases were manually verified by running the program with assertions
        // enabled (-ea) and confirming that invalid inputs trigger assertion failures.
        //
        // This approach ensures correctness during development without introducing
        // exception handling, as per assignment constraints.

        // For example: setContent(-1, 0, 1); // triggers an Assertion error
    }

    /**
     * @return a 3-digit string (with leading zeros if necessary) representing
     *         the quadrant contents in KBS format
     */
    @Override
    public String toString() {
        return StringUtils.zeroFill((int)kbs, 3);
    }

    /**
     * Encodes klingons, bases, and stars into a single integer (KBS format)
     * 
     * @apiNote Invalid input triggers assertion failure when assertions are enabled
     *          (-ea)
     * 
     * @param klingons
     * @param bases
     * @param stars
     * 
     * @return Returns a formatted KBS value
     */
    static private char setContent(int klingons, int bases, int stars) {
        final int KLINGON_MIN = 0;
        final int KLINGON_MAX = 3;
        
        final int BASE_MIN = 0;
        final int BASE_MAX = 1;
        
        final int STAR_MAX = 8;
        final int STAR_MIN = 1;

        assert klingons >= KLINGON_MIN && klingons <= KLINGON_MAX : "Klingon out of range";
        assert bases >= BASE_MIN && bases <= BASE_MAX : "Base out of range";
        assert stars >= STAR_MIN && stars <= STAR_MAX : "Star out of range";

        return (char) (klingons * 100 + bases * 10 + stars);
    }

    /**
     * Populates a Quadrant with Klingons, bases, and stars
     *
     * Rules:
     * 
     * - 20% chance that 1 klingon is present 
     * - 5% chance that 2 klingons is present 
     * - 2% chance that 3 klingons is present 
     * - 73% chance that 0 klingons is present
     * 
     * - 4% chance that 1 star base is present
     * - Max of 1 per quadrant, and no more than 2 for each galaxy
     *  
     * @apiNote default visibility so that galaxy can use it when constructing itself
     * 
     * @return the Quadrant's new populated value
     */
    static char populate() {
        return setContent(genKlingons(), genBases(), genStars());
    }

    // Data
    private char kbs;
}

/*
 * Sample Output
 * 
 * Testing Quadrant getters
 * Got 3 klingons, expected 3
 * Got 1 bases, expected 1
 * Got 8 stars, expected 8
 * Quadrant getters success
 * Testing Quadrant constructors
 * Got 0 klingons, expected between 0-3
 * Got 0 bases, expected between 0-1
 * Got 1 stars, expected between 1-8
 * Got Quadrant: Klingons(3), Bases(1), Stars(2), expected Quadrant: Klingons(3), Bases(1), Stars(2)
 * Quadrant constructor success
 * Testing Quadrant reduceKlingons
 * Got 2 klingons, expected 2
 * Got 1 klingons, expected 1
 * Got 0 klingons, expected 0
 * Got 0 klingons, expected 0
 * Quadrant reduceKlingons success
 * Quadrant stress test
 * Number of quadrants with 1 klingon: 20.00% <- this one may change by 1% due to noise
 * Number of quadrants with 2 klingon: 4.00% <- this one may change by 1% due to noise
 * Number of quadrants with 3 klingon: 2.00% <- this one may change by 1% due to noise
 * Number of quadrants with 1 base: 0.00% <- this one may change by 1% due to iterations + noise
 * Time taken: 15 ms <- this one may change due to the amonut of iterations
 * Quadrant stress test success
 * Quadrant whitebox test
 * Got 318, expected 318
 * Got 212, expected 212
 * Got 318, expected "318"
 * Got 001, expected "001"
 * Quadrant whitebox test success
 * 
 */
