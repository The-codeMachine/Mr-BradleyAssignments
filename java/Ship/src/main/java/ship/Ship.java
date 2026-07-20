package ship;

import common.*;
import device.*;

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
 * Base class for all ships.
 *
 * A ship contains:
 *  - shields
 *  - hull health
 *  - location
 *  - devices
 *
 * Supports:
 *  - taking damage
 *  - repairing devices
 *  - docking
 *  - random device events
 *
 */
public class Ship {
    Ship(int shields, int sectorX, int sectorY, int quadrantX, int quadrantY) {
        this.shields = shields;
        
        this.sectorX = sectorX;
        this.sectorY = sectorY;

        this.quadrantX = quadrantX;
        this.quadrantY = quadrantY;
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

    private int shields;
    
    private int sectorX;
    private int sectorY;

    private int quadrantX;
    private int quadrantY;

    private static final int GRID_SIZE = 8;
}