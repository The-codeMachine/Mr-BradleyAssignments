package ship;

import java.util.ArrayList;

import common.GameLib.Location;

/**
 * 
 * This is the base Ship class. The ship class
 * consists of shield, and position information.
 * It handles movement calculation, damage reduction,
 * and phaser firing for all base ships. Other ships
 * like the Enterprise might use this as a super 
 * class and work upon the current functions 
 * (e.g. adding checks for devices).
 * Current list of operations consist of:
 *  - Move (move the ship based off warp factor, and direction)
 *  - Make the ship take damage
 *  - Fire the ship's phasers
 * 
 * Ship's get location, construction, and fire phasers all
 * take base-1 as input. 
 * 
 * Internally, all the variables are base-0. 
 * 
 */
public class Ship {
    public Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY) {
        this.shields = shields;
        
        location = new Location(common.GameLib.toBase0(sectorX), common.GameLib.toBase0(sectorY),
            common.GameLib.toBase0(quadrantX), common.GameLib.toBase0(quadrantY)); 
    }

    /**
     * 
     * Gets the ship's local position (which 
     * sector it is currently in).
     * 
     * @return the ship's sector position
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * Gets the shields of the ship and returns it.
     * 
     * @return the shields of the ship
     */
    public double shields() {
        return shields;
    }

    /**
     * 
     * Makes the ship move based off a warp
     * factor and direction. This uses exact
     * trignonmetry to calculate the precise place
     * the ship will end up. 
     * 
     * The path will be returned as base-0. 
     * 
     * @param warpFactor
     * @param warpDirection
     * @return the path the ship will take to their destination 
     */
    public ArrayList<Location> calculatePath(double warpFactor, double warpDirection) {
        if (warpFactor > 10.0)
            warpFactor = 10.0;

        ArrayList<Location> path = new ArrayList<>();

        double currentGlobalX = location.quadrantX * GRID_SIZE + location.sectorX;
        double currentGlobalY = location.quadrantY * GRID_SIZE + location.sectorY;

        double distanceInSectors = warpFactor * GRID_SIZE;

        double angleDegrees = 90.0 - (warpDirection - 1.0) * 45.0;
        double radians = Math.toRadians(angleDegrees);

        double deltaX = distanceInSectors * Math.cos(radians);
        double deltaY = distanceInSectors * Math.sin(radians);

        double newGlobalX = common.MathUtils.clamp(currentGlobalX + deltaX, 0.0, 63.99);
        double newGlobalY = common.MathUtils.clamp(currentGlobalY + deltaY, 0.0, 63.99);

        double dx = newGlobalX - currentGlobalX;
        double dy = newGlobalY - currentGlobalY;

        int steps = (int)Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)));

        int lastGlobalX = -1;
        int lastGlobalY = -1;

        for (int i = 1; i <= steps; i++) {
            double t = (double)i / steps;

            int globalX = (int)(currentGlobalX + dx * t);
            int globalY = (int)(currentGlobalY + dy * t);

            if (globalX == lastGlobalX && globalY == lastGlobalY)
                continue;

            lastGlobalX = globalX;
            lastGlobalY = globalY;

            int quadrantX = globalX / GRID_SIZE;
            int quadrantY = globalY / GRID_SIZE;

            int sectorX = globalX % GRID_SIZE;
            int sectorY = globalY % GRID_SIZE;

            path.add(new Location(sectorX, sectorY, quadrantX, quadrantY));
        }

        return path;
    }

    /**
     * 
     * Moves the ship to the new location.
     * Does not do any checks to validate
     * that the location is a valid position. 
     * 
     * @param location
     */
    public void move(Location location) {
        this.location = location;
    }

    /**
     * 
     * Makes the ship take damage. Returns whether
     * the damage destroys the ship or not. 
     * 
     * @param phaserEnergy
     * @return true if the ship is destoryed 
     */
    public boolean takeDamage(double phaserEnergy) {
        shields -= phaserEnergy;
        if (shields <= 0) {
            shields = 0;
            return true;
        }

        return false;
    }

    /**
     * 
     * Calculates the effective phaser energy
     * based off how much is fired, how 
     * far the ship is, and how many klingons
     * are in the quadant currently. 
     * 
     * @param phaserEnergy
     * @param x
     * @param y
     * @param numKlingons
     * 
     * @return the effective phaserEnergy based off calculations
     */
    public int firePhasers(double phaserEnergy, int x, int y, int numKlingons) {
        double distance = Math.sqrt(Math.pow(location.sectorX - x, 2) + Math.pow(location.sectorY - y, 2));
        double h = phaserEnergy / numKlingons;

        return (int)((h / distance) * (common.GameLib.random() + 2));
    }

    private double shields;
    
    private Location location;

    private static final int GRID_SIZE = 8;
}