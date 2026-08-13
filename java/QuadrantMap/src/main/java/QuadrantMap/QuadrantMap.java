package QuadrantMap;

import java.util.ArrayList;

import common.GameLib;
import common.GameLib.Location;
import common.IO;
import quadrant.*;
import klingon.*;

/**
 * 
 * QuadrantMap encapsulates all the positional values of objects
 * within a Quadrant. This includes:
 *  - Klingons
 *  - Bases
 *  - Stars
 *  - The Enterprise
 * 
 * A QuadrantMap can be constructed from a quadrant and/or the Enterprise's
 * initial position. 
 * 
 * A QuadrantMap owns the Klingons within its quadrant as well. Not just
 * their positional value, but the actual Klingon object. These can be
 * access through the getKlingons function. 
 * 
 * QuadrantMap can also check whether the Enterprise can dock or not. This
 * checks if the Enterprise is beside a base within the Quadrant. 
 * 
 * All functions which include:
 *  - Placing a new value
 *  - Clearing a sector
 *  - Moving an object from (x, y) to (newX, newY)
 *  - Removing an object from (x, y)
 *  - Getting the string representation of an object at (x, y)
 *  - Checking whether (x, y) is empty
 * 
 * take either base-1 coordinates or base-0 through Location. We recommend 
 * using the Location functions, but both are possible. QuadrantMap
 * takes (column, row) notation. 
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
        klingons = new ArrayList<>();
        enterprise = new Location(Location.INVALID, Location.INVALID, Location.INVALID, Location.INVALID);
        initializeQuadrant(q, x, y);
    }

    /**
     * 
     * Constructs a QuadrantMap from quadrant Q.
     * 
     * @param q
     */
    public QuadrantMap(Quadrant q) {
        klingons = new ArrayList<>();
        enterprise = new Location(Location.INVALID, Location.INVALID, Location.INVALID, Location.INVALID);
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

        if (value.equals(ENTERPRISE)) {
            enterprise.sectorY = x;
            enterprise.sectorX = y;
        }
    }

    /**
     * 
     * Writes a fixed-width symbol into the specified sector.
     * Uses base-0 coordinates through Location.
     * 
     * @param loc
     * @param value
     */
    public void place(Location loc, String value) {
        place(loc.sectorY, loc.sectorX, value);
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
     * Removes whatever occupies the specific sector.
     * Clearing is implemented by replacing the sectory with
     * the empty-space symbol.
     * 
     * Uses base-0 coordinates through Location.
     * 
     * @param loc
     */
    public void clearSector(Location loc) {
        clearSector(loc.sectorY, loc.sectorX);
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
    public void move(int x, int y, int newX, int newY, String value) {
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
        }
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
     * Uses base-0 coordinates through Location. 
     * 
     * @param oldLocation
     * @param newLocation
     * @param value
     */
    public void move(Location oldLocation, Location newLocation, String value) {
        move(oldLocation.sectorY, oldLocation.sectorX,
            newLocation.sectorY, newLocation.sectorX, value);
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
     * Clears a sector only if it has value as
     * its object. If it does then it is cleared.
     * 
     * Uses base-0 coordinates through Location.
     * 
     * @param loc
     * @param object
     */
    public void removeObject(Location loc, String object) {
        removeObject(loc.sectorY, loc.sectorX, object);
    }

    /**
     * 
     * Gets the locations of all the klingons within this Quadrant
     * 
     * @return the locations of all the klingons within this Quadrant
     */
    public ArrayList<Klingon> klingons() {
        return klingons;
    }

    /**
     * 
     * Returns the amount of damage the klingons used to damage the Enterprise. Calculates
     * the damage based off distance, and reduces the Klingon's energy reserves. 
     * 
     * @return
     */
    public int klingonsFire() {
        if (klingons.size() <= 0 || canDock())
            return 0;
        
        int out = 0;

        for (Klingon klingon : klingons) {
            int damage = klingon.firePhasers(enterprise.sectorY, enterprise.sectorX);

            IO.printf("Klingon %s has fired their phasers dealing: %d damage\n",
                    klingon.getLocation().sectorString(),
                    damage
            );

            out += damage;
        }

        return out;
    }

    /**
     * 
     * Moves the klingons in the Quadrant to a random sector. Checks that it is a valid
     * sector and that the klingon can move there. 
     * 
     */
    public void klingonsMove() {
        for (Klingon klingon : klingons) {
            Location location = klingon.calculateDestination();
            while (!empty(location)) {
                location = klingon.calculateDestination();
            }

            move(klingon.getLocation(), location, KLINGON);
            klingon.move(location);
        }
    }

    /**
     * 
     * Gets the location of the starbase within the QuadrantMap.
     * If there is no starbase it will return {-1, -1, -1, -1}.
     * 
     * @return the location of the starbase within the QuadrantMap
     */
    public Location base() {
        return baseLocation;
    }

    /**
     * 
     * Checks whether the Enterprise can dock or not based off its current position.
     * Returns true if the Enterprise can dock, and false elsewise. 
     * 
     * @return true if the Enterprise can dock and false if it cannot
     */
    public boolean canDock() {
        int centerX = enterprise.sectorY;
        int centerY = enterprise.sectorX;

        for (int y = centerY - 1; y <= centerY + 1; ++y) {
            for (int x = centerX - 1; x <= centerX + 1; ++x) {
                if (x < GameLib.MIN_INDEX_1 || x > GameLib.MAX_INDEX_1 || 
                    y < GameLib.MIN_INDEX_1 || y > GameLib.MAX_INDEX_1)
                    continue;

                if (at(x, y).equals(BASE))
                    return true;
            }
        }

        return false;
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
     * 
     * Returns the symbol stored at the specified sector.
     * The 2D coordinates are converted into a 1D index into
     * the backing String, and the fixed-width symbol stored
     * at that location is returned.
     * 
     * Uses base-0 coordinates through Location.
     * 
     * @param loc
     * @return the symbol as a string from (sectorX, sectorY)
     */
    public String at(Location loc) {
        return at(loc.sectorY, loc.sectorX);
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

    /**
     * 
     * Checks if sector (x, y) is empty.
     * X, and y both use base-1 positions.
     * Checks if at(x, y) == " ".
     * 
     * Uses base-0 coordinates through Location
     * 
     * @param loc
     * @return
     */
    public boolean empty(Location loc) {
        return empty(loc.sectorY, loc.sectorX);
    }

    @Override
    public String toString() {
        String out = "";
        final String dashRow = "---+".repeat(GameLib.COLS) + "\n";

        for (int i = 1; i <= GameLib.ROWS; ++i) {
            out += dashRow;

            for (int j = 1; j <= GameLib.COLS; ++j) {
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
        assert amount <= GameLib.ROWS * GameLib.COLS;

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
     * Places all the Klingons within the QuadrantMap and record
     * where they are located. 
     * 
     * @param amount
     */
    private void placeKlingons(int amount) {
        assert amount <= GameLib.ROWS * GameLib.COLS;

        for (int i = 0; i < amount; ++i) {
            int[] pos = generateRandomPosition();

            while (!empty(pos[X], pos[Y])) {
                pos = generateRandomPosition();
            }

            place(pos[X], pos[Y], KLINGON);
            klingons.add(new Klingon(new Location(GameLib.toBase0(pos[X]), GameLib.toBase0(pos[Y]), Location.INVALID, Location.INVALID)));
        }
    }

    private void placeBases(int amount) {
        baseLocation = new Location(Location.INVALID, Location.INVALID, Location.INVALID, Location.INVALID);

        assert(amount <= GameLib.ROWS * GameLib.COLS);

        for (int i = 0; i < amount; ++i) {
            int[] pos = generateRandomPosition();

            while (!empty(pos[X], pos[Y])) {
                pos = generateRandomPosition();
            }
            
            place(pos[X], pos[Y], BASE);
            baseLocation = new Location(GameLib.toBase0(pos[X]), GameLib.toBase0(pos[Y]), Location.INVALID, Location.INVALID);
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
        placeKlingons(q.klingons());
        placeBases(q.bases());
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

        placeKlingons(q.klingons());
        placeBases(q.bases());
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
        return GameLib.toBase0(y) * GameLib.COLS + GameLib.toBase0(x);
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
        out[X] = GameLib.randomInt(1, GameLib.COLS);
        out[Y] = GameLib.randomInt(1, GameLib.ROWS);

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
        return x > 0 && x <= GameLib.COLS && y > 0 && y <= GameLib.ROWS;
    }

    private QuadrantString quadrantString;
    private ArrayList<Klingon> klingons;
    private Location baseLocation;
    private Location enterprise;

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