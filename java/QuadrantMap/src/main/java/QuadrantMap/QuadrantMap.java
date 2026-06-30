package QuadrantMap;

import common.GameLib;
import quadrant.*;

/**
 * QuadrantMap handles all of the movement and positional
 * status for Klingons, bases, stars, and the Enterprise
 * within a Quadrant. It allows you to remove a klingon,
 * and move the Enterprise. Operations include:
 *  - Construction (raw kbs, klingons bases stars, or a Quadrant)
 *  - Move the Enterprise
 *  - Remove a klingon
 *  - Check what the value of a sector is
 *  - Check if a sector is empty
 *  - Get the number of klingons/bases/stars in the Quadrant
 *  - Convert the map to a string
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
 */
public class QuadrantMap {
    /**
     * 
     * Constructs a QuadrantMap from quadrant Q,
     * setting Enterprise's coordinates to (x, y).
     * 
     * @param q
     * @param x
     * @param y
     */
    public QuadrantMap(Quadrant q, int x, int y) {
        quadrantString = " ".repeat(ROWS * COLS * SYMBOL_SIZE);
        quadrant = q;

        insert(x - 1, y - 1, ENTERPRISE);
        insertValues(quadrant.klingons(), KLINGON);
        insertValues(quadrant.bases(), BASE);
        insertValues(quadrant.stars(), STAR);
    }

    /**
     * 
     * Moves the Enterprise to a new sector.
     *
     * The move succeeds only if the destination sector is empty.
     * Internally, the destination is updated before the previous
     * sector is cleared so that the map always contains exactly
     * one Enterprise. 
     * 
     * @param x
     * @param y
     * @param newX
     * @param newY
     */
    public void moveEnterprise(int x, int y, int newX, int newY) {
        move(x - 1, y - 1, newX - 1, newY - 1, ENTERPRISE);
    }

    /**
     * 
     * Removes a klingon from (x, y) and from the Quadrant.
     * X, and y both use base-1 positions. Removes a klingon
     * by checking if a klingon is there, and then clears it. 
     * 
     * @param x
     * @param y
     */
    public void removeKlingon(int x, int y) {
        if (klingons() <= 0)
            return;

        if (at(x, y).equals("+K+")) {
            clear(x - 1, y - 1);
            quadrant.reduceKlingons();
        }
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
        assert validPos(x - 1, y - 1) : "(x, y) must be a valid sector";

        int index = getIndexFrom(x - 1, y - 1);
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

    /**
     * 
     * Gets the number of klingons in the quadrant. 
     * 
     * @return the number of klingons in the quadrant
     */
    public int klingons() {
        return quadrant.klingons();
    }

    /**
     * 
     * Gets the number of bases in the quadrant. 
     * 
     * @return the number of bases in the quadrant
     */
    public int bases() {
        return quadrant.bases();
    }

    /**
     * 
     * Gets the number of stars in the quadrant. 
     * 
     * @return the number of stars in the quadrant
     */
    public int stars() {
        return quadrant.stars();
    }

    @Override
    public String toString() {
        String out = "";
        String dashRow = "-".repeat(COLS * (SYMBOL_SIZE + 1)) + "\n";

        for (int i = 0; i < ROWS; ++i) {
            out += dashRow;

            for (int j = 0; j < COLS; ++j) {
                out += at(j + 1, i + 1) + "|";
            }

            out += "\n";
        }

        out += "Klingons: " + Integer.toString(klingons()) +
                ", Bases: " + Integer.toString(bases()) +
                ", Stars: " + Integer.toString(stars());

        return out;
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
    private void clear(int x, int y) {
        if (empty(x + 1, y + 1))
            return;

        insert(x, y, EMPTY);
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
    private void insert(int x, int y, String value) {
        assert validPos(x, y) : "X and Y must be valid positions";
        assert value.length() == SYMBOL_SIZE : "Value must be exactly the same as SYMBOL_SIZE";

        int index = getIndexFrom(x, y);
        quadrantString = quadrantString.substring(0, index)
        + value.substring(0, SYMBOL_SIZE)
        + quadrantString.substring(index + SYMBOL_SIZE);
    }

    /**
     * 
     * Inserts a value at a random location amount of times.
     * 
     * @param amount
     * @param value
     */
    private void insertValues(int amount, String value) {
        for (int i = 0; i < amount; ++i) {
            int[] pos = generateRandomPosition();

            while (!empty(pos[0] + 1, pos[1] + 1)) {
                pos = generateRandomPosition();
            }

            insert(pos[0], pos[1], value);
        }
    }

    /**
     * 
     * Moves a value from (x, y) to (newX, newY). It does
     * this by checking it (x, y) is actually the value, and
     * then clearing it, and inserting it in (newX, newY) after 
     * verifying that (newX, newY) is empty. Can be used to move
     * Enterprise or Klingons. 
     * 
     * @param x
     * @param y
     * @param newX
     * @param newY
     * @param value
     */
    private void move(int x, int y, int newX, int newY, String value) {
        assert validPos(x, y) : "(x, y) must be a valid sector";
        assert validPos(newX, newY) : "(newX, newY) must be a valid sector";

        assert at(x + 1, y + 1).equals(value);

        if (empty(newX + 1, newY + 1)) {
            clear(x, y);
            insert(newX, newY, value);
        }
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
    private Quadrant quadrant;

    private static final int ROWS         = 8;
    private static final int COLS         = 8;
    private static final int SYMBOL_SIZE  = 3;

    private static final String KLINGON     = "+K+";
    private static final String BASE        = ">!<";
    private static final String STAR        = " * ";
    private static final String ENTERPRISE  = "<*>";
    private static final String EMPTY       = "   ";

}

/**
 * Sample Output
 * 
 * QuadrantMap test
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   | * |   |   |   |
 * --------------------------------
 *    |   |   |   |<*>|   |   | * |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   | * |
 * --------------------------------
 *    |   |   |   |   |   |   | * |
 * --------------------------------
 *  * |   |   |   |   |   |   |   |
 * Klingons: 0, Bases: 0, Stars: 5
 * (5, 3): <  *  >
 * Is (5, 3) empty: false
 * Klingons: 0
 * Bases: 0
 * Stars: 5
 * QuadrantMap test success
 * 
 */
