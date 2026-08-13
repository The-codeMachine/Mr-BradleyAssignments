package galaxy;

import quadrant.Quadrant;

import java.util.List;

import common.GameLib;
import common.IO;
import common.StringUtils;
import common.GameLib.Location;
import common.MathUtils;

/**
 * 
 * The Galaxy represents this game's world. It encapsulates
 * 64 quadrants in an 8 x 8 grid. Every quadrant is accessible
 * through the getQuadrant function. All functions take base-1
 * coordinates or a Location (using base-0). 
 * 
 * The Galaxy ensures there is a maximum of 2 star bases and a 
 * minimum of 1 star base. You can get the total number of 
 * klingons in the galaxy, and reduce klingons from a specific
 * quadrant. 
 * 
 * The Galaxy implements the long range scan, and scanned galaxy
 * for the Enterprise. Based off previous long range scans the
 * Enterprise can see more of the scanned galaxy. This only 
 * updates if the Enterprise scans that quadrant again (like the
 * original game).
 * 
 * Every quadrant has a galatic region name, and galatic region
 * roman numeral. These can be accessed statically (since it does
 * not change between different galaxies). You can get a quadrant's
 * full name using the getGalaticRegionName function. 
 * 
 * Using the functions mentioned above, Galaxy allows you to print
 * the galatic region name map. 
 * 
 */
public class Galaxy {
    public Galaxy() {
        totalBases = 0;
        populateGalaxy();
    }

    /**
     * Gets the quadrant located at [index][index2]. 
     * 
     * Takes base-1 coordinates.
     * 
     * @param x 
     * @param y
     * 
     * @apiNote index, and index2 must be between 1-8 (referencing an 8 by 8 grid)
     * @apiNote index represents the row
     * @apiNote index2 represents the column
     * 
     * @return a Quadrant located at [index][index2]
     */
    public Quadrant getQuadrant(int x, int y) {
        x = GameLib.toBase0(x);
        y = GameLib.toBase0(y);    
        
        assert validIndex(x) && validIndex(y) : "Index must be within given parameters (an 8, by 8 grid)";

        return map[x][y];
    } 

    /**
     * 
     * Gets the Quadrant located at (quadrantX, quadrantY). 
     * 
     * Takes base-0 coordinates through Location.
     * 
     * @param loc
     * @return a Quadrant located at (quadrantX, quadrantY)
     */
    public Quadrant getQuadrant(Location loc) {
        return getQuadrant(loc.quadrantY, loc.quadrantX);
    }

    /**
     * 
     * Returns the galatic region name of a particular quadrant. Takes base-1
     * coordinates. 
     * 
     * @param x
     * @param y
     * @return the galatic region name of quadrant (x, y)
     */
    public static String getQuadrantRegionName(int x, int y) {
        return getQuadrantRegionName(new Location(Location.INVALID, Location.INVALID, x, y));
    }

    /**
     * 
     * Returns the galatic region name of a particular quadrant. Takes base-0
     * coordinates through Location. 
     * 
     * @param location
     * @return the galatic region name of quadrant at Location
     */
    public static String getQuadrantRegionName(Location location) {
        int x = GameLib.toBase0(location.quadrantY);
        int y = GameLib.toBase0(location.quadrantX);

        if (!validIndex(x) || !validIndex(y))
            return "";

        return GALACTIC_REGION_NAMES.get(y).get(x > 4 ? 1 : 0);
    }

    /**
     * 
     * Gets the Quadrant's roman numeral for a particular region. Takes
     * base-1 coordinates. 
     * 
     * @param x
     * @param y
     * @return the Quadrant's roman numeral for a particular region. 
     */
    public static String getQuadrantRomanNumeral(int x, int y) {
        return getQuadrantRegionName(new Location(Location.INVALID, Location.INVALID, GameLib.toBase0(x), GameLib.toBase0(y)));
    }

    /**
     * 
     * Gets a quadrant's roman numeral for a paritcular region. Takes
     * base-0 coordinates through location. 
     * 
     * @param location
     * @return the Quadrant's roman numeral for a particular region
     */
    public static String getQuadrantRomanNumeral(Location location) {
        int x = GameLib.toBase0(location.quadrantY);
        int y = GameLib.toBase0(location.quadrantX);

        if (!validIndex(x) || !validIndex(y))
            return "";

        return NUMERALS.get(x % 4);
    }

    /**
     * 
     * Gets a quadrant's full galatic region name based off a location. Takes base-1 
     * coordinates.
     * 
     * @param x
     * @param y
     * @return the quadrant's full galatic region name 
     */
    public static String getGalaticRegionName(int x, int y) {
        return getGalaticRegionName(new Location(Location.INVALID, Location.INVALID, 
            x, y));
    }

    /**
     * 
     * Gets a quadrant's full galatic region name based off a location. Takes base-0
     * coordinates through location.
     * 
     * @param location
     * @return
     */
    public static String getGalaticRegionName(Location location) {
        String name = getQuadrantRegionName(location);
        if (name.isEmpty())
            return "";

        name += ' ';
        name += getQuadrantRomanNumeral(location);

        return name.isEmpty() ? "" : name;
    }

    /**
     * 
     * Prints the galatic region map. This includes only the names of the regions, and
     * not their roman numerals. 
     * 
     */
    public static void printGalaticRegionMap() {
        IO.println(StringUtils.padLeft(StringUtils.padCenter("The Galaxy", 48), 52));
        IO.println(StringUtils.padLeft("  1     2     3     4     5     6     7     8  ", 52));
        IO.println(StringUtils.padLeft("----- ----- ----- ----- ----- ----- ----- -----", 52));

        for (int y = GameLib.MIN_INDEX_0; y <= GameLib.MAX_INDEX_0; ++y) {
            IO.printf("%d   %s%s\n",
                                GameLib.toBase1(y), 
                                StringUtils.padCenter(getQuadrantRegionName(new Location(Location.INVALID, Location.INVALID, 1, GameLib.toBase1(y))), 24), 
                                StringUtils.padCenter(getQuadrantRegionName(new Location(Location.INVALID, Location.INVALID, 7, GameLib.toBase1(y))), 24)
                            );
            IO.println(StringUtils.padLeft("----- ----- ----- ----- ----- ----- ----- -----", 52));
        }
    }

    /**
     * 
     * Gets the total number of starbases in the galaxy. 
     * 
     * @return the total number of starbases in the galaxy. 
     */
    public int starBases() {
        return totalBases;
    }

    /**
     * 
     * Reduces the number of starbases by one, and removes it from
     * the quadrant. Takes base-1 coordinates. 
     * 
     * @param x
     * @param y
     */
    public void reduceStarBases(int x, int y) {
        Quadrant q = getQuadrant(x, y);
        if (q.bases() >= 1 && totalBases > 0) {
            q.removeBase();
            totalBases--;
        }
    }

    /**
     * 
     * Reduces the number of star bases by one, and removes it from
     * the quadrant. Takes base-0 coordinates through location. 
     * 
     * @param location
     */
    public void reduceStarBases(Location location) {
        reduceStarBases(location.quadrantY, location.quadrantX);
    }

    /**
     * 
     * Gets and returns the total number of Klingons in the galaxy.
     * 
     * @return the total number of Klingons in the galaxy. 
     */
    public int klingons() {
        return totalKingons;
    }

    /**
     * 
     * Reduces the amount of klingons in both the specific Quadrant and the
     * total number of klingons. Checks that there is actually a klingon in
     * that quadrant. Takes base-1 coordinates. 
     * 
     * @param x
     * @param y
     */
    public void reduceKlingons(int x, int y) {
        Quadrant q = getQuadrant(x, y);
        if (q.klingons() >= 1 && totalKingons > 0) {
            q.reduceKlingons();
            totalKingons--;
        }
    }

    /**
     * 
     * Reduces the amount of klingons in both the specific Quadrant and the
     * total number of klingons. Checks that there is actually a klingon in
     * that quadrant. Takes base-0 coordinates through Location.
     * 
     * @param location
     */
    public void reduceKlingons(Location location) {
        reduceKlingons(location.quadrantY, location.quadrantX);
    }

    /**
     * 
     * Makes a long range scan around the Enterprise (inputted as location). 
     * Updates the scanned galaxy. 
     * 
     * @param location
     */
    public void longRangeScan(Location location) {
        int startY = (int)MathUtils.clamp(GameLib.toBase0(location.quadrantX) - 1, GameLib.MIN_INDEX_0, GameLib.MAX_INDEX_0);
        int endY   = (int)MathUtils.clamp(GameLib.toBase0(location.quadrantX) + 1, GameLib.MIN_INDEX_0, GameLib.MAX_INDEX_0);
        int startX = (int)MathUtils.clamp(GameLib.toBase0(location.quadrantY) - 1, GameLib.MIN_INDEX_0, GameLib.MAX_INDEX_0);
        int endX   = (int)MathUtils.clamp(GameLib.toBase0(location.quadrantY) + 1, GameLib.MIN_INDEX_0, GameLib.MAX_INDEX_0);

        for (int y = startY; y <= endY; ++y) {
            for (int x = startX; x <= endX; ++x)
                IO.print("+-----");

            IO.println("+");

            for (int x = startX; x <= endX; ++x) {
                Quadrant q = getQuadrant(GameLib.toBase1(x), GameLib.toBase1(y));
                IO.print("| " + q.toString() + " ");
                scannedGalaxy[y][x] = q;
            }
            IO.println("|");
        }

        for (int i = startX; i <= endX; ++i) 
            IO.print("+-----");

        IO.println("+");
    }

    /**
     * 
     * Prints the entire scanned galaxy. 
     * 
     */
    public void printScannedGalaxy() {
        IO.println("\n+-----+-----+-----+-----+-----+-----+-----+-----+");

        for (int y = 0; y < GameLib.MAP_SIZE; ++y) {
            for (int x = 0; x < GameLib.MAP_SIZE; ++x) {
                Quadrant q = scannedGalaxy[y][x];
                if (q == null) {
                    IO.printf("| --- ");
                    continue;
                }

                IO.printf("| %s ", q.toString());
            }

            IO.println("|\n+-----+-----+-----+-----+-----+-----+-----+-----+");
        }
    }

    /**
     * Prints the map into the console
     */
    public void printMap() {
        System.out.println(this.toString());
    }

    /**
     * turns the galaxy into a string, similar to 
     * 
     * "
     * 004 104 014 006 008 005 002 001 
     * 002 105 002 206 007 002 102 008 
     * 008 004 008 005 103 004 005 008 
     * 001 205 104 003 003 004 018 007 
     * 001 006 003 003 108 005 001 005 
     * 008 006 106 006 002 003 002 006 
     * 005 001 001 105 001 006 008 004 
     * 004 003 204 002 108 002 205 106 
     * "
     * 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                sb.append(map[i][j]).append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * This populates the galaxy with quadrants.
     * It ensures it has at least one base, and a max of two bases
     */
    private void populateGalaxy() {
        totalBases = 0;
        totalKingons = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map[i][j] = new Quadrant();

                if (map[i][j].hasBase()) {
                    if (totalBases < 2) {
                        totalBases++;
                    } else {
                        map[i][j].removeBase();
                    }
                }

                totalKingons += map[i][j].klingons();
            }
        }

        // Ensure at least 1 base exists
        if (totalBases < 1) {
            int i = GameLib.randomInt(0, 7);
            int j = GameLib.randomInt(0, 7);

            map[i][j].putBase();
            totalBases = 1;
        }
    }

    /**
     * tests the internal private functions
     */
    static void whiteBoxTest() {
        System.out.println("White box test");

        Galaxy g = new Galaxy();

        g.printMap();

        System.out.printf("\n\n"); // padding between the maps

        System.out.println(g);

        // verifies there is the correct number of klingons, and bases
        int klingon1 = 0;
        int klingon2 = 0;
        int klingon3 = 0;
        int bases = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                Quadrant q = g.getQuadrant(i, j);
                
                if (q.hasBase()) {
                    bases++;
                }

                int klingons = q.klingons();
                if (klingons == 1) {
                    klingon1++;
                } else if (klingons == 2) {
                    klingon2++;
                } else if (klingons == 3) {
                    klingon3++;
                }
            }
        }

        double klingon1Percent = klingon1 * 100.0 / 64;
        double klingon2Percent = klingon2 * 100.0 / 64;
        double klingon3Percent = klingon3 * 100.0 / 64;
        double basePercent = bases * 100.0 / 64;

        System.out.printf("Percent of 1 Klingons: %.2f%%\n", klingon1Percent);
        System.out.printf("Percent of 2 Klingons: %.2f%%\n", klingon2Percent);
        System.out.printf("Percent of 3 Klingons: %.2f%%\n", klingon3Percent);
        System.out.printf("Percent of Bases: %.2f%%\n", basePercent);

        System.out.println("White box test success");
    }

    /**
     * 
     * Checks whether index is a valid index for the galaxy. Takes
     * base-0 coordinates. 
     * 
     * @param index
     * @return
     */
    private static boolean validIndex(int index) {
        return index >= GameLib.MIN_INDEX_0 && index <= GameLib.MAX_INDEX_0;
    }

    private Quadrant[][] map = new Quadrant[8][8];
    private Quadrant[][] scannedGalaxy = new Quadrant[8][8];

    private int totalBases;
    private int totalKingons;

    private static final List<List<String>> GALACTIC_REGION_NAMES = List.of(
        List.of("ANTARES",     "SIRIUS"),
        List.of("RIGEL",       "DENEB"),
        List.of("PROCYON",     "CAPELLA"),
        List.of("VEGA",        "BETELGEUSE"),
        List.of("CANOPUS",     "ALDEBARAN"),
        List.of("ALTAIR",      "REGULUS"),
        List.of("SAGITTARIUS", "ARCTURUS"),
        List.of("POLLUX",      "SPICA")
    );

    private static final List<String> NUMERALS = List.of(
        "I", "II", "III", "IV"
    );
}

/**
 * Sample Output
 * 
 * White box test
 * 006 105 004 202 005 107 002 008 <- maps may vary 
 * 002 007 015 105 003 108 002 007 
 * 005 106 106 107 115 106 103 008 
 * 002 006 003 008 002 003 101 005 
 * 006 001 304 008 005 008 002 001 
 * 001 002 003 005 105 005 102 108 
 * 005 107 003 006 005 101 108 002 
 * 004 005 102 007 001 002 001 301 
 * 
 * 
 * 006 105 004 202 005 107 002 008 
 * 002 007 015 105 003 108 002 007 
 * 005 106 106 107 115 106 103 008 
 * 002 006 003 008 002 003 101 005 
 * 006 001 304 008 005 008 002 001 
 * 001 002 003 005 105 005 102 108 
 * 005 107 003 006 005 101 108 002 
 * 004 005 102 007 001 002 001 301 
 * 
 * Percent of 1 Klingons: 28.00% <- may vary due to only generating 64 quadrants
 * Percent of 2 Klingons: 1.00%
 * Percent of 3 Klingons: 3.00%
 * Percent of Bases: 3.00% 
 * White box test success
 * 
 */