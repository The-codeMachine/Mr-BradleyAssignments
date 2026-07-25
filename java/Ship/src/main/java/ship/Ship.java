package ship;

import java.util.ArrayList;

import common.IO;
import common.GameLib;
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

        warpFactor = Math.min(warpFactor, 8.0);

        // Warp 1 = 8 sectors
        int distance = (int) Math.round(warpFactor * GRID_SIZE);

        // Convert direction into angle
        double angleDegrees = 90.0 - (warpDirection - 1.0) * 45.0;
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

        IO.printf("Start Quadrant : (%d, %d)\n",
                GameLib.toBase1(location.quadrantX),
                GameLib.toBase1(location.quadrantY));

        IO.printf("Start Sector   : (%d, %d)\n",
                GameLib.toBase1(location.sectorX),
                GameLib.toBase1(location.sectorY));

        IO.println("");

        IO.printf("Global Position\n");
        IO.printf("X = %d * %d + %d = %.3f\n",
                location.quadrantX,
                GRID_SIZE,
                location.sectorX,
                x);

        IO.printf("Y = %d * %d + %d = %.3f\n",
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

        IO.printf("Angle = 90 - ((%.3f - 1.0) * 45)\n", warpDirection);
        IO.printf("      = %.6f degrees\n", angleDegrees);

        IO.printf("Radians = %.6f\n", radians);

        IO.println("");

        IO.printf("Direction Vector\n");
        IO.printf("dx = cos(%.6f) = %.6f\n", radians, dx);
        IO.printf("dy = -sin(%.6f) = %.6f\n", radians, dy);

        IO.printf("Vector Length = %.6f\n", length);

        IO.println("");

        int step = 1;
        double travelled = 0;

        while (travelled < distance) {

            IO.println("--------------------------------------------------");
            IO.printf("Step %d\n", step++);

            IO.println("");

            IO.println("Current Position");
            IO.printf("x = %.6f\n", x);
            IO.printf("y = %.6f\n", y);

            double nextX = x + dx;
            double nextY = y + dy;

            IO.println("");

            IO.println("Movement");

            IO.printf("x = %.6f + %.6f = %.6f\n",
                    x, dx, nextX);

            IO.printf("y = %.6f + %.6f = %.6f\n",
                    y, dy, nextY);

            x = nextX;
            y = nextY;

            travelled++;

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
            IO.println("Sector Calculation");

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

                IO.printf("Quadrant      : (%d, %d)\n",
                        GameLib.toBase1(quadrantX),
                        GameLib.toBase1(quadrantY));

                IO.printf("Local Sector  : (%d, %d)\n",
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
            IO.printf("Quadrant (%d,%d) Sector (%d,%d)\n",
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