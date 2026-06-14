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
     * Repairs all devices by a value equal to the warp factor
     * 
     * @param warpFactor
     * 
     */
    public void moveRepair(double warpFactor) {
        if (warpFactor > 1.0)
            warpFactor = 1.0;

        repairAllDevices(warpFactor);
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
    public void takeDamage(double phaserEnergy, double shields) {
        if (phaserEnergy <= 10 || phaserEnergy / shields <= 0.02)
            return;

        double damage = phaserEnergy / shields + 0.5;
        this.damage(randomDevice(), damage);
    }

    /**
     * 
     * Makes a random damage/repair event occur (60%/40% split).
     * There is a 20% chance of one occurring. Will damage a 
     * random device by (1 to 6) or repair a random device by
     * (1 to 4).
     * 
     */
    public void randomDamageRepairEvent() {
        if (GameLib.chanceOf(0.8))
            return;

        // damage
        if (GameLib.chanceOf(0.6)) {
            damage();
        } // repair
        else {
            repair();
        }
    }

    /**
     * 
     * Repairs all the devices by an amount. 
     * 
     * @param amount
     * 
     */
    public void repairAllDevices(double amount) {
        for (int i = 0; i < 8; ++i) {
            repair(i, amount);
        }
    }

    /**
     * 
     * Checks if a device (index) is operational.
     * 
     * @param index
     * 
     * @return the operation status of a device at index
     * 
     */
    public boolean isOperational(int index) {
        if (index < 0 || index > 7)
            return false;

        return devices[index] == 0.0;
    }

    /**
     * 
     * Gets the damage of a device (index).
     * Returns 1.0 for an error
     * 
     * @param index
     * 
     * @return the damage of a device at index
     * 
     */
    public double getDamage(int index) {
        if (index < 0 || index > 7)
            return 1.0; // error

        return devices[index];
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
        assert index >= 0 && index <= 7 && amount > 0;

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
        assert index >= 0 && index <= 7 && amount > 0;

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
     * Returns a damage report from the devices. 
     * 
     */
    public String damageReport() {
        return "Devices Status Report\n" + toString();
    }

    @Override
    public String toString() {
        String out = "";

        out += "WARP ENGINES: " + Double.toString(devices[0]) + "\n";
        out += "SHORT RANGE SENSORS: " + Double.toString(devices[1]) + "\n";
        out += "LONG RANGE SENSORS: " + Double.toString(devices[2]) + "\n";
        out += "PHASER CONTROL: " + Double.toString(devices[3]) + "\n";
        out += "TORPEDO CONTROL: " + Double.toString(devices[4]) + "\n";
        out += "SHIELD CONTROL: " + Double.toString(devices[5]) + "\n";
        out += "DAMAGE CONTROL: " + Double.toString(devices[6]) + "\n";
        out += "COMPUTER SYSTEMS: " + Double.toString(devices[7]) + "\n";

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