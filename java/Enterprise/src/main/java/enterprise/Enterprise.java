package enterprise;

import common.GameLib.Location;
import java.util.ArrayList;

import device.*;
import ship.*;

/**
 * The Enterprise represents the player's
 * ship. It can do everything other ships
 * can, and more. This includes:
 * - Moving
 * - Firing phasers
 * - Firing torpedoes
 * - Docking
 * 
 * If the Enterprise is destroyed, the game
 * ends for the player.
 * 
 * If a certain device is destroyed, then that
 * specific game mechanic is unavailable for the
 * player.
 * 
 */
public class Enterprise extends Ship {
    public Enterprise(double shields, int sectorX, int sectorY, 
        int quadrantX, int quadrantY, int energy, int torpedoes, boolean docked) {
        super(shields, sectorX, sectorY, quadrantX, quadrantY);

        devices = new Devices();
        this.energy = energy;
        this.torpedoes = torpedoes;
        this.docked = docked;
    }

    /**
     * 
     * Makes the Enterprise move based off warpFactor
     * and warpDirection, but double checks that the
     * warp engines are still capable. This still allows
     * the user to use impulse engines if the warp
     * engines are offline. 
     * 
     * @param warpFactor
     * @param warpDirection
     */
    public ArrayList<Location> calculatePath(double warpFactor, double warpDirection) {
        if (devices.isDamaged(Devices.WARP_ENGINES) && warpFactor >= 1.0)
            return new ArrayList<>();

        return super.calculatePath(warpFactor, warpDirection);
    }

    /**
     * 
     * Makes the Enterprise take damage based off
     * the effective phaser energy.
     * 
     * @param phaserEnergy
     * 
     */
    public boolean takeDamage(double phaserEnergy) {
        devices.hitDamage(phaserEnergy, super.shields());
        return super.takeDamage(phaserEnergy);
    }

    /**
     * 
     * Returns if the Enterprise is destroyed.
     * Will return true if the Enterprise is destoryed.
     * 
     * @return whether the Enterprise is destroyed or not
     */
    public boolean isDestroyed() {
        return shields() <= 0;
    }

    private Devices devices;

    private int energy;
    private int torpedoes;
    private boolean docked;
}
