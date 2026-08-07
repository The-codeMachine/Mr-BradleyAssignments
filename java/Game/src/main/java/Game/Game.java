package Game;

import common.GameLib;
import common.IO;
import common.GameLib.Location;
import device.Devices;

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
        while ( handleCommand() && 
            numKlingons > 0 && 
        currentStardate <= missionDuration + startingStardate) {}

        // success
        if (numKlingons <= 0) {
            IO.println("Congratulations, captain! The last klingon battle cruiser");
            IO.println("menacing the Federation has been destroyed.");
            IO.printf("Your efficiency rating is: %.6f", 1000 * Math.pow(currentStardate - startingStardate, 2));
            return;
        }

        // lost because of time
        if (currentStardate > missionDuration + startingStardate) {
            IO.printf("It is stardate: %.6f\n", currentStardate);
            IO.println("You have failed to destroy all the Klingon warships");
            IO.println("before they could launch their attack against the");
            IO.println("Federation. They will destroy the Enterprise as well");
            IO.println("as the Federation");
            return;
        }
        
        // they destroyed the Enterprise elsewise or the user quitted
        IO.printf("It is stardate: %.6f\n", currentStardate);
        IO.println("You have failed to destroy all the Klingon warships");
        IO.println("The Enterprise has been destroyed and now there is ");
        IO.println("nothing stopping the Klingons from destroying the");
        IO.println("Federation. ");
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

        ArrayList<Location> path =
            enterprise.calculatePath(warpFactor, warpDirection);

        if (path.isEmpty())
            return false;

        Location oldLocation = enterprise.getLocation();

        MovementResult result = findMovementDestination(path);

        Location newLocation = result.destination();
        double starDateChange = result.starDateChange();

        // If we did not cross a quadrant boundary, normal movement
        // time applies.
        if (starDateChange == 0.0 && !oldLocation.equals(newLocation)) {
            starDateChange = warpFactor < 1.0
                ? 0.1 * Math.floor(10.0 * warpFactor)
                : 1.0;
        }

        currentStardate += starDateChange;

        updateEnterpriseMap(oldLocation, newLocation);

        enterprise.move(newLocation, warpFactor);
        enterprise.updateDocked(canDock());

        return newLocation.equals(path.getLast());
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
        initializeTime();
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
                map[x][y] = new QuadrantMap(galaxy.getQuadrant(GameLib.toBase1(x), GameLib.toBase1(y)));
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
        numKlingons = 0;

        for (int x = 0; x < MAP_SIZE; ++x) {
            ArrayList<ArrayList<Klingon>> rowKlingons = new ArrayList<>();
            
            for (int y = 0; y < MAP_SIZE; ++y) {
                ArrayList<Klingon> klingonsInQuadrant = new ArrayList<>();
                for (Location loc : map[x][y].klingons()) {
                    klingonsInQuadrant.add(new Klingon(new Location(loc.sectorX, loc.sectorY, x, y)));
                    numKlingons++;
                }

                rowKlingons.add(klingonsInQuadrant);
            }

            klingons.add(rowKlingons);
        }

            IO.printf("Your orders are as follows: \n");
            IO.printf("destroy the %d Klingon warships which have invaded\n", numKlingons);
            IO.printf("the galaxy before they can attack Federation headquarters\n");
    }

    /**
     * 
     * Initilizes the time, including the starting
     * star date, and the ending star date
     * 
     */
    private void initializeTime() {
        currentStardate = (int)(GameLib.random() * 20 + 20) * 100;
        startingStardate = (int)currentStardate;
        missionDuration = 25 + (int)(GameLib.random() * 10);
        
        IO.printf("on stardate: %d, this gives you %d days\n", startingStardate + missionDuration, missionDuration);
    }

    /**
     * 
     * Places the Enterprise in its random location. 
     * 
     */
    private void placeEnterprise() {
        Location location = enterprise.getLocation();

        at(location).place(location, QuadrantMap.ENTERPRISE);
    }

    /**
     * Finds the movement destination based off the path and what it collides with.
     * Also calculates the Stardate cost of the movement.
     *
     * A successfully crossed quadrant boundary costs 1 Stardate.
     *
     * @param path the calculated movement path
     * @return the final destination and Stardate cost
     */
    private MovementResult findMovementDestination(ArrayList<Location> path) {
        Location startingLocation = enterprise.getLocation();
        Location destination = startingLocation;

        double starDateChange = 0.0;
        Location previousLocation = startingLocation;

        for (Location location : path) {
            QuadrantMap quadrant = at(location);

            // The Enterprise cannot enter an occupied sector.
            if (!quadrant.empty(location))
                break;

            // Successfully entering a different quadrant costs 1 Stardate.
            if (!location.sameQuadrant(previousLocation))
                starDateChange += 1.0;

            destination = location;
            previousLocation = location;
        }

        return new MovementResult(destination, starDateChange);
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
        
        oldQuadrant.clearSector(oldLocation);
        newQuadrant.place(newLocation, QuadrantMap.ENTERPRISE);

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
        ArrayList<Klingon> currentKlingons = getKlingons(location);

        Iterator<Klingon> it = currentKlingons.iterator();
        while (it.hasNext()) {
            Klingon klingon = it.next();

            klingon.adjustEnergy(-enterprise.firePhasers(phaserEnergy, klingon.getLocation().sectorX,
                klingon.getLocation().sectorY, currentKlingons.size()));

            if (!klingon.isDestroyed()) {
                continue;
            }

            galaxy.getQuadrant(location).reduceKlingons();
            at(location).removeObject(klingon.getLocation(), QuadrantMap.KLINGON);

            it.remove();
            IO.printf("Destroyed klingon at %s\n", klingon.getLocation().toString());
        }
    }

    /**
     * 
     * Fires a torpedo from the Enterprise in the direction of warpDirection
     * 
     * @param warpDirection
     */
    private void fireTorpedo(double warpDirection) {
        if (enterprise.getTorpedoes() <= 0)
            return;

        enterprise.reduceTorpedoes();
        // biggest warp factor possible so it won't stop
        ArrayList<Location> path = enterprise.calculatePath(8.0, warpDirection);

        Location destination = findMovementDestination(path).destination();
        Location currLocation = enterprise.getLocation();

        // checks that the torpedo is in the same quadrant
        if (!destination.sameQuadrant(currLocation))
            return;

        // if the next object in the path is a klingon then destroy it
        int indexOf = path.indexOf(destination);
        if (indexOf == -1) {
            Location loc = path.get(0);
            if (at(loc).at(loc).equals(QuadrantMap.KLINGON)) {
                destroyKlingon(loc);
                return;
            }
        }
        
        indexOf++;
        if (indexOf >= path.size())
            return;

        Location loc = path.get(indexOf);
        if (at(loc).at(loc).equals(QuadrantMap.KLINGON)) {
            destroyKlingon(loc);
        }
    }

    /**
     * 
     * Makes the klingons within the Enterprise's quadrant fire at it. 
     * 
     */
    private void klingonsFire() {
        if (enterprise.getDocked()) {
            IO.println("Starbase shields protect you from incoming klingon attacks");
            return;
        }

        Location pos = enterprise.getLocation();
        ArrayList<Klingon> currKlingons = klingons.get(pos.quadrantX).get(pos.quadrantY);

        for (Klingon k : currKlingons) {
            int damage = k.firePhasers(pos.sectorX, pos.sectorY);

            IO.printf("Klingon (%d, %d) has fired their phasers dealing: %d damage\n",
                    GameLib.toBase1(k.getLocation().sectorX), GameLib.toBase1(k.getLocation().sectorY),
                    damage
                );

            enterprise.takeDamage(damage);
        }
    }

    public void klingonsMove() {
        Location position = enterprise.getLocation();
        ArrayList<Klingon> currKlingons = getKlingons(position);

        for (Klingon k : currKlingons) {
            Location location = k.calculateDestination();
            while (!at(location).empty(location)) {
                location = k.calculateDestination();
            }

            QuadrantMap quadrant = at(location);
            quadrant.move(k.getLocation(), location, QuadrantMap.KLINGON);
            k.move(location); 
        }
    }

    /**
     * 
     * Destroys a Klingon. This will remove it from QuadrantMap, Quadrant, and
     * from the Klingons 3D array. 
     * 
     * @param position
     */
    private void destroyKlingon(Location position) {
        ArrayList<Klingon> currKlingons = klingons.get(position.quadrantX).get(position.quadrantY);

        Iterator<Klingon> it = currKlingons.iterator();
        while (it.hasNext()) {
            Klingon klingon = it.next();
            Location klingonLocation = klingon.getLocation();

            if (klingonLocation.sectorX != position.sectorX || klingonLocation.sectorY != position.sectorY)
                continue;

            galaxy.getQuadrant(position).reduceKlingons();
            at(position).removeObject(klingonLocation, QuadrantMap.KLINGON);

            it.remove();
            IO.printf("Destroyed klingon at %s\n", klingonLocation.toString());
        }
    }

    /**
     * 
     * Gets the Klingons at (x, y). Returns a reference to that klingon
     * vector. Takes base-1 coordinates. 
     * 
     * @param x
     * @param y
     * @return
     */
    private ArrayList<Klingon> getKlingons(int x, int y) {
        return klingons.get(GameLib.toBase0(x)).get(GameLib.toBase0(y));
    }

    /**
     * 
     * Gets the Klingons at (x, y). Returns a reference to that klingon
     * vector. Takes base-0 coordinates through location.
     * 
     * @param position
     * @return
     */
    private ArrayList<Klingon> getKlingons(Location position) {
        return klingons.get(position.quadrantX).get(position.quadrantY);
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
            case "TOR":
                torpedoCommand(command);
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
            klingonsFire();
            klingonsMove();
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
        if (enterprise.isDeviceBroken(Devices.SHORT_RANGE_SENSORS)) {
            IO.println("Short Range Sensors needs repair, cannot do scan");
            return;
        }

        Location enterpriseLocation = enterprise.getLocation();
        IO.println(at(enterpriseLocation).toString());
        IO.println(enterprise.toString());
        IO.printf("Star date: %.6f\n", currentStardate);
    }

    /**
     * 
     * Does a long range scan around the Enterprise. Returns
     * the quadrant's KBS value around the Enterprise. 
     * 
     */
    private void longRangeCommand() {
        if (enterprise.isDeviceBroken(Devices.LONG_RANGE_SENSORS)) {
            IO.println("Long Range Sensors needs repair, cannot do scan");
            return;
        }

        Location loc = enterprise.getLocation();
        
        int startY = Math.max(0, loc.quadrantY - 1);
        int endY = Math.min(7, loc.quadrantY + 1);

        int startX = Math.max(0, loc.quadrantX - 1);
        int endX = Math.min(7, loc.quadrantX + 1);

        for (int y = startY; y <= endY; ++y) {
            for (int x = startX; x <= endX; ++x) {
                IO.print(galaxy.getQuadrant(GameLib.toBase1(x), GameLib.toBase1(y)).toString() + " ");
            }

            IO.println("");
        }
    }

    /**
     * 
     * Fires the Enterprise's phasers at Klingons
     * 
     * @param command
     */
    private void phaserCommand(ArrayList<String> command) {
        if (enterprise.isDeviceBroken(Devices.PHASER_CONTROL)) {
            IO.println("Phaser control needs repair, cannot fire phasers");
            return;
        }

        if (command.size() < 2) {
            IO.warning("PHA usage <phaser energy>");
            return;
        }

        try {
            double phaserEnergy = Double.parseDouble(command.get(1));
            firePhasers(phaserEnergy);
            klingonsFire();
            shortRangeCommand();
        } catch (Exception e) {
            IO.warning("Please enter valid doubles");
            return;
        }
    }

    /**
     * 
     * Fires a torpedoe in a certain direction. 
     * 
     * @param command
     */
    private void torpedoCommand(ArrayList<String> command) {
        if (enterprise.isDeviceBroken(Devices.TORPEDO_CONTROL)) {
            IO.println("Torpedo control needs repair, cannot fire a torpedo");
            return;
        }

        if (command.size() < 2) {
            IO.warning("TOR usage <warp direction>");
            return;
        }

        try {
            double warpDirection = Double.parseDouble(command.get(1));
            fireTorpedo(warpDirection);
            klingonsFire();
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
        if (enterprise.isDeviceBroken(Devices.SHIELD_CONTROL)) {
            IO.println("Shield control needs repair, adjust shields");
            return;
        }

         if (command.size() < 2) {
            IO.warning("SHE usage <new shield>");
            return;
        }

        try {
            double newShields = Double.parseDouble(command.get(1));
            if (newShields < 0.0) {
                IO.println("Invalid shields value; must be 0 or greater");
                return;
            }

            if (enterprise.getDocked()) {
                IO.println("Cannot raise shields while docked");
                return;
            }

            enterprise.adjustShields(newShields);

            klingonsFire();
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
        if (enterprise.isDeviceBroken(Devices.DAMAGE_CONTROL)) {
            IO.println("Damage control needs repair, cannot report damage");
            return;
        }


        enterprise.damageReport();
    }

    private Enterprise enterprise;
    
    private Galaxy galaxy;
    private QuadrantMap[][] map;

    private ArrayList<ArrayList<ArrayList<Klingon>>> klingons; 

    private int numKlingons;

    private double currentStardate;
    private int startingStardate;
    private int missionDuration;

    private static final int MAP_SIZE = 8;
    private static final int MIN_SECTOR = 1;
    private static final int MAX_SECTOR = 8;

    private record MovementResult(Location destination, double starDateChange) {}
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