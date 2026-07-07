package QuadrantMap;

import common.GameLib;
import quadrant.*;

/**
 * TODO:
 * Assertions are currently used to document preconditions during
 * development, and handle exceptions. Error handling with exceptions
 * return values, etc. will be visited later as the design evolves.  
 */

/**
 * QuadrantMap handles all of the movement and positional
 * status for all objects within a Quadrant. Currently this
 * includes: klingons, stars, bases, and the Enterprise. 
 * Operations include:
 *  - Construction (through Quadrant)
 *  - Insert an object
 *  - Clear a sector
 *  - Move an object
 *  - Remove an object
 *  - Check what object is at a certain sector
 *  - Check if a sector is empty
 *  - Convert map to string
 *
 * Currently, there are 8 rows and 8 columns, with each
 * symbol being 3 big.
 * 
 * All public methods use 1-based coordinates because they represent
 * the quadrant from the player's perspective. Players naturally
 * think of the first sector as (1,1), rather than (0,0).
 *
 * Private helper methods use 0-based coordinates because the internal
 * String representation uses Java's natural 0-based indexing. This
 * simplifies conversion between 2D sector coordinates and the 1D
 * String representation.
 *
 * Conversion between the two coordinate systems occurs only at the
 * public API boundary. Public methods convert to 0-based coordinates
 * before calling private helper methods.
 * 
 *
 */
public class QuadrantMap {
    /**
     * 
     * Constructs a QuadrantMap from quadrant Q,
     * setting Enterprise's coordinates to (x, y).
     * 
     * Design Note: 
     * The Enterprise position is supplied separately because 
     * a QuadrantMap is intended to represent the visible state
     * of a quadrant after the Enterprise has entered it. The
     * Quadrant stores the klingons, bases, and stars, while the
     * Enterprise is considered part of the game state. 
     * 
     * @param q
     * @param x
     * @param y
     */
    public QuadrantMap(Quadrant q, int x, int y) {
        initializeQuadrant(q, x, y);
    }

    /**
     * 
     * Writes a fixed-width symbol into the specified sector.
     * Uses substring methods to change the characters within 
     * the quadrantString by getting all previous characters, 
     * and all characters after, and inserting the new characters. 
     * 
     * @param x
     * @param y
     * @param value
     */
    public void insert(int x, int y, String value) {
        x--; y--;

        assert validPos(x, y) : "X and Y must be valid positions";
        assert value.length() == SYMBOL_SIZE : "Value must be exactly the same as SYMBOL_SIZE";

        int index = getIndexFrom(x, y);
        quadrantString = quadrantString.substring(0, index)  // prefix
        + value                                              // infix
        + quadrantString.substring(index + SYMBOL_SIZE);     // postfix
    }
    
    /**
     * 
     * Removes whatever occupies the specified sector.
     * Clearing is implemented by replacing the sector with
     * the empty-space symbol.
     * 
     * @param x
     * @param y
     * 
     */
    public void clearSector(int x, int y) {
        // wpuld be great to know where an assert fails in insert when called by another method -- we will come back to this
        insert(x, y, EMPTY);
    }
        
    /**
     * 
     * Moves a value from (x, y) to (newX, newY). It does
     * this by checking if (x, y) is actually the value, and
     * then clearing it, and inserting it into (newX, newY) after 
     * verifying that (newX, newY) is empty. Can be used to move
     * Enterprise or Klingons. 
     *
     * @param x
     * @param y
     * @param newX
     * @param newY
     * @param value
     */
    public void move(int x, int y, int newX, int newY, String value) {
        // you assert with expressions
        assert validPos(x - 1, y - 1) : "(x, y) sector must be valid";
        assert validPos(newX - 1, newY - 1) : "(newX, newY) sector must be valid";

        // then without wrong unadjusted x, y values
        assert at(x, y).equals(value) : "Original sector (x, y) must be == value";

        // then do the asserts again - in these calls ( just pointing it out - its something we will address later )
        if (empty(newX, newY)) {
            clearSector(x, y);
            insert(newX, newY, value);
        }
    }

    /**
     * 
     * Clears a sector only if it has value as 
     * its object. If it does then it is cleared. 
     * 
     * @param x
     * @param y
     * @param object
     */
    public void removeObject(int x, int y, String object) {
        assert validPos(x - 1, y - 1) : "Sector (x, y) must be valid";
        assert at(x, y).equals(object) : "Sector (x, y) must be the object"; 
        // at is a public method so we keep the coordinates 1-based 
        
        clearSector(x, y);
    }

    /**
     * 
     * Returns the symbol stored at the specified sector.
     * The 2D coordinates are converted into a 1D index into
     * the backing String, and the fixed-width symbol stored
     * at that location is returned.
     * 
     * @param x
     * @param y
     * @return the symbol as a string from (x, y)
     */
    public String at(int x, int y) {
        x--; y--;
        assert validPos(x, y) : "(x, y) must be a valid sector";

        int index = getIndexFrom(x, y);
        return quadrantString.substring(index, index + SYMBOL_SIZE);
    }

    /**
     * 
     * Checks if sector (x, y) is empty. 
     * X, and y both use base-1 positions. 
     * Checks if at(x, y) == "   ".
     * 
     * @param x
     * @param y
     * @return true if the sector is empty
     */
    public boolean empty(int x, int y) {
        return at(x, y).equals(EMPTY);
    }

    @Override
    public String toString() {
        String out = "";
        final String dashRow = "-".repeat(COLS * (SYMBOL_SIZE + 1)) + "\n";

        for (int i = 0; i < ROWS; ++i) {
            out += dashRow;

            for (int j = 0; j < COLS; ++j) {
                out += at(j + 1, i + 1) + "|";
            }

            out += "\n";
        }

        return out;
    }

    /**
     * 
     * Inserts a value at a random location amount of times.
     * 
     * @param amount
     * @param value
     */
    private void insertValues(int amount, String value) {
        assert amount <= ROWS * COLS;

        final int X = 0, Y = 1;

        for (int i = 0; i < amount; ++i) {
            int[] pos = generateRandomPosition();

            // x       y
            pos[0]++; pos[1]++;

            while (!empty(pos[0], pos[1])) {
                pos = generateRandomPosition();

                // converts to base-1
                pos[0]++; pos[1]++;
            }

            insert(pos[0], pos[1], value);
        }
    }

    /**
     * 
     * Initializes the Quadrant by placing the Enterprise at (x, y), 
     * and uses the Quadrant information to place the rest of the
     * objects. 
     * 
     * @param q
     * @param x
     * @param y
     */
    private void initializeQuadrant(Quadrant q, int x, int y) {
        quadrantString = " ".repeat(ROWS * COLS * SYMBOL_SIZE);

        insert(x, y, ENTERPRISE);
        insertValues(q.klingons(), KLINGON);
        insertValues(q.bases(), BASE);
        insertValues(q.stars(), STAR);
    }

    /**
     * 
     * Converts the 2D index (x, y) into a 1D index
     * for the quadrantString. X, and y use base-0
     * positions. This uses the formula:
     * 
     * y * AMOUNT_OF_COLUMNS (COLS) * SYMBOL_SIZE +
     * x * SYMBOL_SIZE = the start index of the column
     * 
     * Where y = amount of rows, and x = amount of columns. 
     * The calculation works because each row occupies
     * COLS * SYMBOL_SIZE characters in the backing String. 
     * Multiplying y by this value skips entire rows, 
     * while x * SYMBOL_SIZE moves to the correct sector 
     * within that row.
     * 
     * @param x
     * @param y
     * @return a 1D index for the quadrantString
     */
    private static int getIndexFrom(int x, int y) {
        assert validPos(x, y) : "(x, y) must be a valid sector";

        return y * COLS * SYMBOL_SIZE + x * SYMBOL_SIZE;
    }

    /**
     * 
     * Generates two random ints, one the x (0), and the other
     * the y value (1). Returns an array. X, and y are returned
     * as base-0 positions. Based off the COLS and ROWS. 
     * 
     * @return an array of random ints
     */
    private static int[] generateRandomPosition() {
        int[] out = new int[2];
        out[0] = GameLib.randomInt(0, COLS - 1);
        out[1] = GameLib.randomInt(0, ROWS - 1);

        return out;
    }

    /**
     * 
     * Checks whether the supplied 0-based coordinates lie within
     * the bounds of the quadrant. 
     * 
     * @param x
     * @param y
     * @return true if (x, y) is a valid sector
     */
    private static boolean validPos(int x, int y) {
        return x >= 0 && x < COLS && y >= 0 && y < ROWS;
    }

    private String quadrantString;

    private static final int ROWS         = 8;
    private static final int COLS         = 8;
    private static final int SYMBOL_SIZE  = 3;

    /**
     * Design Note:
     * The object symbols are currently represented as String constants.
     * An enum may provide better type safety and group the symbols
     * into a single abstraction.  
     */
    public static final String KLINGON     = "+K+";
    public static final String BASE        = ">!<";
    public static final String STAR        = " * ";
    public static final String ENTERPRISE  = "<*>";
    public static final String EMPTY       = "   ";

}

/**
 * Sample Output
 * 
 * QuadrantMap test
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |>!<|
 * --------------------------------
 *    |   |   |<*>|   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   | * |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * (5, 3): <     >
 * Is (5, 3) empty: true
 * (7, 8): <  *  >
 * Is (7, 8) empty: false
 * (7, 8): <     >
 * Is (7, 8) empty: true
 * QuadrantMap test success
 * 
 */
