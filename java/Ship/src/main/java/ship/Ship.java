package ship;

import java.util.ArrayList;

import common.GameLib;
import common.GameLib.Location;

/**
 * 
 * The Ship class is the base class to all moveable
 * objects within this game. This includes the Enterprise
 * and Klingons. Every Ship has a specific energy level
 * and location. Location is represented through the
 * Location class. 
 * 
 * Ship implements the following non-virtual functions:
 *  - Getting the current energy level
 *  - Adjusting the current energy level
 *  - Getting the current location
 * 
 * The rest of Ship's functions are virtual:
 *  - Checking whether the ship is destroyed (check through energy level <= 0)
 *  - Calculating the path of a ship based off a warp factor and direction
 *  - Moving the ship (simply adjusts the Ship's location and reduces the energy
 *      level based off how far it went).
 *  - Makes the ship take damage (reduces the energy levels).
 *  - Makes the ship fire its phasers (simply calculates the damage output).
 * 
 * All functions take base-1 coordinates. This includes the firePhasers function.
 * Despite it taking raw coordinates, unlike most classes where it would take
 * base-1 coordinates, this takes base-1 coordinates. 
 * 
 */
public class Ship {
    public Ship(int energy, Location location) {
        this.energy = energy;
        this.location = location;
    }

    public Ship(int energy) {
        this.energy = energy;
        location = new Location();
    }

    public Ship() {
        energy = 0;
        location = new Location();
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
     * Gets the energy of the ship and returns it.
     * 
     * @return the energy of the ship
     */
    public int energy() {
        return energy;
    }

    /**
     * 
     * Adjusts the energy by the amount. Does not check that
     * the energy can go into negatives. 
     * 
     * @param energy
     */
    public void adjustEnergy(int energy) {
        this.energy += energy;
    }

    /**
     * 
     * Checks whether the ship is destroyed based off its energy
     * 
     * @return true if the ship is destroyed
     */
    public boolean isDestroyed() {
        return energy <= 0;
    }

    /**
     * 
     * Makes the ship move based off a warp
     * factor and direction. This uses exact
     * trignonmetry to calculate the precise place
     * the ship will end up. This uses a ray. 
     * 
     * The path will be returned as base-1.
     * 
     * This converts the warp direction into radians
     * (degrees). Based off these degrees it calculates
     * two movement vectors (one for x, and another for y).
     * Based off these vectors we calculate the next 
     * horizontal and vertical boundary we move the ship
     * and add it to the path. 
     * 
     * Cardinal Directions:
     * 
     *         3   
     *     4       2   
     * 5               1
     *     6       8
     *         7
     * 
     * These are calculated into angle degrees through:
     * (warpDirection - 1.0) * 45;
     * 
     * The 45 makes every warpDirection be a different 
     * cardinal direction (e.g. N = 3, SW = 6). The
     * subtraction from one converts warpDirection to
     * base-0 from base-1.
     * 
     * @param warpFactor
     * @param warpDirection
     * @return the path the ship will take to their destination
     */
    public ArrayList<Location> calculatePath(double warpFactor, double warpDirection) {
        ArrayList<Location> path = new ArrayList<>();

        warpFactor = Math.min(warpFactor, 8.0);

        // Warp 1 = 8 sectors
        double distance = warpFactor * GameLib.MAP_SIZE;

        // Convert direction into angle
        double angleDegrees = (warpDirection - 1.0) * 45.0;
        double radians = Math.toRadians(angleDegrees);

        // Direction vector
        double dx = Math.cos(radians);
        double dy = -Math.sin(radians);

        // Normalize
        double length = Math.hypot(dx, dy);
        dx /= length;
        dy /= length;

        // Current galaxy position
        double x = GameLib.toBase0(location.getQuadrantY()) * GameLib.COLS + GameLib.toBase0(location.getSectorY());
        double y = GameLib.toBase0(location.getQuadrantX()) * GameLib.ROWS + GameLib.toBase0(location.getSectorX());

        int lastX = (int) Math.floor(x);
        int lastY = (int) Math.floor(y);

        double travelled = 0;

        while (travelled < distance) {

            double distanceToXBoundary = calculateNextBoundaryX(x, dx);
            double distanceToYBoundary = calculateNextBoundaryY(y, dy);

            double movement = Math.min(distanceToXBoundary, distanceToYBoundary);

            // Prevent overshooting warp distance
            if (travelled + movement > distance)
                movement = distance - travelled;

            x += dx * movement;
            y += dy * movement;

            travelled += movement;

            // Outside galaxy
            if (x < 0 || x >= 64 ||
                y < 0 || y >= 64) {
                break;
            }

            int globalX = (int)Math.floor(x + EPSILON);
            int globalY = (int)Math.floor(y + EPSILON);

            if (globalX != lastX || globalY != lastY) {

                int quadrantX = globalX / GameLib.COLS;
                int quadrantY = globalY / GameLib.ROWS;

                int sectorX = globalX % GameLib.COLS;
                int sectorY = globalY % GameLib.ROWS;

                path.add(new Location(
                        GameLib.toBase1(sectorX),
                        GameLib.toBase1(sectorY),
                        GameLib.toBase1(quadrantX),
                        GameLib.toBase1(quadrantY)));

                lastX = globalX;
                lastY = globalY;
            }
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
     * the damage destroys the ship or not. Based off the
     * ship's energy (because of how klingons work).
     * 
     * @param phaserEnergy
     * @return true if the ship is destoryed
     */
    public boolean takeDamage(double phaserEnergy) {
        adjustEnergy((int)-phaserEnergy);
        if (energy <= 0) {
            energy = 0;
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
    public int firePhasers(double phaserEnergy, int x, int y) {
        double distance = Math.hypot(location.getSectorY() - x, location.getSectorX() - y);

        return (int) ((phaserEnergy / distance) * (common.GameLib.random() + 2));
    }

    /**
     * 
     * Calculates the next boundary the ship will pass
     * through on the x axis. This is based off the
     * direction, and current x position.
     *  
     * Returns a infinity is there is none. This makes
     * the ship overshoot the warp factor making it stop
     * the calculation.
     * 
     * @param x
     * @param dx
     * @return the next x boundary the ship will pass
     */
    private static double calculateNextBoundaryX(double x, double dx) {
        if (dx > 0)
            return (Math.floor(x) + 1 - x) / dx;

        if (dx < 0)
            return (Math.ceil(x) - 1 - x) / dx;

        return Double.POSITIVE_INFINITY;
    }

    /**
     * 
     * Calculates the next boundary the ship will pass
     * through on the y axis. This is based off the
     * direction, and current y position.
     * 
     * Returns a infinity is there is none. This makes
     * the ship overshoot the warp factor making it stop
     * the calculation.
     * 
     * @param y
     * @param dy
     * @return the next y boundary the ship will pass
     */
    private static double calculateNextBoundaryY(double y, double dy) {
        if (dy > 0)
            return (Math.floor(y) + 1 - y) / dy;

        if (dy < 0)
            return (Math.floor(y) - 1 - y) / dy;

        return Double.POSITIVE_INFINITY;
    }

    private int energy;

    private Location location;

    private static final double EPSILON = 1e-9;
}