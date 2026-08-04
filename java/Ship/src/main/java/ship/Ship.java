package ship;

import java.util.ArrayList;

import common.IO;
import common.GameLib;
import common.GameLib.Location;

/**
 * 
 * This is the base Ship class. The ship class
 * consists of energy, and position information.
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
     * The path will be returned as base-0.
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
        int distance = (int) Math.round(warpFactor * GRID_SIZE);

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
        double x = location.quadrantX * GRID_SIZE + location.sectorX;
        double y = location.quadrantY * GRID_SIZE + location.sectorY;

        int lastX = (int) Math.floor(x);
        int lastY = (int) Math.floor(y);

        IO.println("==================================================");
        IO.println("Navigation Calculation");
        IO.println("==================================================");

        IO.printf("Start Quadrant (column, row) : (%d, %d)\n",
                GameLib.toBase1(location.quadrantX),
                GameLib.toBase1(location.quadrantY));

        IO.printf("Start Sector (column, row)   : (%d, %d)\n",
                GameLib.toBase1(location.sectorX),
                GameLib.toBase1(location.sectorY));

        IO.println("");

        IO.printf("Global Position (column, row)\n");
        IO.printf("X = %d * %d + %d + 0.5 = %.3f\n",
                location.quadrantX,
                GRID_SIZE,
                location.sectorX,
                x);

        IO.printf("Y = %d * %d + %d + 0.5 = %.3f\n",
                location.quadrantY,
                GRID_SIZE,
                location.sectorY,
                y);

        IO.println("");

        IO.printf("Warp Factor = %.2f\n", warpFactor);
        IO.printf("Distance = round(%.2f * %d) = %d sectors\n",
                warpFactor,
                GRID_SIZE,
                distance);

        IO.println("");

        IO.printf("Direction = %.3f\n", warpDirection);

        IO.printf("Angle = ((%.3f - 1.0) * 45)\n", warpDirection);
        IO.printf("      = %.6f degrees\n", angleDegrees);

        IO.printf("Radians = %.6f\n", radians);

        IO.println("");

        IO.printf("Direction Vector\n");
        IO.printf("dx = cos(%.6f) = %.6f (takes radians)\n", radians, dx);
        IO.printf("dy = -sin(%.6f) = %.6f (takes radians)\n", radians, dy);

        IO.printf("Vector Length = %.6f\n", length);

        IO.println("");

        double travelled = 0;

        while (travelled < distance) {

            IO.println("--------------------------------------------------");

            IO.println("");

            IO.println("Current Position (column, row)");
            IO.printf("x = %.6f\n", x);
            IO.printf("y = %.6f\n", y);

            double distanceToXBoundary = calculateNextBoundaryX(x, dx);
            double distanceToYBoundary = calculateNextBoundaryY(y, dy);

            double movement = Math.min(distanceToXBoundary, distanceToYBoundary);

            // Prevent overshooting warp distance
            if (travelled + movement > distance)
                movement = distance - travelled;

            IO.println("");

            IO.println("Movement (column, row)");

            IO.printf("x = %.6f + %.6f * %.6f = %.6f\n",
                    x, dx, distanceToXBoundary, x + dx * movement);

            IO.printf("y = %.6f + %.6f * %.6f = %.6f\n",
                    y, dy, distanceToYBoundary, y + dy * movement);

            x += dx * movement;
            y += dy * movement;

            travelled += movement;

            // Outside galaxy
            if (x < 0 || x >= 64 ||
                y < 0 || y >= 64) {

                IO.println("");
                IO.println("Movement exits the galaxy.");
                break;
            }

            int globalX = (int)Math.floor(x);
            int globalY = (int)Math.floor(y);

            IO.println("");
            IO.println("Sector Calculation (column, row)");

            IO.printf("floor(%.6f) = %d\n", x, globalX);
            IO.printf("floor(%.6f) = %d\n", y, globalY);

            if (globalX != lastX || globalY != lastY) {

                int quadrantX = globalX / GRID_SIZE;
                int quadrantY = globalY / GRID_SIZE;

                int sectorX = globalX % GRID_SIZE;
                int sectorY = globalY % GRID_SIZE;

                IO.println("");
                IO.println("Sector boundary crossed.");

                if (globalX != lastX)
                    IO.printf("X changed: %d -> %d\n", lastX, globalX);

                if (globalY != lastY)
                    IO.printf("Y changed: %d -> %d\n", lastY, globalY);

                IO.printf("Global Sector : (%d, %d)\n",
                        globalX,
                        globalY);

                IO.printf("Quadrant (column, row)     : (%d, %d)\n",
                        GameLib.toBase1(quadrantX),
                        GameLib.toBase1(quadrantY));

                IO.printf("Local Sector (column, row) : (%d, %d)\n",
                        GameLib.toBase1(sectorX),
                        GameLib.toBase1(sectorY));

                path.add(new Location(
                        sectorX,
                        sectorY,
                        quadrantX,
                        quadrantY));

                lastX = globalX;
                lastY = globalY;
            } else {
                IO.println("");
                IO.println("Still inside the current sector.");
            }
        }

        IO.println("");
        IO.println("==================================================");
        IO.println("Visited Sectors");
        IO.println("==================================================");

        for (Location l : path) {
            IO.printf("Quadrant (column, row) (%d,%d) Sector (column, row) (%d,%d)\n",
                    GameLib.toBase1(l.quadrantX),
                    GameLib.toBase1(l.quadrantY),
                    GameLib.toBase1(l.sectorX),
                    GameLib.toBase1(l.sectorY));
        }

        IO.println("==================================================");

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
    public void move(Location location, double warpFactor) {
        int energyUsed = (int) (warpFactor * 8 + 0.5);
        if (energy < energyUsed) {
            IO.println("Insufficient energy for warp");
            return;
        }

        adjustEnergy(-energyUsed);
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
        double distance = Math.sqrt(Math.pow(location.sectorX - x, 2) + Math.pow(location.sectorY - y, 2));

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

    private static final int GRID_SIZE = 8;
}