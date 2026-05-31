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

        assert "[1], Warp Engines, Damage: 0.0".equals(d.toString()) : "String conversion function does not work properly";
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
        return "[" + Integer.toString(id) + "], " + name + ", Damage: " + Double.toString(damageLevel);
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
 * Device id: 1
 * Device name: Test Device
 * Device damage: 0
 * Getters test success
 * Device white box test
 * Device is fully working, and not damaged: [1], Warp Engines, Damage: 0.0
 * Device is not operational because it has 2.5 damage: [1], Warp Engines, Damage: -2.5
 * Device has 1.5 damage: [1], Warp Engines, Damage: -1.5
 * Device is repair fully, and operational again: [1], Warp Engines, Damage: 0.0
 * Device white box test success
 * Device test success
 * 
 */