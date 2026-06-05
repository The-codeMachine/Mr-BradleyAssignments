package device;

import common.*;

/**
 * A device represents one of the Enterprise's ship systems.
 *
 * Rules:
 *  - Damage is represented as a NEGATIVE value.
 *  - 0.0 means fully operational.
 *  - The more negative the value, the more damaged the device is.
 *
 * Operations include:
 *  - damaging the device
 *  - repairing the device
 *  - checking operational status
 *  - printing device information
 *  - random event (damage, or repair)
 *
 */
public class Device {
    public Device() {
        id = 0;
        name = "UNKNOWN";
        damageLevel = 0.0;
    }

    public Device(int id, String name) {
        this.id = id;
        this.name = name;
        damageLevel = 0.0;
    }

    /**
     * 
     * Gets the device's id
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * Gets the device's name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * Gets the device's damage
     * 
     */
    public double getDamage() {
        return damageLevel;
    }

    /**
     * 
     * Checks if the device is operational
     * 
     */
    public boolean isOperational() {
        return damageLevel == 0.0;
    }

    /**
     * 
     * Damages the device by an amount
     * 
     * @param amount
     * 
     */
    public void damage(double amount) {   
        if (GameLib.chanceOf(0.4))
            return;

        assert amount >= 0.0;

        damageLevel -= amount;
    }

    /**
     * 
     * Repairs the device by an amount
     * 
     * @param amount
     * 
     */
    public void repair(double amount) {
        assert amount >= 0.0;

        damageLevel += amount;
        if (damageLevel > 0.0)
            damageLevel = 0;
    }

    /**
     * 
     * Damages the device by a random amount (between 1 - 5.99..)
     * 
     */
    public void damage() {
        double amount = GameLib.randomInRange(1, 5);
        damage(amount);
    }

    /**
     * 
     * Repairs the device by a random amount (between 1 - 3.99..)
     * 
     */
    public void repair() {
        double amount = GameLib.randomInRange(1, 3);
        repair(amount);
    }

    /**
     * 
     * Make a random event occur (60/40 split between damage/repair)
     * 
     */
    public void event() {
        // 60% damage, 40% repair
        if (GameLib.chanceOf(0.6)) {
            double amount = GameLib.randomInRange(1, 5);
            damage(amount);
        } else {
            double amount = GameLib.randomInRange(1, 3);
            repair(amount);
        }
    }

    /**
     * 
     * Resets the device's damage level
     * 
     */
    public void reset() {
        damageLevel = 0.0;
    }

    /**
     * 
     * Checks that all of the functions work properly
     * 
     */
    public static void whiteBoxTest() {
        System.out.println("Device white box test");

        Device d = new Device(1, "Warp Engines");

        assert "[1], Warp Engines, Damage: 0.0\n".equals(d.toString()) : "String conversion function does not work properly";
        assert d.isOperational() : "Device did not start operational";
        assert d.getDamage() == 0.0 : "Device did not begin with 0.0 damage";

        System.out.println("Device is fully working, and not damaged: " + d.toString());

        d.damage(2.5);

        System.out.println("Device might not be operational because it might have taken 2.5 damage (60% chance): " + d.toString());

        d.repair(1.0);

        System.out.println("Device might have (if it got damaged) 1.5 damage: " + d.toString());

        d.repair(100.0);

        assert d.getDamage() == 0.0 : "Device did not repair correctly";
        assert d.isOperational() : "Device is not operational after being fully repaired";

        System.out.println("Device is repaired fully, and operationl again: " + d.toString());

        System.out.println("Device white box test success");
    }

    @Override
    public String toString() {
        return "[" + Integer.toString(id) + "], " + name + ", Damage: " + Double.toString(damageLevel) + "\n";
    }

    private double damageLevel;

    private int id;
    private String name;
}

/**
 * Sample Output
 * 
 * Device Test
 * Getters test
 * Device constructed with specs: [1], Test Device, Damage: 0.0
 * 
 * Device id: 1 
 * Device id: Test Device 
 * Device id: 0.000000 
 * Getters test success
 * Device white box test
 * Device is fully working, and not damaged: [1], Warp Engines, Damage: 0.0
 * 
 * Device might not be operational because it might have taken 2.5 damage (60% chance): [1], Warp Engines, Damage: -2.5
 * 
 * Device might have (if it got damaged) 1.5 damage: [1], Warp Engines, Damage: -1.5
 * 
 * Device is repaired fully, and operationl again: [1], Warp Engines, Damage: 0.0
 * 
 * Device white box test success
 * Device test success
 * Devices test
 * Devices getters test
 * Damage Report
 * [1], Warp Engines, Damage: 0.0
 * [2], Phaser Control, Damage: 0.0
 * 
 * [1], Warp Engines, Damage: 0.0
 * 
 * Devices getters test success
 * Damage/Repair test
 * Damage test
 * Damage Report
 * [1], Warp Engines, Damage: -3.0
 * [2], Phaser Control, Damage: 0.0
 * 
 * 
 * Repair test
 * Damage Report
 * [1], Warp Engines, Damage: -3.0
 * [2], Phaser Control, Damage: 0.0
 * 
 * Damage/Repair test success
 * Devices random test
 * Devices random damage
 * Damage Report
 * [1], Warp Engines, Damage: -3.272119124617654
 * [2], Phaser Control, Damage: 0.0
 * 
 * 
 * Devices random repair
 * Damage Report
 * [1], Warp Engines, Damage: -1.9403896280683515
 * [2], Phaser Control, Damage: 0.0
 * 
 * Devices random test success
 * Devices ship test
 * Damage test
 * Damage Report
 * [1], Warp Engines, Damage: 0.0
 * [2], Phaser Control, Damage: -3.91346542053011
 * 
 * 
 * Devices random event
 * 
 * Devices move repair
 * Damage Report
 * [1], Warp Engines, Damage: 0.0
 * [2], Phaser Control, Damage: -3.61346542053011
 * 
 * Devices ship test success
 * Devices test success
 * 
 */