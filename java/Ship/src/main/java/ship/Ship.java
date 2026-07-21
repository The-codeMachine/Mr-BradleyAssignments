package ship;

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
        
        this.sectorX = common.GameLib.toBase0(sectorX);
        this.sectorY = common.GameLib.toBase0(sectorY);

        this.quadrantX = common.GameLib.toBase0(quadrantX);
        this.quadrantY = common.GameLib.toBase0(quadrantY);
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
        out[X] = common.GameLib.toBase1(sectorX);
        out[Y] = common.GameLib.toBase1(sectorY);

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
        out[X] = common.GameLib.toBase1(quadrantX);
        out[Y] = common.GameLib.toBase1(quadrantY);

        return out;
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
     * @param warpFactor
     * @param warpDirection
     */
    public void move(double warpFactor, double warpDirection) {
        if (warpFactor > 10.0)
            warpFactor = 10.0;

        double currentGlobalX = quadrantX * GRID_SIZE + sectorX;
        double currentGlobalY = quadrantY * GRID_SIZE + sectorY;

        // warp speed == total sector distance
        double distanceInSectors = warpFactor * GRID_SIZE;
        
        // converts to correct orientation, and warpDirection is correct (from base-1 to base-0)
        double angleDegrees = 90.0 - (warpDirection - 1.0) * 45.0;
        // convert dirrection to standard radians
        double radians = Math.toRadians(angleDegrees);

        // calculate displacement vectors using trignonmetry
        double deltaX = distanceInSectors * Math.cos(radians);
        double deltaY = distanceInSectors * Math.sin(radians);

        // new global positions
        double newGlobalX = currentGlobalX + deltaX;
        double newGlobalY = currentGlobalY + deltaY;

        // edge cases for galaxy boundaries
        newGlobalX = common.MathUtils.clamp(newGlobalX, 0.0, 63.99);
        newGlobalY = common.MathUtils.clamp(newGlobalY, 0.0, 63.99);

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
        double distance = Math.sqrt(Math.pow(sectorX - x, 2) + Math.pow(sectorY - y, 2));
        double h = phaserEnergy / numKlingons;

        return (int)((h / distance) * (common.GameLib.random() + 2));
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