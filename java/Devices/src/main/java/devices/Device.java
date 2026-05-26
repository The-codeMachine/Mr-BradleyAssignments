package Device;

import common.*;

/**
 * 
 * The device is the super of class of all other devices.
 * It holds the damage and repair controls, but gives
 * the actual functionality of the device to the 
 * device subclasses. This helps keep everything
 * organized and safe. 
 * 
 */

public class Device {

    /**
     * 
     * Creates a new device with damage set to 0.
     * 
     */
    public Device() {
        damage = 0;
    }

    /**
     * 
     * Creates a new device with a specified damage.
     * 
     * @param damagee
     * 
     */
    public Device(int damagee) {
        damage = damagee;
    }

    /**
     * 
     * Makes the device take damage (60% chance). If it succeeds, 
     * there will be between 1 to 5 damage. 
     * 
     */
    public void takeDamage() {
        if (GameLib.chanceOf(0.6)) {
            damage -= randomInt(1, 5);
        }
    }

    /**
     * 
     * Makes the device repair (40% chance). If it succeeds then it 
     * repairs the device between 1 to 3 damage. 
     * 
     */
    public void repair() {
        if (GameLib.chanceOf(0.4)) {
            damage += randomInt(1, 3);
        }
    }

    private int damage;
}

public class WarpEngine : Device {
    
}



