package enterprise;

import common.GameLib.Location;
import common.GameLib;
import common.IO;
import java.util.ArrayList;

import device.*;
import ship.*;

/**
 * 
 * The Enterprise represents the player's ship. It inherits
 * publicly from the Ship class. This class extends its
 * functionalities by adding:
 * 
 *  - Shields
 *  - Torpedoes
 *  - Docking capabilities
 *  - Device functionality
 * 
 * You can adjust the Enterprise's shields, check whether it 
 * is destroyed, or a device is destroyed. You can calculate
 * its movement path based off warp factor and direction.
 * You can update its dock value, print a damage report, or
 * reduce its torpedoes. 
 * 
 * This functionality is more or less forwarded to the Game
 * class which uses the Enterprise's public functions to 
 * allows the user to do specific commands.  
 * 
 */
public class Enterprise extends Ship {
    public Enterprise(int energy, Location location, double shields, int torpedoes, boolean docked) {
        super(energy, location);

        devices = new Devices();
        this.shields = shields;
        this.torpedoes = torpedoes;
        this.docked = docked;
        this.randomRepairModifier = GameLib.randomInRange(RANDOM_MODIFIER_MIN, RANDOM_MODIFIER_MAX);
    }

    public Enterprise(int energy, double shields, int torpedoes, boolean docked) {
        super(energy);

        devices = new Devices();
        this.shields = shields;
        this.torpedoes = torpedoes;
        this.docked = docked;
        this.randomRepairModifier = GameLib.randomInRange(RANDOM_MODIFIER_MIN, RANDOM_MODIFIER_MAX);
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
            IO.printf("Chief engineer Scott reports \"The engines won't take warp %.3f!\"\n", warpFactor);
            IO.println("Warp engines are damaged. Maximum speed is warp 0.2");
            return new ArrayList<>();
        }

        return super.calculatePath(warpFactor, warpDirection);
    }
    
    /**
     * 
     * Moves the Enterprise and repairs all devices while at it. It 
     * also makes a random event occur to the devices, and consumes
     * energy. 
     * 
     * @param loc
     * @param warpFactor
     */
    @Override
    public void move(Location loc, double warpFactor) {
        int energyUsed = (int) (warpFactor * 8 + 0.5);
        if (energy() < energyUsed) {
            IO.println("Engineering reports: ");
            IO.printf("\"Insufficient energy available for manuvering at warp %.3f!\"", warpFactor);

            // reduces by 10 because of lost between circulation
            energyUsed += 10; 
            if (shields < energyUsed - energy() || devices.isDamaged("SHIELD CONTROL")) {
                IO.println("** Fatal Error **");
                IO.println("You have just stranded your ship in space;");
                IO.println("You have insufficient maneuvering energy,");
                IO.println("and shield control is presently incapable of");
                IO.println("cross-circutting to the engine room!");
                kill();
                return;
            }

            IO.printf("Deflector control room acknowledges %d units of energy are presently deployed to the shields\n", shields);
            // uses the shields to complete navigation
            shields -= energyUsed - energy();
            adjustEnergy(-energy()); // sets energy to 0
            IO.println("Shield control supplies energy to complete the maneuver");

            return;
        }

        adjustEnergy(-energyUsed);
        
        // set a new modifier if the quadrant location is different
        if (!getLocation().sameQuadrant(loc))
            randomRepairModifier = GameLib.randomInRange(RANDOM_MODIFIER_MIN, RANDOM_MODIFIER_MAX);

        devices.repairOverTime(warpFactor);
        devices.randomEvent();
        super.move(loc);
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
        if (docked)
            return isDestroyed();

        devices.hitDamage(phaserEnergy, energy());

        shields -= phaserEnergy;
        return isDestroyed();
    }

    /**
     * 
     * Calculates the amount of damage phasers do to a klingon. 
     * Will reduce accuracy if the COMPUTER_SYSTEMS are broken  
     * (essentially decreases the amount of damage). 
     * 
     * @return the amount of damage the phasers do to a klingon
     */
    public int firePhasers(double phaserEnergy, int x, int y, int numKlingons) {
        double distance = Math.hypot(getLocation().sectorX - x, getLocation().sectorY - y);
        double h = phaserEnergy / numKlingons;

        return isDeviceBroken(Devices.COMPUTER_SYSTEMS) ? 
        (int) ((h / distance) * (GameLib.random() + 2) * GameLib.random()) : 
        (int) ((h / distance) * (GameLib.random() + 2));
    }

    /**
     * 
     * Gets the number of torpedoes the Enterprise currently has
     * 
     * @return the amount of torpedoes the Enterprise has
     */
    public int getTorpedoes() {
        return torpedoes;
    }

    /**
     * 
     * Reduces the torpedoes by one
     * 
     */
    public void reduceTorpedoes() {
        if (torpedoes <= 0)
            return;

        torpedoes--;
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
     * Repairs all devices completely. 
     * 
     */
    public void repairDevices() {
        devices.repairAllDevicesFully();
    }

    /**
     * 
     * Adjusts the shields to the new shields value. Will log
     * an error if there is not sufficient energy. 
     * 
     * @param shields
     */
    public void adjustShields(double shields) {
        if (shields < 0) {
            return;
        }

        IO.println("Shield Control reports: ");
        double diffShields = this.shields - shields;
        if (-diffShields > energy()) {
            IO.printf("Not enough energy to adjust shields to: %d\n", shields);
            IO.println("This is not the Federation treasury");
            return;
        }

        adjustEnergy((int)(diffShields));
        this.shields = shields;
        IO.printf("Shields now at %.3f units per your command\n", this.shields);

    }

    /**
     * 
     * Gets whether a device is broken or not
     * 
     * @param device
     * @return true if the device is broken and false if it is not
     */
    public boolean isDeviceBroken(String device) {
        return devices.isDamaged(device);
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
     * Sets the Enterprise's shields to -1. This essentially 
     * kills the Enterprise and destroys it. 
     * 
     */
    public void kill() {
        shields = -1;
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
        if (shields > 0) {
            IO.println("Shields lowered for docking");
            adjustShields(0);
        }

        adjustEnergy(3000 - energy());
        torpedoes = 10;

        // repairs all devices fully
        devices.repairAllDevicesFully();
    }

    private Devices devices;
    private double randomRepairModifier;

    private double shields;
    private int torpedoes;
    private boolean docked;

    private final double RANDOM_MODIFIER_MIN = 0;
    private final double RANDOM_MODIFIER_MAX = 0.5;
}
