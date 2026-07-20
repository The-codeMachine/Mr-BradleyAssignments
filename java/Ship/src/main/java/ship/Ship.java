package ship;

import static common.MathUtils.clamp;

/**
 * TODO:
 * Find a way to handle ship destruction 
 * within the shipl Maybe return a boolean
 * returning whether or not it was destroyed, 
 * and then the QuadrantMap will remove it
 * if it was destroyed. Or if the Enterprise
 * gets destroyed end the game. But we will
 * simply override that. 
 * 
 */

/**
 * TODO:
 * Add a phaser firing functionality. Make
 * sure the phaser calculation works. Currently,
 * though we don't do this until Mr. Bradley 
 * tells us to. We currently only need the 
 * movement functionality. 
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
 *  - Move (move the ship based off warp factor, and direction)
 *  - Make the ship take damage
 *  - Fire the ship's phasers
 * 
 */
public class Ship {
    public Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY) {
        this.shields = shields;
        
        this.sectorX = sectorX;
        this.sectorY = sectorY;

        this.quadrantX = quadrantX;
        this.quadrantY = quadrantY;
    }

    /**
     * 
     * Gets the ship's local position (which 
     * sector it is currently in).
     * 
     * @return the ship's sector position
     */
    public int[] getLocalLocation() {
        int[] out = new int[2];
        out[X] = sectorX;
        out[Y] = sectorY;
        return out;
    }

    /**
     * 
     * Gets which quadrant this ship is 
     * located in currently.
     * 
     * @return the ship's quadrant position
     */
    public int[] getGlobalLocation() {
        int[] out = new int[2];
        out[X] = quadrantX;
        out[Y] = quadrantY;
        return out;
    }

    /**
     * 
     * Makes the ship move based off a warp
     * factor and direction. This uses exact
     * trignonmetry to calculate the precise place
     * the ship will end up. 
     * 
     * @param warpFactor
     * @param warpDirection
     */
    public void move(double warpFactor, double warpDirection) {
        double currentGlobalX = quadrantX * GRID_SIZE + sectorX;
        double currentGlobalY = quadrantY * GRID_SIZE + sectorY;

        // warp speed == total sector distance
        double distanceInSectors = warpFactor * GRID_SIZE;
        
        // convert dirrection to standard radians
        double radians = Math.toRadians(warpDirection);

        // calculate displacement vectors using trignonmetry
        double deltaX = distanceInSectors * Math.cos(radians);
        double deltaY = distanceInSectors * Math.sin(radians);

        // new global positions
        double newGlobalX = currentGlobalX + deltaX;
        double newGlobalY = currentGlobalY + deltaY;

        // edge cases for galaxy boundaries
        newGlobalX = common.MathUtils.clamp(newGlobalX, 0, 63.99);
        newGlobalY = common.MathUtils.clamp(newGlobalY, 0, 63.99);

        // convert to ints
        int newQuadX = (int) (newGlobalX / GRID_SIZE);
        int newQuadY = (int) (newGlobalY / GRID_SIZE);

        int newSectX = (int) (newGlobalX % GRID_SIZE);
        int newSectY = (int) (newGlobalY % GRID_SIZE);

        // assign new values
        quadrantX = newQuadX;
        quadrantY = newQuadY;

        sectorX = newSectX;
        sectorY = newSectY;
    }

    /**
     * 
     * Makes the ship take damage based off 
     * effective phaser energy. 
     * 
     * @param phaserEnergy
     */
    public void takeDamage(double phaserEnergy) {
        shields -= phaserEnergy;
        if (shields <= 0) {
            // destory ship IDK how to handle rn
        }
    }

    /**
     * 
     * Makes the ship fire phasers. This is
     * based off the (x, y) value which is
     * its destination. (Within one quadrant)
     * 
     * Not implemented yet. 
     * 
     * @param phaserEnergy
     * @param x
     * @param y
     */
    public void firePhasers(double phaserEnergy, int x, int y) {

    }

    /**
     * 
     * Gets the shields of the ship and returns it.
     * Only subclasses can access this. 
     * 
     * @return the shields of the ship
     */
    protected double shields() {
        return shields;
    }

    private double shields;
    
    private int sectorX;
    private int sectorY;

    private int quadrantX;
    private int quadrantY;

    private static final int GRID_SIZE = 8;

    public static final int X = 0;
    public static final int Y = 1;
}