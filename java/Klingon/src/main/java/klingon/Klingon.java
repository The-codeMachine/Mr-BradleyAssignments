package klingon;

import common.GameLib.Location;
import common.GameLib;

import ship.Ship;

/**
 * 
 * The Klingon class extends publicly from Ship. It
 * extends functionality minimally. Fire phasers now
 * reduces the Klingon's energy pool. The Klingon can
 * now also generate a random energy pool. 
 * 
 * This random energy pool is based off the algorithm:
 * 
 * ENERGY = BASE_ENERGY x (0.5 + RND(1))
 * 
 * Where BASE_ENERGY is equal to 200 and RND(1) returns 
 * a random number [0, 1). This allows a range between
 * [100, 300). 
 * 
 */
public class Klingon extends Ship {

    public Klingon() {
        super(generateRandomEnergy());
    }

    public Klingon(Location loc) {
        this(loc, generateRandomEnergy());
    }

    public Klingon(Location loc, int energy) {
        super(energy, loc);
    }

    /**
     * 
     * Calculates the amount of phaser damage to give the Enterprise
     * based off the Enterprise's current location. Will deplete the
     * klingon's energy supply by diving it by (3, 4]. Takes base-0
     * input. 
     * 
     * @param x
     * @param y
     * @return
     */
    public int firePhasers(int x, int y) {
        int energy = energy();
        adjustEnergy((int)(energy / GameLib.randomInRange(3, 4)) - energy);
        
        return super.firePhasers(energy, x, y);
    }

    /**
     * 
     * This moves the Klingon to a new random sector in the Quadrant.
     * This function returns that random sector. This does NOT consume
     * energy, and essentially teleports the Klingon (like the original).
     * 
     * This will not actually move the Klingon, but rather return
     * where it should go.
     * 
     * @return the random sector calculated 
     */
    public Location calculateDestination() {
        Location randomLocation = new Location();
        return new Location(randomLocation.sectorX, randomLocation.sectorY, 
                            getLocation().quadrantX, getLocation().quadrantY);
    }

    /**
     * 
     * Moves the klingon to a new location.
     * 
     * @param loc
     */
    public void move(Location loc) {
        super.move(loc);
    }
    
    /**
     * 
     * Generates a random number to represent the Klingon's base
     * energy. 
     * 
     * @return a random number representing the Klingon's base energy
     */
    private static int generateRandomEnergy() {
        return (int)(BASE_ENERGY + (0.5 * GameLib.random()));
    }

    private static final int BASE_ENERGY = 200;
}
