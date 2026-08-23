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
 * The Game class is the largest in Super Star Trek. It
 * is the entry point of the entire system. It owns 
 * everything from the Enterprise to Klingons, and the
 * Galaxy. As such, there are few public functions, these
 * include:
 *  - Access a quadrant map at (x, y) 
 *  - Access to the Enterprise
 *  - Running the game
 *  - Moving the Enterprise (probably will be moved to private but
 *      it was public for tests).
 * 
 * All functions either take base-1 coordinates through raw values
 * (x, y), or base-0 coordinates through the Location class. 
 * 
 * Game handles the time as well. 
 * 
 * Game forwards the user's commands to their specific functions. It
 * tells the Klingons to fire, and move. Game handles win and lose 
 * conditions. It also handles all library computer functions. 
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
        return at(location.getQuadrantY(), location.getQuadrantX());
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
        IO.println("Your mission begins with your starship located");
        IO.printf("in the galatic quadrant %s\n", Galaxy.getGalaticRegionName(enterprise.getLocation()));
        
        // checks the current quadrant
        shortRangeCommand();

        // handles command parsing 
        while ( handleCommand() && 
                galaxy.klingons() > 0 && 
        currentStardate <= missionDuration + startingStardate) {}

        // success
        if (galaxy.klingons() <= 0) {
            IO.println("Congratulations, captain! The last klingon battle cruiser");
            IO.println("menacing the Federation has been destroyed.");
            IO.printf("Your efficiency rating is: %0.6f", 1000 * Math.pow(currentStardate - startingStardate, 2));
            return;
        }

        // lost because of time
        if (currentStardate > missionDuration + startingStardate) {
            IO.printf("It is stardate: %.3f\n", currentStardate);
            IO.printf("There were %d Klingon warships left to destroy\n", galaxy.klingons());
            IO.println("before they could launch their attack against the");
            IO.println("Federation. They will destroy the Enterprise as well");
            IO.println("as the Federation. ");
            return;
        }
        
        // the enterprise got destroyed
        // if the shields are == -1 then that means a special death occurred
        // and that death message already printed, so skip a death message. 
        if (enterprise.isDestroyed() && enterprise.shields() != -1) {
            IO.printf("It is stardate: %.3f\n", currentStardate);
            IO.printf("There were %d Klingon warships left to destroy\n", galaxy.klingons());
            IO.println("The Enterprise has been destroyed and now there is ");
            IO.println("nothing stopping the Klingons from destroying the");
            IO.println("Federation. ");
        }

        IO.println("The Federation is in need of a new starship commander");
        IO.println("for a similar mission -- if there is a volunteer let");
        IO.println("him step forward and say \"Aye.\"");

        // restart the game if the player enters AYE
        if (IO.prompt("").toUpperCase().equals("AYE")) {
            enterprise = new Enterprise(3000, 0.0, 10, false);
            constructGame();
            run();
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
        enterprise.updateDocked(at(newLocation).canDock());

        return newLocation.equals(path.getLast());
    }

    /**
     * 
     * Constructs the game  
     * 
     */
    private void constructGame() {
        this.enterprise = new Enterprise(3000, 0, 10, false);
        galaxy = new Galaxy();
        map = new QuadrantMap[8][8];
        
        initializeQuadrants();
        initializeTime();
        placeEnterprise();
    }

    /**
     * 
     * Initializes all Quadrants for the Game
     * 
     */
    private void initializeQuadrants() {
        for (int x = 0; x < GameLib.MAP_SIZE; ++x) {
            for (int y = 0; y < GameLib.MAP_SIZE; ++y) {
                map[x][y] = new QuadrantMap(galaxy.getQuadrant(GameLib.toBase1(x), GameLib.toBase1(y)));
            }
        }
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

        // checks that it entered a new quadrant
        if (!newLocation.sameQuadrant(oldLocation))
            IO.printf("\nNow entering %s quadrant ...\n\n", Galaxy.getGalaticRegionName(newLocation));
    }

    /**
     * 
     * Destroys a star base within the Galaxy. Removes it from the
     * quadrant and galaxy. 
     * 
     * @param location
     */
    private void destroyStarbase(Location location) {
        IO.println("***Starbase Destroyed***");

        galaxy.reduceStarBases(location);
        at(location).removeObject(location, QuadrantMap.BASE);

        if (galaxy.starBases() > 0 || galaxy.klingons() > missionDuration + startingStardate - currentStardate) {
            IO.println("Starfleet command is reviewing your record to consider court martial");
        } else {
            IO.println("That does it, Captain! You are hereby relieved of command");
            IO.println("and sentenced to 99 stardates at hard labour on Cygnus 12!");
            enterprise.kill();
        }
    }

    /**
     * 
     * Handles firing the phasers at all klingons. If a klingon is
     * destroyed, it will handle destroying the klingon as well. 
     * 
     * @param phaserEnergy
     */
    private void firePhasers(double phaserEnergy) {
        Location location = enterprise.getLocation();
        
        // works on copy so destroyKlingon can safely modify 
        ArrayList<Klingon> currentKlingons = new ArrayList<>(at(location).klingons());
        if (currentKlingons.size() <= 0) {
            IO.println("Science officer Spock reports: ");
            IO.println("\"Sensors show no enemy ships in this quadrant\"");
            return;
        }
        
        enterprise.adjustEnergy((int)-phaserEnergy);

        if (enterprise.isDeviceBroken(Devices.COMPUTER_SYSTEMS)) {
            IO.println("Computer failure hampers accuracy");
        }

        IO.println("Phasers locked on target.");

        Iterator<Klingon> it = currentKlingons.iterator();
        int klingonSize = currentKlingons.size();
        while (it.hasNext()) {
            Klingon klingon = it.next();
            Location klingonLocation = klingon.getLocation();

            int damage = enterprise.firePhasers(phaserEnergy, klingonLocation.getSectorY(), 
                                                klingonLocation.getSectorX(), klingonSize);
            klingon.adjustEnergy(-damage);

            IO.printf("%d unit hit on Klingon at %s\n", 
                        damage, klingonLocation.sectorString()
            );

            if (!klingon.isDestroyed()) {
                IO.printf("(Sensors show %d units remaining on klingon: %s)\n",
                        klingon.energy(), klingonLocation.sectorString()
                );
                continue;
            }

            destroyKlingon(klingonLocation);
        }
    }

    /**
     * 
     * Fires a torpedo from the Enterprise in the direction of warpDirection
     * 
     * @param warpDirection
     */
    private void fireTorpedo(double warpDirection) {
        if (enterprise.getTorpedoes() <= 0) {
            IO.println("All photon torpedoes expended");
            return;
        }

        enterprise.reduceTorpedoes();
        // biggest warp factor possible so it won't stop
        ArrayList<Location> path = enterprise.calculatePath(8.0, warpDirection);

        Location destination = findMovementDestination(path).destination();
        Location currLocation = enterprise.getLocation();

        // finds where the torpedo stopped
        int indexOf = path.indexOf(destination);
        // if it is not found then the torpedoe stopped at its first sector
        if (indexOf == -1) {
            indexOf = 0;
        } else {
            // if the next object is not within the path it went out of the galaxy
            indexOf++;
            if (indexOf == path.size()) {
                IO.println("Torpedo missed");
                return;
            }
        }

        // print the torpedo's track
        IO.println("Torpedo Track");
        for (int i = 0; i <= indexOf; ++i) {
            IO.println(path.get(i).toString());
        }

        // checks that the torpedo is in the same quadrant
        if (!destination.sameQuadrant(currLocation)) {
            IO.println("Torpedo missed");
            return;
        }

        Location loc = path.get(indexOf);
        String sector = at(loc).at(loc);
        if (sector.equals(QuadrantMap.KLINGON)) {
            destroyKlingon(loc);
        } else if (sector.equals(QuadrantMap.BASE)) {
            destroyStarbase(loc);
        } else if (sector.equals(QuadrantMap.STAR)) {
            IO.printf("Star at %s absorbed torpedo energy\n", loc.sectorString());
        }
    }

    /**
     * 
     * Destroys the klingon at a position. Removes it from QuadrantMap,
     * the klingons vector, and galaxy. 
     * 
     * @param position
     */
    private void destroyKlingon(Location position) {
        ArrayList<Klingon> currKlingons = at(position).klingons();

        Iterator<Klingon> it = currKlingons.iterator();
        while (it.hasNext()) {
            Klingon klingon = it.next();
            Location klingonLocation = klingon.getLocation();

            if (!klingonLocation.sameSector(position))
                continue;

            at(position).removeObject(klingonLocation, QuadrantMap.KLINGON);
            galaxy.getQuadrant(position).reduceKlingons();

            IO.printf("***Klingon Destroyed***\nKlingon: %s\n", klingonLocation.sectorString());
            it.remove();
            return;
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
            case "TOR":
                torpedoCommand(command);
                break;
            case "SHE":
                shieldCommand(command);
                break;
            case "DAM":
                damageReportCommand();
                break;
            case "COM":
                computerLibraryCommand(command);
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

            Location oldPos = enterprise.getLocation();
            boolean moveSuccess = move(warpFactor, warpDirection);

            Location newLocation = enterprise.getLocation();
            if (oldPos != newLocation) {
                at(newLocation).klingonsMove();
                enterprise.takeDamage(at(newLocation).klingonsFire());
                if (enterprise.isDestroyed())
                    return;
            }

            if (!moveSuccess) {
                IO.printf("Warp engines shut down at %s due to bad navigation\n", newLocation.sectorString());
            }

            shortRangeCommand();
        } catch (Exception e) {
            IO.exception(e);
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

        // Determine status condition
        Location location = enterprise.getLocation();
        String condition = "Green";

        if (enterprise.energy() < 300)
            condition = "Yellow";

        if (galaxy.getQuadrant(location).klingons() > 0)
            condition = "*Red*";

        if (enterprise.getDocked())
            condition = "Docked";

        IO.printf("Status condition: %s%n", condition);

        String[] mapLines = at(location).toString().split("\n");

        String[] statusLines = {
            String.format("Energy: %d", enterprise.energy()),
            String.format("Location: %s", enterprise.getLocation()),
            String.format("Torpedoes: %d", enterprise.getTorpedoes()),
            String.format("Shields: %.1f", enterprise.shields()),
            String.format("Docked: %s", enterprise.getDocked()),
            String.format("Klingons left: %d", galaxy.klingons()),
            String.format("Star date: %.3f", currentStardate)
        };

        for (int i = 0; i < mapLines.length; i++) {
            String status = i < statusLines.length ? statusLines[i] : "";
            IO.printf("%-40s   %s%n", mapLines[i], status);
        }

        IO.println("");
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

        galaxy.longRangeScan(enterprise.getLocation());
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
            enterprise.takeDamage(at(enterprise.getLocation()).klingonsFire());
        } catch (Exception e) {
            IO.exception(e);
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
            enterprise.takeDamage(at(enterprise.getLocation()).klingonsFire());
        } catch (Exception e) {
            IO.exception(e);
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

            enterprise.takeDamage(at(enterprise.getLocation()).klingonsFire());
        } catch (Exception e) {
            IO.exception(e);
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

        // repair the devices
        double repairTime = enterprise.estimateRepairDevices();
        if (repairTime <= 0.0)  
            return;

        IO.println("Technicians standing by to effect repairs to your ship;");
        IO.printf("estimated time to repair %.3f stardates\n", repairTime);

        String ans = IO.prompt("will you authorize the repair order (Y/N): ");
        if (ans.toUpperCase().equals("Y"))  {
            currentStardate += repairTime;
            enterprise.repairDevices();
        }
    }

    /**
     * 
     * Starts the library computer and forwards the user's requests to
     * the specific library function. 
     * 
     * @param command
     */
    void computerLibraryCommand(ArrayList<String> command) {
        if (enterprise.isDeviceBroken(Devices.COMPUTER_SYSTEMS.toString())) {
            IO.println("Computer systems needs repairs, cannot access library computer");
            return;
        }

        if (command.size() < 2) {
            IO.warning("COM usage <COMMAND INDEX>");
            return;
        }

        try {
            int cmd = Integer.parseInt(command.get(1));

            switch (cmd) {
                case 0:
                    computerLibraryCommandCGR();
                    break;
                case 1:
                    computerLibraryCommandSR();
                    break;
                case 2:
                    computerLibraryCommandPTD();
                    break;
                case 3:
                    computerLibraryCommandSND();
                    break;
                case 4:
                    computerLibraryCommandDC();
                    break;
                case 5:
                    computerLibraryCommandGRNM();
                    break;
                default:
                    IO.println("Invalid command index");
                    break;
            }
        } catch (NumberFormatException e) {
            IO.exception(e);
        }
    }

    /**
     * 
     * Prints the cumulative galactic record to the user. This displays
     * a full memory grid of all historical short range and long range
     * scans the user has done so far. 
     * 
     */
    void computerLibraryCommandCGR() {
        galaxy.printScannedGalaxy();
    }

    /**
     * 
     * Prints a status report. Shows remaining Klingon warships,
     * stardates left, starbases available, and the ship's current condition.
     * 
     */
    void computerLibraryCommandSR() {
        IO.println("Status Report:\n");
        IO.printf("Klingons left: %d%n\n", galaxy.klingons());
        IO.printf(
            "Mission must be completed in %.3f stardates\n",
            missionDuration + (startingStardate - currentStardate)
        );

        // checks that there are still starbases in the galaxy
        if (galaxy.starBases() < 1) {
            IO.println(
                "Your stupidity has left you on your on in the galaxy -- you have no starbases left"
            );
        } else {
            IO.printf(
                "The Federation is maintaining %d starbases in the galaxy\n\n",
                galaxy.starBases()
            );
        }

        damageReportCommand();
    }

    /**
     * 
     * // Calculates the prints the precise firing directions and distances
     * // from the Enterprise to every Klingon inside the Enterprise's current Quadrant.
     * 
     */
    void computerLibraryCommandPTD() {
        Location startingLocation = enterprise.getLocation();
        ArrayList<Klingon> currKlingons = at(startingLocation).klingons();

        if (currKlingons.isEmpty()) {
            IO.println("Science officer Spock reports: ");
            IO.println("\"Sensors show no enemy ships in this quadrant\"");
            return;
        }

        IO.println("From Enterprise to Klingon battle cruiser: ");

        for (Klingon k : currKlingons) {
            DDResult dd = calculateDD(
                startingLocation,
                k.getLocation()
            );

            IO.printf("%nKlingon %s: \n", k.getLocation().sectorString());
            IO.printf("Direction: %.10f\n", dd.direction);
            IO.printf("Factor: %.10f\n", dd.factor);
        }
    }

    /**
     * 
     * Provides the user with exact warp direction and distance needed to reach a 
     * starbase located inside the Enterprise's current Quadrant.
     * 
     */
    void computerLibraryCommandSND() {
        Location startingPosition = enterprise.getLocation();

        Location starbaseLocation = at(startingPosition).base();

        if (starbaseLocation.equals(new Location(Location.INVALID, Location.INVALID, 
                Location.INVALID, Location.INVALID))) {
            IO.println("Mr. Spock reports: ");
            IO.println("\"Sensors show no starbases in this quadrant\"");
            return;
        }

        // Determine which side of the starbase the Enterprise is on.
        int dx = startingPosition.getSectorY() - starbaseLocation.getSectorY();
        int dy = startingPosition.getSectorX() - starbaseLocation.getSectorX();

        // Select the starbase's neighboring sector closest to the Enterprise.
        //
        // If the Enterprise is east of the starbase, target the east neighbor.
        // If it is northeast, target the northeast neighbor, etc.
        Location targetLocation = new Location(
            starbaseLocation.getSectorY(),
            starbaseLocation.getSectorX(),
            starbaseLocation.getQuadrantY(),
            starbaseLocation.getQuadrantX()
        );

        if (dx > 0)
            targetLocation.setSectorY(targetLocation.getSectorY() + 1);
        else if (dx < 0)
            targetLocation.setSectorY(targetLocation.getSectorY() - 1);

        if (dy > 0)
            targetLocation.setSectorX(targetLocation.getSectorX() + 1);
        else if (dy < 0)
            targetLocation.setSectorX(targetLocation.getSectorX() - 1);

        IO.println(targetLocation.toString());

        DDResult dd = calculateDD(startingPosition, targetLocation);

        IO.println("From Enterprise to Starbase: ");
        IO.printf("Direction: %.10f%nFactor: %.10f\n", dd.direction, dd.factor);
    }

    /**
     * 
     * Calculates the distance and direction the Enterprise must travel to reach a 
     * specific sector in inside its current Quadrant. 
     * 
     */
    void computerLibraryCommandDC() {
        IO.println("Initial sector: ");
        Location startingPosition = IO.promptSector();

        IO.println("Final sector: ");
        Location endingPosition = IO.promptSector();

        DDResult dd = calculateDD(startingPosition, endingPosition);

        IO.printf("Direction: %.10f%nFactor: %.10f\n", dd.direction, dd.factor);
    }

    /**
     * 
     * 
     * Prints the galatic region name map. There are 16 major galatic regions mapped
     * across the game's world. 
     * 
     */
    void computerLibraryCommandGRNM() {
        Galaxy.printGalaticRegionMap();
    }

    /**
     * 
     * Calculates the distance and direction something must travel to reach another 
     * position. Returns the direction and factor as references. 
     * 
     * @param startingLocation
     * @param endingLocation
     * @return the direction and factor
     */
    DDResult calculateDD(Location startingLocation, Location endingLocation) {
        double direction = 0.0;
        double factor = 0.0;

        // calculate distance
        double dx = endingLocation.getSectorY() - startingLocation.getSectorY();
        double dy = startingLocation.getSectorX() - endingLocation.getSectorX();

        double distance = Math.hypot(dx, dy);

        if (distance == 0.0)
            return new DDResult(direction, factor);

        factor = distance / GameLib.MAP_SIZE;

        double degrees = Math.toDegrees(Math.atan2(dy, dx));

        if (degrees < 0.0)
            degrees += 360.0;

        direction = 1.0 + degrees / 45.0;

        if (direction >= 9.0)
            direction -= 8.0;

        return new DDResult(direction, factor);
    }

    private Enterprise enterprise;
    
    private Galaxy galaxy;
    private QuadrantMap[][] map;

    private double currentStardate;
    private int startingStardate;
    private int missionDuration;

    private record DDResult(double direction, double factor) {}
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