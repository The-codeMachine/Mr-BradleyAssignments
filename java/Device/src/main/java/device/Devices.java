package device;

import common.*;

/**
 * 
 * The Devices class holds all the information for
 * the devices within the Enterprise. It allows the
 * Enterprise to do certain actions. Each device
 * index correlates to a different device:
 * 0: WARP ENGINES
 * 1: SHORT RANGE SENSORS
 * 2: LONG RANGE SENSORS
 * 3: PHASER CONTROL
 * 4: TORPEDO CONTROL
 * 5: SHIELD CONTROL
 * 6: DAMAGE CONTROL
 * 7: COMPUTER SYSTEMS
 * 
 * Devices' operations include:
 *  - Construction
 *  - Repairs the device by an amount (equal to the warp factor)
 *  - Takes damage (based off a phaser)
 *  - Has a random damage/repair event occur
 *  - Repairs all the devices (for docking)
 *  - Checks if a device is operational
 *  - Prints a status report
 * 
 */
public class Devices {
    Devices() {
        this.devices = new double[8];
    }

    /**
     * 
     * Makes a random device take damage. 40% chance that it
     * actually occurs. [1, 6) damage may occur. 
     * 
     */
    public void takeDamage() {
        if (GameLib.chanceOf(40))
            return;
        
        damage();
    }

    /**
     * 
     * Makes damage occur to all devices over time. 
     * 
     * @param time
     * 
     */
    public void damageOverTime(double time) {
        for (int i = 0; i < devices.length; ++i) {
            damage(i, time);
        }
    }

    /**
     * 
     * Damages a device based off the amount of phaser energy,
     * and shields remaining. 
     * 
     * @param phaserEnergy
     * @param shields
     * 
     */
    public void hitDamage(double phaserEnergy, double shields) {
        if (phaserEnergy <= 20 || phaserEnergy / shields <= 0.02)
            return;

        double damage = phaserEnergy / shields + 0.5 * GameLib.random();
        this.damage(randomDevice(), damage);
    }

    /**
     * 
     * Makes a damage event occur. [1, 6) damage to a 
     * random device. 
     * 
     */
    public void damageEvent() {
        damage();
    }

    /**
     * 
     * Repairs a device by an amount
     * 
     * @param index
     * @param amount
     * 
     */
    public void makeRepair(int index, double amount) {
        repair(index, amount);
    }

    /**
     * 
     * Repairs all the devices by an amount. 
     * 
     * @param amount
     * 
     */
    public void repairAll(double amount) {
        for (int i = 0; i < devices.length; ++i) {
            repair(i, amount);
        }
    }

    /**
     * 
     * Repairs all the devices over a given time.
     * 
     * @param time
     * 
     */
    public void repairOverTime(double time) {
        repairAll(time);
    }

    /**
     * 
     * Makes a repair event occur. [1, 4) repair occurs
     * to a random device. 
     * 
     */
    public void repairEvent() {
        if (!anyDamaged())
            return;

        repair();
    }

    /**
     * 
     * Makes a random damage/repair event occur (60%/40% split).
     * There is a 20% chance of one occurring. Will damage a 
     * random device by [1, 6) or repair a random device by
     * [1, 4).
     * 
     */
    public void randomEvent() {
        if (GameLib.chanceOf(80))
            return;

        if (GameLib.chanceOf(60)) {
            damageEvent();
        } else { 
            repairEvent();
        }
    }

    /**
     * 
     * Damages a device at index by an amonut
     * 
     * @param index
     * @param amount
     * 
     */
    private void damage(int index, double amount) {
        assert isValidIndex(index);
        assert amount > 0;

        devices[index] -= amount;
    }

    /**
     * 
     * Repairs a device at index by an amount
     * 
     * @param index
     * @param amount
     * 
     */
    private void repair(int index, double amount) {
        assert isValidIndex(index);
        assert amount > 0;

        if (!isDamaged(index))
            return;

        devices[index] += amount;

        if (devices[index] > 0.0)
            devices[index] = 0.0;
    }

    /**
     * 
     * Damages a random device by a random amount (between 1 and 6)
     * 
     */
    private void damage() {
        int index = randomDevice();
        damage(index, GameLib.randomInRange(1, 6));
    }

    /**
     * 
     * Repairs a random device by a random amount (between 1 and 4)
     * 
     */
    private void repair() {
        int index = randomDevice();
        repair(index, GameLib.randomInRange(1, 4));
    }

    /**
     * 
     * Returns a number between 0 and 7 representing a random number
     * 
     * @return a random device index
     * 
     */
    private int randomDevice() {
        return GameLib.randomInt(0, 7);
    }

    /**
     * 
     * Checks if the device is damaged
     * 
     * @param index
     * @return true if the device is damaged and false if not
     * 
     */
    public boolean isDamaged(int index) {
        assert isValidIndex(index);

        return devices[index] != 0.0;
    }

    /**
     * 
     * Checks if there are any devices damaged.  
     * 
     * @return true if even one device is damaged
     * 
     */
    private boolean anyDamaged() {
        for (int i = 0; i < devices.length; ++i) {
            if (isDamaged(i))
                return true;
        }

        return false;
    }

    /**
     * 
     * Returns the device's damage level
     * 
     * @param index
     * @return the damage level of the device
     * 
     */
    public double getDamage(int index) {
        assert isValidIndex(index);

        return devices[index];
    }

    /**
     * 
     * Returns a damage report from the devices. 
     * 
     */
    public String damageReport() {
        return "Devices Status Report\n" + toString();
    }

    /**
     * 
     * Checks if the index is valid.
     * 
     * @param index
     * @return true if the index is valid
     * 
     */
    private boolean isValidIndex(int index) {
        return index >= 0 && index <= 7;
    }

    @Override
    public String toString() {
        String out = "";

        out += "WARP ENGINES: " + Double.toString(getDamage(0)) + "\n";
        out += "SHORT RANGE SENSORS: " + Double.toString(getDamage(1)) + "\n";
        out += "LONG RANGE SENSORS: " + Double.toString(getDamage(2)) + "\n";
        out += "PHASER CONTROL: " + Double.toString(getDamage(3)) + "\n";
        out += "TORPEDO CONTROL: " + Double.toString(getDamage(4)) + "\n";
        out += "SHIELD CONTROL: " + Double.toString(getDamage(5)) + "\n";
        out += "DAMAGE CONTROL: " + Double.toString(getDamage(6)) + "\n";
        out += "COMPUTER SYSTEMS: " + Double.toString(getDamage(7)) + "\n";

        return out;
    }

    private double[] devices;
}

/**
 * Sample Output
 * 
 * Devices test
 * Getters test
 * Warp engines damage: 0.000000
 * Warp engines operational status: true
 * Devices Status Report
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * Getters test success
 * Simulation test
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * ...
 * 
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: -0.6
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * Simulation test success
 * Devices test success
 * 
*/