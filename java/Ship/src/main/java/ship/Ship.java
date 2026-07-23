package ship;

import java.util.ArrayList;

import common.IO;
import common.GameLib.Location;

/**
 * 
 * TODO:
 * Resonably large issue. Not sure how to handle it currently,
 * but essentially, the QuadrantMap is upside-down. As y goes
 * up it goes down. This is backwards to what is normal. This
 * issue is currently fixed by adjusting the delta-y to be 
 * negative, this makes North = up, but we might want to flip
 * the QuadrantMap, maybe just in the printing section. Any 
 * ideas? 
 * 
 */

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
 * - Move (move the ship based off warp factor, and direction)
 * - Make the ship take damage
 * - Fire the ship's phasers
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
     * This converts the warp direction into radians
     * (degrees). Based off these degrees, it constructs
     * a ratio of x sectors to y sectors travelled. It
     * then simulates travelling through all these sectors
     * and adds it to the path which it returns. 
     * 
     * @param warpFactor
     * @param warpDirection
     * @return the path the ship will take to their destination
     */
    public ArrayList<Location> calculatePath(double warpFactor, double warpDirection) {
        ArrayList<Location> path = new ArrayList<>();

        warpFactor = Math.min(warpFactor, 10.0);

        // Warp 1 = 8 sectors
        int distance = (int) Math.round(warpFactor * GRID_SIZE);

        // Convert direction into angle
        double angleDegrees = 90.0 - (warpDirection - 1.0) * 45.0;
        double radians = Math.toRadians(angleDegrees);

        // Direction ratio
        double dx = Math.cos(radians);
        double dy = -Math.sin(radians);

        // Normalize
        double length = Math.sqrt(dx * dx + dy * dy);

        dx /= length;
        dy /= length;

        // Current galaxy position
        double x = location.quadrantX * GRID_SIZE + location.sectorX;
        double y = location.quadrantY * GRID_SIZE + location.sectorY;

        int lastX = (int) x;
        int lastY = (int) y;

        double travelled = 0;

        IO.printf("angleDegrees: %.3f\n", angleDegrees);
        IO.printf("radians: %.3f\n", radians);
        IO.printf("dx, dy: %.3f, %.3f\n", dx, dy);

        while (travelled < distance) {

            IO.printf("(x, y): %.3f, %.3f\n", x, y);

            x += dx;
            y += dy;

            travelled++;

            // Outside galaxy
            if (x < 0 || x >= 64 ||
                    y < 0 || y >= 64) {
                break;
            }

            int globalX = (int) Math.floor(x);
            int globalY = (int) Math.floor(y);

            if (globalX != lastX || globalY != lastY) {

                path.add(new Location(
                        globalX % GRID_SIZE,
                        globalY % GRID_SIZE,
                        globalX / GRID_SIZE,
                        globalY / GRID_SIZE));

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

        return (int) ((h / distance) * (common.GameLib.random() + 2));
    }

    private double shields;

    private Location location;

    private static final int GRID_SIZE = 8;
}