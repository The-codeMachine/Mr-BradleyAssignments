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
 *  o Construct a Quadrant() // uses an RNG to generate a random quadrant
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
     */
    public Quadrant() {
        totalQuadrants++;
        kbs = setContent(genKlingons(), genBases(), genStars());
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
        totalQuadrants++;
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
        if (kbs >= 100) // removes overhead (instead of using klingons() >= 1)
            kbs -= 100;
    }

    /**
     * Tests the setContent function, and the toString method
     */
    public static void whiteBoxTest() {
        System.out.println("Quadrant whitebox test");

        Quadrant q = new Quadrant();

        q.kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

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
        return String.format("%03d", (int) kbs);
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
        assert klingons >= KLINGON_MIN && klingons <= KLINGON_MAX : "Klingon out of range";
        assert bases >= BASE_MIN && bases <= BASE_MAX : "Base out of range";
        assert stars >= STAR_MIN && stars <= STAR_MAX : "Star out of range";

        return (char) (klingons * 100 + bases * 10 + stars);
    }

    /**
     * Generates the number of klingons in a quadrant using the following rules:
     * - 20% for 1 klingon to generate 
     * - 4% for 2 klingons to generate
     * - 2% for 3 klingons to generate
     * 
     * @return the number of klingons for 1 quadrant
     */
    static private int genKlingons() {
        int r = RAND.nextInt(1, 100);
        
        if (r <= 20)      return 1; // 20% chance of 1 klingon to exist in this quadrant
        else if (r <= 24) return 2; // 4% chance of 2 klingon to exist in this quadrant
        else if (r <= 26) return 3; // 2% chance of 3 klingon to exist in this quadrant

        return 0;
    }

    /**
     * Generates the number of bases in a quadrant using the following rules:
     * - 4% chance for one base inside the quadrant
     *  - No more than 2 per galaxy
     * 
     * @return the number of bases for 1 quadrant
     */
    static private int genBases() {
        if (totalBases < BASE_MAX_GALAXY) {
            // 4% chance of a quadrant having a base
            if (RAND.nextInt(1, 100) <= BASE_CHANCE) {
                totalBases++;
                return 1;
            }
        }

        // checks if there has not been any bases generated yet, 
        // if not then add one to the last quadrant
        if (totalQuadrants == AMOUNT_OF_QUADRANTS && totalBases == 0) {
            totalBases++;
            return 1;
        }

        return 0;
    }

    /**
     * Randomly generates a random number of stars between 1-8
     *  
     * @return number of stars for 1 quadrant
     */
    static private int genStars() {
        return RAND.nextInt(STAR_MIN, STAR_MAX);
    }

    // Data
    private char kbs;

    static int totalBases = 0;
    static int totalQuadrants = 0;

    private static final Random RAND = new Random();

    // Constants
    private static final int KLINGON_MAX = 3;
    private static final int BASE_MAX = 1;
    private static final int STAR_MAX = 8;
    private static final int KLINGON_MIN = 0;
    private static final int BASE_MIN = 0;
    private static final int STAR_MIN = 1;

    private static final int BASE_MAX_GALAXY = 2;
    private static final int BASE_CHANCE = 4;
    
    private static final int AMOUNT_OF_QUADRANTS = 64;
    
}
