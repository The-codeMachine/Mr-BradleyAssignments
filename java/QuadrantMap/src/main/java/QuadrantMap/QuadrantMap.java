package QuadrantMap;

import common.GameLib;
import quadrant.*;

/**
 * 
 * TODO:
 * Resonably large issue. Not sure how to handle it currently,
 * but essentially, the QuadrantMap is upside-down. As y goes
 * up it goes down. This is backwards to what is normal. This
 * issue is currently fixed by adjusting the delta-y to be 
 * negative, this makes North = up, but we might want to flip
 * the QuadrantMap, maybe just in the printing section. Any 
 * ideas? 
 * 
 */

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
 *  - Construction (through Quadrant, with or without an Enterprise)
 *  - Insert an object
 *  - Clear a sector
 *  - Move an object
 *  - Remove an object
 *  - Check what object is at a certain sector
 *  - Check if a sector is empty
 *  - Convert map to string
 *
 * Currently, there are 8 rows and 8 columns, with each
 * symbol being 3 characters long.
 * 
 * All methods use 1-based coordinates because they represent
 * the quadrant from the player's perspective. Players naturally
 * think of the first sector as (1,1), rather than (0,0).
 *
 * Conversion between the two coordinate systems occurs only at the
 * getIndexFrom. 
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
     * Constructs a QuadrantMap from quadrant Q.
     * 
     * @param q
     */
    public QuadrantMap(Quadrant q) {
        initializeQuadrant(q);
    }

    /**
     * 
     * Writes a fixed-width symbol into the specified sector.
     * Uses 1-Based coordinates
     * 
     * @param x
     * @param y
     * @param value
     */
    public void place(int x, int y, String value) {
        assert validPos(x, y) : "X and Y must be valid positions";

        // Checks are done in the .place function
        int index = getIndexFrom(x, y);
        quadrantString.place(index, value);
    }

    /**
     * 
     * Removes whatever occupies the specified sector.
     * Clearing is implemented by replacing the sector with
     * the empty-space symbol.
     * 
     * Uses base-1 coordinates. 
     * 
     * @param x
     * @param y
     * 
     */
    public void clearSector(int x, int y) {
        // Checks like validPos are done within place
        place(x, y, EMPTY);
    }

    /**
     * 
     * Moves a value from (x, y) to (newX, newY). It does
     * this by checking if (x, y) is actually the value, and
     * then clearing it, and inserting it into (newX, newY) after
     * verifying that (newX, newY) is empty. Can be used to move
     * Enterprise or Klingons. Checks that the path between
     * the two coordinates is clear. 
     * 
     * Uses base-1 coordinates. 
     *
     * @param x
     * @param y
     * @param newX
     * @param newY
     * @param value
     */
    public boolean move(int x, int y, int newX, int newY, String value) {
        // you assert with expressions
        assert validPos(x, y) : "(x, y) sector must be valid";
        assert validPos(newX, newY) : "(newX, newY) sector must be valid";

        // then without wrong unadjusted x, y values
        assert at(x, y).equals(value) : "Original sector (x, y) must be == value";

        // then do the asserts again - in these calls ( just pointing it out - its
        // something we will address later )
        if (empty(newX, newY)) {
            clearSector(x, y);
            place(newX, newY, value);
            
            return true;
        }

        return false;
    }

    /**
     * 
     * Clears a sector only if it has value as
     * its object. If it does then it is cleared.
     * 
     * Uses base-1 coordinates.
     * 
     * @param x
     * @param y
     * @param object
     */
    public void removeObject(int x, int y, String object) {
        assert validPos(x, y) : "Sector (x, y) must be valid";
        assert at(x, y).equals(object) : "Sector (x, y) must be the object";

        clearSector(x, y);
    }

    /**
     * 
     * Returns the symbol stored at the specified sector.
     * The 2D coordinates are converted into a 1D index into
     * the backing String, and the fixed-width symbol stored
     * at that location is returned.
     * 
     * Uses base-1 coordinates. 
     * 
     * @param x
     * @param y
     * @return the symbol as a string from (x, y)
     */
    public String at(int x, int y) {
        assert validPos(x, y) : "(x, y) must be a valid sector";

        // getIndexFrom converts from base-1
        int index = getIndexFrom(x, y);
        return quadrantString.at(index);
    }

    /**
     * 
     * Checks if sector (x, y) is empty.
     * X, and y both use base-1 positions.
     * Checks if at(x, y) == " ".
     * 
     * Uses base-1 coordinates
     * 
     * @param x
     * @param y
     * @return true if the sector is empty
     */
    public boolean empty(int x, int y) {
        // getIndexFrom converts (x, y) to a 0-based index for quadrantString
        int index = getIndexFrom(x, y);
        return quadrantString.isEmpty(index);
    }

    @Override
    public String toString() {
        String out = "";
        final String dashRow = "-".repeat(COLS * (SYMBOL_SIZE + 1)) + "\n";

        for (int i = 1; i <= ROWS; ++i) {
            out += dashRow;

            for (int j = 1; j <= COLS; ++j) {
                out += at(j, i) + "|";
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
    private void placeValues(int amount, String value) {
        assert amount <= ROWS * COLS;

        for (int i = 0; i < amount; ++i) {
            int[] pos = generateRandomPosition();

            while (!empty(pos[X], pos[Y])) {
                pos = generateRandomPosition();
            }

            place(pos[X], pos[Y], value);
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
        quadrantString = new QuadrantString();

        place(x, y, ENTERPRISE);
        placeValues(q.klingons(), KLINGON);
        placeValues(q.bases(), BASE);
        placeValues(q.stars(), STAR);
    }

    /**
     * 
     * Initializes the Quadrant without placing the Enterprise at (x, y),
     * and uses the Quadrant information to place the rest of the
     * objects.
     * 
     * @param q
     */
    private void initializeQuadrant(Quadrant q) {
        quadrantString = new QuadrantString();

        placeValues(q.klingons(), KLINGON);
        placeValues(q.bases(), BASE);
        placeValues(q.stars(), STAR);
    }

    /**
     * 
     * Converts the 2D index (x, y) into a 1D index
     * for the quadrantString. X, and y use base-1
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

        // converts to base-0 because QuadrantString uses base-0
        return GameLib.toBase0(y) * COLS + GameLib.toBase0(x);
    }

    /**
     * 
     * Generates two random ints, one the x (0), and the other
     * the y value (1). Returns an array. X, and y are returned
     * as base-1 positions. Based off the COLS and ROWS.
     * 
     * @return an array of random ints
     */
    private static int[] generateRandomPosition() {
        int[] out = new int[2];
        out[X] = GameLib.randomInt(1, COLS);
        out[Y] = GameLib.randomInt(1, ROWS);

        return out;
    }

    /**
     * 
     * Checks whether the supplied 1-based coordinates lie within
     * the bounds of the quadrant.
     * 
     * @param x
     * @param y
     * @return true if (x, y) is a valid sector
     */
    private static boolean validPos(int x, int y) {
        return x > 0 && x <= COLS && y > 0 && y <= ROWS;
    }

    private QuadrantString quadrantString;

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int SYMBOL_SIZE = 3;

    private static final int X = 0, Y = 1; // array point index names

    /**
     * Design Note:
     * The object symbols are currently represented as String constants.
     * An enum may provide better type safety and group the symbols
     * into a single abstraction.
     */
    public static final String KLINGON = "+K+";
    public static final String BASE = ">!<";
    public static final String STAR = " * ";
    public static final String ENTERPRISE = "<*>";
    public static final String EMPTY = "   ";

}

/**
 * Sample Output
 * 
 * QuadrantMap test
 * Klingons: 0, Bases: 0, Stars: 8
 * Enterprise location: (7, 1)
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   | * |   |   |   |   |
 * --------------------------------
 *  * |   |   |   |   |   |   |   |
 * --------------------------------
 *  * |   |   |   | * |   |   |   |
 * --------------------------------
 *    |   | * |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |<*>|   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * (5, 3): < * >
 * Is (5, 3) empty: false
 * (7, 8): < * >
 * Is (7, 8) empty: false
 * (7, 8): <   >
 * Is (7, 8) empty: true
 * QuadrantMap test success
 * 
 */