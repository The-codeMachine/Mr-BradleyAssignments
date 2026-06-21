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
 */
public class QuadrantMap {
    public QuadrantMap(Quadrant q) {
        quadrantString = " ".repeat(ROWS * COLS * SYMBOL_SIZE);
        quadrant = q;

        insertValues(quadrant.klingons(), "+K+");
        insertValues(quadrant.bases(), ">!<");
        insertValues(quadrant.stars(), " * ");
    }

    /**
     * 
     * Moves the enterprise from (x, y) to (newX, newY). 
     * 
     * @param x
     * @param y
     * @param newX
     * @param newY
     */
    public void moveEnterprise(int x, int y, int newX, int newY) {
        assert at(x, y) == "<*>";

        if (empty(newX, newY)) {
            insert(newX, newY, "<*>");
            clear(x, y);
        }
    }

    /**
     * 
     * Removes a klingon from (x, y) and from the Quadrant
     * 
     * @param x
     * @param y
     */
    public void removeKlingon(int x, int y) {
        if (klingons() <= 0)
            return;
        
        if (at(x, y).equals("+K+")) {
            clear(x, y);
            quadrant.reduceKlingons();
        }
    }

    /**
     * 
     * Gets the symbol at (x, y).
     * 
     * @param x
     * @param y
     * @return the symbol as a string from (x, y)
     */
    public String at(int x, int y) {
        assert validPos(x, y) : "(x, y) must be a valid sector";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SYMBOL_SIZE; ++i) {
            sb.append(quadrantString.charAt(x * SYMBOL_SIZE + y * ROWS * SYMBOL_SIZE + i));
        }

        return sb.toString();
    }

    /**
     * 
     * Checks if sector (x, y) is empty
     * 
     * @param x
     * @param y
     * @return true if the sector is empty
     */
    public boolean empty(int x, int y) {
        assert validPos(x, y) : "(x, y) must be a valid sector";

        return at(x, y).equals("   ");
    }

    /**
     * 
     * Gets the number of klingons in the quadrant
     * 
     * @return the number of klingons in the quadrant
     */
    public int klingons() {
        return quadrant.klingons();
    }
    
    /**
     * 
     * Gets the number of bases in the quadrant
     * 
     * @return the number of bases in the quadrant
     */
    public int bases() {
        return quadrant.bases();
    }

    /**
     * 
     * Gets the number of stars in the quadrant
     * 
     * @return the number of stars in the quadrant
     */
    public int stars() {
        return quadrant.stars();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS * (SYMBOL_SIZE + 1); ++j) {
                out.append('-');
            }
            out.append("\n");

            for (int j = 0; j < COLS; ++j) {
                out.append(at(j, i) + "|");
            }

            out.append("\n");
        }

        out.append("Klingons: " + Integer.toString(klingons()) +
        ", Bases: " + Integer.toString(bases()) +
        ", Stars: " + Integer.toString(stars()));

        return out.toString();
    }

    /**
     * 
     * Clears (x, y) from the Quadrant string
     * 
     * @param x
     * @param y
     * 
     */
    private void clear(int x, int y) {
        if (empty(x, y))
            return;

        insert(x, y, "   ");
    }

    /**
     * 
     * Inserts a value at (x, y). 
     * 
     * @param x
     * @param y
     * @param value
     */
    private void insert(int x, int y, String value) {
        assert validPos(x, y) : "X and Y must be valid positions";

        if (value.length() < SYMBOL_SIZE)
            return;

        StringBuilder sb = new StringBuilder(quadrantString);
        if (empty(x, y)) {
            for (int i = 0; i < SYMBOL_SIZE; ++i) {
                sb.setCharAt(x * SYMBOL_SIZE + y * ROWS * SYMBOL_SIZE + i, value.charAt(i));
            }
        }

        quadrantString = sb.toString();
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
            int x = generateRandomPosition();
            int y = generateRandomPosition();

            while (!empty(x, y)) {
                x = generateRandomPosition();
                y = generateRandomPosition();
            }

            insert(x, y, value);
        }
    }

    /**
     * 
     * Makes (x, y) a random number between 0, and ROWS / COLS
     * 
     */
    private static int generateRandomPosition() {
        return GameLib.randomInt(0, ROWS - 1);
    }

    /**
     * 
     * Checks if (x, y) is a valid sector in QuadrantMap.
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

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int SYMBOL_SIZE = 3;

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
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * Klingons: 0, Bases: 0, Stars: 1
 * (4, 2): <  *  >
 * Is (4, 2) empty: false
 * Klingons: 0
 * Bases: 0
 * Stars: 1
 * QuadrantMap test success
 * 
 */