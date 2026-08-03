package Game;

import common.GameLib;
import common.IO;
import common.GameLib.Location;

import java.util.Iterator;
import java.util.ArrayList;

import QuadrantMap.QuadrantMap;
import enterprise.Enterprise;
import galaxy.Galaxy;
import klingon.Klingon;

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

        Location oldLocation = enterprise.getLocation();
        Location newLocation = findMovementDestination(path);

        updateEnterpriseMap(oldLocation, newLocation);

        enterprise.move(newLocation, warpFactor);
        enterprise.updateDocked(canDock());

        return newLocation == path.getLast();
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
        
        initializeQuadrants();
        initializeKlingons();
        placeEnterprise();
    }

    /**
     * 
     * Initializes all Quadrants for the Game
     * 
     */
    private void initializeQuadrants() {
        for (int x = 0; x < MAP_SIZE; ++x) {
            for (int y = 0; y < MAP_SIZE; ++y) {
                map[x][y] = new QuadrantMap(galaxy.getQuadrant(x, y));
            }
        }
    }

    /**
     * 
     * Initializes all Klingons for the Game. 
     * 
     */
    private void initializeKlingons() {
        klingons = new ArrayList<>();

        for (int x = 0; x < MAP_SIZE; ++x) {
            ArrayList<ArrayList<Klingon>> rowKlingons = new ArrayList<>();
            
            for (int y = 0; y < MAP_SIZE; ++y) {
                ArrayList<Klingon> klingonsInQuadrant = new ArrayList<>();
                for (Location loc : map[x][y].klingons()) {
                    klingonsInQuadrant.add(new Klingon(loc));
                }

                rowKlingons.add(klingonsInQuadrant);
            }

            klingons.add(rowKlingons);
        }
    }

    /**
     * 
     * Places the Enterprise in its random location. 
     * 
     */
    private void placeEnterprise() {
        Location location = enterprise.getLocation();

        at(location).place(GameLib.toBase1(location.sectorX), GameLib.toBase1(location.sectorY), QuadrantMap.ENTERPRISE);
    }

    /**
     * 
     * Finds the movement destination based off the path and what it collides with. 
     * 
     * @param path
     * @return the destintion of an object based off a path
     */
    private Location findMovementDestination(ArrayList<Location> path) {
        Location destination = enterprise.getLocation();

        for (Location location : path) {
            QuadrantMap quadrant = at(location);

            if (!quadrant.empty(GameLib.toBase1(location.sectorX), GameLib.toBase1(location.sectorY)))
                break;

            destination = location;
        }

        return destination;
    }

    /**
     * 
     * Updates the Enterprise's map based off the new and old locations.
     * 
     * @param oldLocation
     * @param newLocation
     */
    private void updateEnterpriseMap(Location oldLocation, Location newLocation) {
        QuadrantMap oldQuadrant = at(oldLocation);
        QuadrantMap newQuadrant = at(newLocation);
        
        oldQuadrant.clearSector(GameLib.toBase1(oldLocation.sectorX), GameLib.toBase1(oldLocation.sectorY));
        newQuadrant.place(GameLib.toBase1(newLocation.sectorX), GameLib.toBase1(newLocation.sectorY), QuadrantMap.ENTERPRISE);

    }

    /**
     * 
     * Checks whether the Enterprise can dock or not
     * 
     * @return true if the Enterprise can dock, and false if else
     */
    private boolean canDock() {
        Location location = enterprise.getLocation();
        QuadrantMap quadrant = at(location);

        int centerX = GameLib.toBase1(location.sectorX);
        int centerY = GameLib.toBase1(location.sectorY);

        for (int y = centerY - 1; y <= centerY + 1; ++y) {
            for (int x = centerX - 1; x <= centerX + 1; ++x) {
                if (x < MIN_SECTOR || x > MAX_SECTOR || y < MIN_SECTOR || y > MAX_SECTOR)
                    continue;

                if (quadrant.at(x, y).equals(QuadrantMap.BASE))
                    return true;
            }
        }

        return false;
    }

    /**
     * 
     * Handles firing the phasers at all klingons. If a klingon is
     * destroyed, it will handle destroying the klingon as well. 
     * 
     * @param phaserEnergy
     */
    private void firePhasers(double phaserEnergy) {
        enterprise.adjustEnergy((int)-phaserEnergy);

        Location location = enterprise.getLocation();
        ArrayList<Klingon> currentKlingons = klingons.get(location.quadrantX).get(location.quadrantY);

        Iterator<Klingon> it = currentKlingons.iterator();
        while (it.hasNext()) {
            Klingon klingon = it.next();

            klingon.adjustEnergy(enterprise.firePhasers(phaserEnergy, klingon.getLocation().sectorX,
                klingon.getLocation().sectorY, currentKlingons.size()));

            if (!klingon.isDestroyed()) {
                continue;
            }

            galaxy.getQuadrant(location.quadrantX, location.quadrantY).reduceKlingons();
            at(location).removeObject(klingon.getLocation().sectorX, klingon.getLocation().sectorY, QuadrantMap.KLINGON);

            it.remove();
        }
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
            case "PHA":
                phaserCommand(command);
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
        if (command.size() < 3) {
            IO.warning("NAV usage <warp direction> <warp factor>");
            return;
        }

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

    private void phaserCommand(ArrayList<String> command) {
        if (command.size() < 2) {
            IO.warning("PHA usage <phaser energy>");
            return;
        }

        try {
            double phaserEnergy = Double.parseDouble(command.get(1));
            firePhasers(phaserEnergy);
            shortRangeCommand();
        } catch (Exception e) {
            IO.warning("Please enter valid doubles");
            return;
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
         if (command.size() < 2) {
            IO.warning("SHE usage <new shield>");
            return;
        }

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

    private ArrayList<ArrayList<ArrayList<Klingon>>> klingons; 

    private static final int MAP_SIZE = 8;
    private static final int MIN_SECTOR = 1;
    private static final int MAX_SECTOR = 8;
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