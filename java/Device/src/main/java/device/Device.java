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

        assert "[1] Warp Engines: 0.0".equals(d.toString()) : "String conversion function does not work properly";
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
        return "[" + Integer.toString(id) + "] " + name + ": " + Double.toString(damageLevel);
    }

    private double damageLevel;

    private int id;
    private String name;
}

public class Devices {
    Devices() {
        devices = new ArrayList<>();
    }

    Devices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    /**
     * 
     * Adds a device to the ArrayList (devices)
     * 
     * @param id
     * @param name
     * 
     */
    public void addDevice(int id, String name) {
        devices.add(new Device(id, name));
    }

    /**
     * 
     * Removes a device from the ArrayList (devices)
     * 
     * @param id
     * 
     */
    public void removeDevice(int id) {
        devices.remove(id);
    }

    /**
     * 
     * Returns a device from the ArrayList (devices)
     * 
     * @param id
     * 
     */
    public Device getDevice(int id) {
        return devices.get(id);
    }

    /**
     * 
     * Makes a random event occur
     * 
     */
    public void randomDamageRepairEvent() {
        if (devices.size() <= 0)
            return;
        
        if (GameLib.chanceOf(0.8))
            return;

        System.out.println(this.toString());

        int index = GameLib.randomInt(0, devices.size() - 1);
        devices[index].event();

        System.out.println(this.toString());
    }

    /**
     * 
     * Damages the device with id by an amount
     * 
     * @param id
     * @param amount
     * 
     */
    public void damage(int id, double amount) {
        if (devices.size() <= 0 || amount <= 0)
            return;

        System.out.println(this.d);

        int index = GameLib.randomInt(0, devices.size() - 1);
        devices.get(index).damage(amount);

        System.out.println(this.d);
    }

    /**
     * 
     * Repairs the device with id by an amount
     * 
     * @param id
     * @param amount
     * 
     */
    public void repair(int id, double amount) {
        if (devices.size() <= 0 || amount <= 0)
            return;

        System.out.println(this.d);

        int index = GameLib.randomInt(0, devices.size() - 1);
        devices.get(index).repair(amount);
        
        System.out.println(this.d);
    }

    /**
     * 
     * Damages a device (with id) by a random amount (between 1-6)
     * 
     * @param id
     * 
     */
    public void randomDamage(int id) {
        if (devices.size() <= 0)
            return;

        System.out.println(this.d);

        int index = GameLib.randomInt(0, devices.size() - 1);
        devices.get(index).randomDamage();

        System.out.println(this.d);
    }

    /**
     * 
     * Repairs a device (with id) by a random amount (between 1-4)
     * 
     * @param id
     * 
     */
    public void randomRepair(int id) {
        if (devices.size() <= 0)
            return;

        System.out.println(this.d);

        int index = GameLib.randomInt(0, devices.size() - 1);
        devices.get(index).randomRepair();

        System.out.println(this.d);
    }

    /**
     * 
     * Repairs all devices by an amount by the warpFactor
     * 
     * @param warpFactor
     * 
     */
    public void moveRepair(double warpFactor) {
        if (warpFactor > 1.0)
            warpFactor = 1.0;

        System.out.println(this.toString());

        for (Device d : devices) {
            d.repair(warpFactor);
        }

        System.out.println(this.toString());
    }

    /**
     * 
     * Makes the devices take damage, but only if there is enough damage
     * 
     * @param phaserEnergy
     * @param distance
     * @param shields
     * 
     */
    public void takeDamage(double phaserEnergy, double distance, double shields) {
        if (phaserEnergy < 0 || distance < 0 || shields < 0 || devices.size() <= 0)
            return;

        double hitPoints = phaserEnergy / distance;
        if (hitPoints / shields <= 0.02)
            return;

        if (GameLib.chanceOf(0.6)) {
            System.out.println(this.d);

            int index = Gamelib.randomInt(1, devices.size())
            devices.get(index).randomDamage();
        }
        
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        String out = "";

        out += "Damage Report\n";

        for (Device d : devices) {
            out += d.toString() + "\n";
        }

        return out;
    }

    private ArrayList<Device> devices;
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