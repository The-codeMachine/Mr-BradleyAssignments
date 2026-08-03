package Game;

import common.GameLib;
import common.IO;
import common.GameLib.Location;

import java.util.ArrayList;

import QuadrantMap.QuadrantMap;
import enterprise.Enterprise;
import galaxy.Galaxy;

/**
 * 
 * The Game class is the main class of the
 * entire Super Star Trek recreation. It 
 * owns everything. It is responsible for 
 * the game loop, forwarding commands to the
 * Enterprise, handling game quiting, lost
 * and winning. A full list of its operations
 * include:
 *  - Construction (initializes everything)
 *  - Getting a QuadrantMap at a certain coordinate 
 *      (either by location, or global quadrant coordinates)
 *  - Running the game loop
 *  - Moving the Enterprise (and later other ships)
 * 
 */
public class Game {

    public Game() {
        constructGame();
    }

    /**
     * 
     * Gets the QuadrantMap at (x, y). This
     * method takes base-1 coordinates.
     * 
     * @param x
     * @param y
     * @return the QuadrantMap at (x, y)
     */
    public QuadrantMap at(int x, int y) {
        return map[GameLib.toBase0(x)][GameLib.toBase0(y)];
    }

    /**
     * 
     * Gets the QuadrantMap at (x, y). This
     * method takes base-0 location coordinates.
     * 
     * @param location
     * @return the QuadrantMap at (x, y)
     */
    public QuadrantMap at(Location location) {
        return map[location.quadrantX][location.quadrantY];
    }

    /**
     * 
     * Gets the current Enterprise. 
     * 
     * @return the enterprise of the game
     */
    public Enterprise getEnterprise() {
        return enterprise;
    }

    /**
     * 
     * Runs the game (takes input from the user,
     * and runs those commands in game).
     * 
     */
    public void run() {
        while (handleCommand()) {

        }
    }

    /**
     * 
     * Moves the Enterprise based off warpFactor
     * and warpDirection. Moves the Enterprise
     * withinQuadrants (moving it between or
     * within a quadrant).
     * 
     * It does this by comparing the new and old
     * coordinates. Based off this it retrieves
     * the old and new QuadrantMap, adjusting values
     * within there.
     * 
     * @param warpFactor
     * @param warpDirection
     * @return true if the move was successful
     */
    public boolean move(double warpFactor, double warpDirection) {
        enterprise.updateDocked(false);

        ArrayList<Location> path = enterprise.calculatePath(warpFactor, warpDirection);

        if (path.isEmpty())
            return false;

        Location last = enterprise.getLocation();

        for (Location location : path) {
            IO.printf("Location (row, column): %s\n", location.toString());

            if (!map[location.quadrantX][location.quadrantY]
                    .empty(GameLib.toBase1(location.sectorX), GameLib.toBase1(location.sectorY))) {
                    break;
            }

            last = location;
        }

        Location oldEnterpriseLocation = enterprise.getLocation();

        // clears old enterprise location
        at(oldEnterpriseLocation)
                .clearSector(GameLib.toBase1(oldEnterpriseLocation.sectorX), GameLib.toBase1(oldEnterpriseLocation.sectorY));

        // sets new enterprise location
        at(GameLib.toBase1(last.quadrantX), GameLib.toBase1(last.quadrantY))
                .place(GameLib.toBase1(last.sectorX), GameLib.toBase1(last.sectorY), QuadrantMap.ENTERPRISE);

        enterprise.move(last, warpFactor);

        if (canDock()) {
            enterprise.updateDocked(true);
        }

        return last == path.getLast();
    }

    /**
     * 
     * Constructs the game  
     * 
     */
    private void constructGame() {
        enterprise = new Enterprise(3000, 0, 10, false);

        galaxy = new Galaxy();
        map = new QuadrantMap[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                map[i][j] = new QuadrantMap(galaxy.getQuadrant(i, j));
            }
        }

        Location enterpriseLocation = enterprise.getLocation();
        map[enterpriseLocation.quadrantX][enterpriseLocation.quadrantY]
            .place(GameLib.toBase1(enterpriseLocation.sectorX), GameLib.toBase1(enterpriseLocation.sectorY), QuadrantMap.ENTERPRISE);
    }

    /**
     * 
     * Checks whether the Enterprise can dock or not
     * 
     * @return true if the Enterprise can dock, and false if else
     */
    private boolean canDock() {
        Location loc = enterprise.getLocation();

        // done like this for base0 to base1 conversion
        for (int i = GameLib.toBase1(loc.sectorY - 1); i < GameLib.toBase1(loc.sectorY + 2); ++i) {
            for (int j = GameLib.toBase1(loc.sectorX - 1); j < GameLib.toBase1(loc.sectorX + 2); ++j) {
                if (i < 1 || i > 8 || j < 1 || j > 8)
                    continue;

                if (at(loc).at(j, i).equals(QuadrantMap.BASE)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 
     * Handles a command from the user. Takes the input
     * and handles the command (gets the correct data, 
     * and calls the functions). Returns a boolean 
     * representing whether the game should continue or
     * not (true if the game should continue).
     * 
     * @param input
     * @return whether the game should continue
     */
    private boolean handleCommand() {
        // continue since readCommand reports an error    
        ArrayList<String> command = IO.readCommand();
        if (command.isEmpty())
            return true;

        String cmd = command.get(0);

        switch (cmd) {
            case "NAV":
                moveCommand(command);
                break;
            case "SRS":
                shortRangeCommand();
                break;
            case "LRS":
                longRangeCommand();
                break;
            case "SHE":
                shieldCommand(command);
                break;
            case "DAM":
                damageReportCommand();
                break;
            case "XXX":
                return false;
        }

        return !enterprise.isDestroyed();
    }

    /**
     * 
     * Handles the move command (gets the correct data,
     * then calls the move function). 
     * 
     */
    private void moveCommand(ArrayList<String> command) {
        try {
            double warpDirection = Double.parseDouble(command.get(1));
            double warpFactor = Double.parseDouble(command.get(2));

            move(warpFactor, warpDirection);
            shortRangeCommand();
        } catch (Exception e) {
            IO.warning("Invalid usage of NAV");
            IO.println("NAV Usage: NAV <warp factor> <warp direction>");
        }
    }

    /**
     * 
     * The Enterprise does a short range scan.
     * 
     */
    private void shortRangeCommand() {
        Location enterpriseLocation = enterprise.getLocation();
        IO.println(map[enterpriseLocation.quadrantX][enterpriseLocation.quadrantY].toString());
        
        IO.println(enterprise.toString());
    }

    /**
     * 
     * Does a long range scan around the Enterprise. Returns
     * the quadrant's KBS value around the Enterprise. 
     * 
     */
    private void longRangeCommand() {
        Location loc = enterprise.getLocation();
        
        int startY = Math.max(0, loc.quadrantY - 1);
        int endY = Math.min(7, loc.quadrantY + 1);

        int startX = Math.max(0, loc.quadrantX - 1);
        int endX = Math.min(7, loc.quadrantX + 1);

        for (int y = startY; y <= endY; ++y) {
            for (int x = startX; x <= endX; ++x) {
                IO.print(galaxy.getQuadrant(x, y).toString() + " ");
            }

            IO.println("");
        }
    }

    /**
     * 
     * Adjusts shields acording to the user. Will replenish the available
     * energy from the shields. 
     * 
     * @param command
     */
    private void shieldCommand(ArrayList<String> command) {
        try {
            double newShields = Double.parseDouble(command.get(1));

            enterprise.adjustShields(newShields);
        } catch (Exception e) {
            IO.warning("Invalid usage of SHE");
            IO.println("SHE Usage: SHE <new shields>");
        }
    }

    /**
     * 
     * Calls the damage report command. 
     * 
     */
    private void damageReportCommand() {
        enterprise.damageReport();
    }

    private Enterprise enterprise;
    private Galaxy galaxy;

    private QuadrantMap[][] map;
}

/**
 * Sample Output 
 * 
 * Game test
 * Initial Quadrant
 * --------------------------------
 * <*>|   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   | * |   |
 * --------------------------------
 *    |   |   |   |   |   |   | * |
 * --------------------------------
 *  * |   |   |   |   |   | * |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * 
 * ...
 * 
 * Warp Clamp
 * Warp 15.00  Direction 2.0
 * (5, 2) in (3, 0)
 * (6, 1) in (3, 0)
 * (7, 0) in (3, 0)
 * Move Successful: true
 * Current Quadrant:
 * --------------------------------
 *    |   |   |   |   |   |   |<*>|
 * --------------------------------
 *    |   |   |   |   |   |   |   |
 * --------------------------------
 *    |   |   |   |   |   | * |   |
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
 * 
 * 
 * Game test success
 * 
 */