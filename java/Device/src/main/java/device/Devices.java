package device;

import common.*;
import java.util.Map;
import java.util.TreeMap;

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
     * Makes a random device take damage. 60% chance that it
     * actually occurs. [1, 6) damage may occur. 
     * 
     */
    public void takeDamage(String deviceName, double amount) {        
        if (GameLib.chanceOf(60)) {
            damage(convertToIndex(deviceName), amount);
        }
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

        if (GameLib.chanceOf(60)) {
            double damage = phaserEnergy / shields + 0.5 * GameLib.random();
            this.damage(randomDevice(), damage);

            System.out.println(damageReport());
        }
    }

    /**
     * 
     * Damages a random device by a random amount (between 1 and 6)
     * 
     */
    public void damageEvent() {
        int index = randomDevice();
        damage(index, GameLib.randomInRange(1, 6));
    }

    /**
     * 
     * Repairs a device by an amount
     * 
     * @param index
     * @param amount
     * 
     */
    public void makeRepair(String deviceName, double amount) {
        repair(convertToIndex(deviceName), amount);
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
        if (anyDamaged())
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
        if (GameLib.chanceOf(20)) {
            if (GameLib.chanceOf(60)) {
                damageEvent();
            } else { 
                repairEvent();
            }
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
        assert isValidIndex(index) && isValidAmount(amount) : "Invalid index or amount";

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
        assert isValidIndex(index) && isValidAmount(amount) : "Invalid index or amount";

        if (devices[index] == UNDAMAGED)
            return;

        devices[index] += amount;
        
        if (devices[index] > UNDAMAGED)
            devices[index] = UNDAMAGED;
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
     * Returns a random number between 0 and # devices - 1 (inclusive)
     * 
     * @return a random device index
     * 
     */
    private int randomDevice() {
        return GameLib.randomInt(0, devices.length - 1);
    }

    /**
     * 
     * Checks if the device is damaged
     * 
     * @param index
     * @return true if the device is damaged and false if not
     * 
     */
    public boolean isDamaged(String deviceName) {
        assert isValidIndex(convertToIndex(deviceName));

        return devices[convertToIndex(deviceName)] != UNDAMAGED;
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
            if (devices[i] == UNDAMAGED)
                continue;

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
    public double getDamage(String deviceName) {
        assert isValidIndex(convertToIndex(deviceName));

        return devices[convertToIndex(deviceName)];
    }

    /**
     * 
     * Gets the status of the device and returns it as a string
     * 
     * @param deviceName
     * @return a string consisting of the device name and its damage
     */
    public String getStatus(String deviceName) {
        return deviceName + ": " + devices[convertToIndex(deviceName)];
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
        return index >= 0 && index <= devices.length - 1;
    }

    /**
     * 
     * Checks if the amount is valid.
     * 
     * @param amount
     * @return true if the amount is valid
     */
    private boolean isValidAmount(double amount) {
        return amount > 0;
    }

    /**
     * 
     * Returns an index based off the device's name
     * 
     * @param deviceName
     * @return the device's index based off the name
     */
    private int convertToIndex(String deviceName) {
        return map.get(deviceName);
    }

    @Override
    public String toString() {
        String out = "";

        out += getStatus(WARP_ENGINES) + "\n";
        out += getStatus(SHORT_RANGE_SENSORS) + "\n";
        out += getStatus(LONG_RANGE_SENSORS) + "\n";
        out += getStatus(PHASER_CONTROL) + "\n";
        out += getStatus(TORPEDO_CONTROL) + "\n";
        out += getStatus(SHIELD_CONTROL) + "\n";
        out += getStatus(DAMAGE_CONTROL) + "\n";
        out += getStatus(COMPUTER_SYSTEMS) + "\n";

        return out;
    }

    public static final String WARP_ENGINES = "WARP ENGINES";
    public static final String SHORT_RANGE_SENSORS = "SHORT RANGE SENSORS";
    public static final String LONG_RANGE_SENSORS = "LONG RANGE SENSORS";
    public static final String PHASER_CONTROL = "PHASER CONTROL";
    public static final String TORPEDO_CONTROL = "TORPEDO CONTROL";
    public static final String SHIELD_CONTROL = "SHIELD CONTROL";
    public static final String DAMAGE_CONTROL = "DAMAGE CONTROL";
    public static final String COMPUTER_SYSTEMS = "COMPUTER SYSTEMS";

    private double[] devices;
    
    private static final Map<String, Integer> map = Map.of(
        WARP_ENGINES, 0,
        SHORT_RANGE_SENSORS, 1,
        LONG_RANGE_SENSORS, 2,
        PHASER_CONTROL, 3,
        TORPEDO_CONTROL, 4,  
        SHIELD_CONTROL, 5,
        DAMAGE_CONTROL, 6,
        COMPUTER_SYSTEMS, 7
    );
    private static final double UNDAMAGED = 0.0;
}

/**
 * Sample Output
 * 
 * Devices test
 * Getters test
 * Warp engines damage: 0.000000
 * Warp engines damage status: false
 * WARP ENGINES: 0.0
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
 * SHORT RANGE SENSORS: 0.0
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