package enterprise;

import common.GameLib.Location;
import common.IO;
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
 * The Enterprise is constructed using a random
 * location, or a specified location. 
 * 
 */
public class Enterprise extends Ship {
    public Enterprise(int energy, Location location, double shields, int torpedoes, boolean docked) {
        super(energy, location);

        devices = new Devices();
        this.shields = shields;
        this.torpedoes = torpedoes;
        this.docked = docked;
    }

    public Enterprise(int energy, double shields, int torpedoes, boolean docked) {
        super(energy);

        devices = new Devices();
        this.shields = shields;
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
        if (devices.isDamaged(Devices.WARP_ENGINES) && warpFactor > 0.2) {
            IO.println("Warp Engines are damaged, maximum warp is 0.2");
            return new ArrayList<>();
        }

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
    @Override
    public boolean takeDamage(double phaserEnergy) {
        devices.hitDamage(phaserEnergy, super.energy());

        shields -= phaserEnergy;
        return isDestroyed();
    }

    /**
     * 
     * Gets the Enterprise's current docked status
     * 
     * @return true if the Enterprise is currently docked and false if it is not
     */
    public boolean getDocked() {
        return docked;
    }

    /**
     * 
     * Updates the docked value of the Enterprise to value. If it
     * becomes true it will dock the Enterprise. 
     * 
     * @param value
     */
    public void updateDocked(boolean value) {
        docked = value;

        if (docked)
            dock();
    }

    /**
     * 
     * Adjusts the shields to the new shields value. Will log
     * an error if there is not sufficient energy. 
     * 
     * @param shields
     */
    public void adjustShields(double shields) {
        double diffShields = this.shields - shields;
        if (-diffShields > energy()) {
            IO.println("Not enough energy to adjust shields to: " + shields);
            return;
        }
        
        adjustEnergy((int)diffShields);
        this.shields = shields;
    }

    /**
     * 
     * Returns if the Enterprise is destroyed.
     * Will return true if the Enterprise is destoryed.
     * 
     * @return whether the Enterprise is destroyed or not
     */
    @Override
    public boolean isDestroyed() {
        return shields < 0;
    }

    /**
     * 
     * Prints the damage report (which is the status of the
     * devices of the Enterprise).
     * 
     */
    public void damageReport() {
        IO.println(devices.damageReport());
    }

    public String toString() {
        return "Energy: " + energy() +
                "\nLocation: " + getLocation() +
                "\nTorpedoes: " + torpedoes +
                "\nShields: " + shields +
                "\nDocked: " + docked;
    }

    /**
     * 
     * This will dock the Enterprise by replenishing all 
     * its energy, and torpedoes as well as repairing all
     * devices. 
     * 
     */
    private void dock() {
        adjustEnergy(3000 - energy());
        torpedoes = 10;

        // repairs all devices fully
        devices.repairAll(10000);
    }

    private Devices devices;

    private double shields;
    private int torpedoes;
    private boolean docked;
}
