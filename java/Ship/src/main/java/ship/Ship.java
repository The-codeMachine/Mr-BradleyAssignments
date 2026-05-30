package ship;

import java.util.ArrayList;

import common.*;
import device.*;

/**
 * Base class for all ships.
 *
 * A ship contains:
 *  - shields
 *  - hull health
 *  - location
 *  - devices
 *
 * Supports:
 *  - taking damage
 *  - repairing devices
 *  - docking
 *  - random device events
 *
 */
public class Ship {
    public Ship(double shields, double health, int x, int y) {
        this.shields = shields;
        this.health = health;
        this.x = x;
        this.y = y;

        this.devices = new ArrayList<>();
    }

    /**
     * 
     * Gets the ship's shields
     * 
     */
    public double getShields() {
        return shields;
    }

    /**
     * 
     * Gets the ship's health
     * 
     */
    public double getHealth() {
        return health;
    }

    /**
     * 
     * Gets the ship's x position
     * 
     */
    public int getX() {
        return x;
    }

    /**
     * 
     * Gets the ship's y position
     * 
     */
    public int getY() {
        return y;
    }

    /**
     * 
     * Sets a new position for the ship
     * 
     * @param newX
     * @param newY
     * 
     */
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    /**
     * 
     * Checks if the ship is destroyed
     * 
     */
    public boolean isDestroyed() {
        return health <= 0.0;
    }

    /**
     * 
     * Makes the ship take damage 
     * 
     * @param amount
     * 
     */
    public void takeDamage(double phaserEnergy, double distance) {
        assert phaserEnergy >= 0.0 && distance >= 0.0;

        double amount = phaserEnergy / distance;

        if (shields > 0.0) {
            shields -= amount;

            if (shields < 0.0) {
                health += shields;
                shields = 0.0;
            }
        } else {
            health -= amount;
        }

        if (amount / shields >= 0.02 && devices.size() > 0) {
            if (GameLib.chanceOf(0.4))
                return;

            int index = GameLib.randomInt(0, devices.size() - 1);
            double damageAmount = GameLib.randomInRange(1.0, 5.0);

            devices.get(index).damage(damageAmount);

            return;
        }

        // possible device damage
        if (GameLib.chanceOf(0.6) && devices.size() > 0) {
            // 60% chance that the device gets damaged 
            if (GameLib.chanceOf(0.4))
                return;

            int index = GameLib.randomInt(0, devices.size() - 1);
            double damageAmount = GameLib.randomInRange(1.0, 5.0);

            devices.get(index).damage(damageAmount);
        }
    }

    /**
     * 
     * Repairs all the devices in the ship
     * 
     * @param amount
     * 
     */
    public void repairAllDevices(double amount) {
        for (Device d : devices) {
            d.repair(amount);
        }
    }

    /**
     * 
     * Resets all of the devices on the ship
     * 
     */
    public void resetDevices() {
        for (Device d : devices) {
            d.reset();
        }
    }

    /**
     * 
     * Random event that occurs on the ship
     * 
     */
    public void randomDeviceEvent() {
        if (devices.size() < 1)
            return;

        // 20% chance
        if (GameLib.chanceOf(0.8))
            return;

        int index = GameLib.randomInt(0, devices.size() - 1);

        // 60% damage, 40% repair
        if (GameLib.chanceOf(0.6)) {
            // only 60% chance that the device actually gets damaged
            if (GameLib.chanceOf(0.4))
                return;

            double amount = GameLib.randomInRange(1.0, 5.0);
            devices.get(index).damage(amount);
        } else {
            double amount = GameLib.randomInRange(1.0, 3.0);
            devices.get(index).repair(amount);
        }
    }

    /**
     * 
     * Gets a device from devices by the index
     * 
     * @param index
     * 
     */
    public Device getDevice(int index) {
        assert index >= 0 && index < devices.size();

        return devices.get(index);
    }

    /**
     * 
     * Returns the total number of devices on the ship
     * 
     */
    public int totalDevices() {
        return devices.size();
    }   

    /**
     * 
     * Adds a device to devices (for testing purposes)
     * 
     */
    public void addDevice(Device d) {
        devices.add(d);
    }

    /**
     * 
     * Tests the ship's private functions
     * 
     */
    public static void whiteBoxTest() {
        System.out.println("Ship white box test");

        Ship s = new Ship(100.0, 100.0, 0, 0);

        s.devices.add(new Device(1, "Warp Engines"));
        s.devices.add(new Device(2, "Sensors"));

        s.takeDamage(20.0, 1.0);

        assert s.getShields() == 80.0 : "Shield is not 80 like it is suppose to be";

        s.randomDeviceEvent();
        
        System.out.println(s);

        System.out.println("Ship white box test success");
    }

    @Override
    public String toString() {
        String out = "";

        out += "Health: " + Double.toString(health) + "\n";
        out += "Shields: " + Double.toString(shields) + "\n";
        out += "Position: (" + Integer.toString(x) + ", " + Integer.toString(y) + ") \n";

        out += "Devices\n";

        for (Device d : devices)
        {
            out += "  " + d.toString() + "\n";
        }

        return out;
    }

    private double shields;
    private double health;

    private int x;
    private int y;

    private ArrayList<Device> devices;
}

/**
 * Sample Output
 * 
 * Ship test
 * Getters test
 * Ship constructed with specs
 * Health: 200.0
 * Shields: 100.0
 * Position: (3, 5) 
 * Devices
 *   [1], Warp Engines, Damage: 0.0
 *   [2], Shield Control, Damage: 0.0
 * 
 * Ship shields: 100.000000
 * Ship health: 200.000000
 * Ship position: (3, 5)
 * Ship total devices: 2
 * Getters test success
 * 
 * Damage test
 * Ship after taking 25 damage
 * Health: 200.0
 * Shields: 75.0
 * Position: (0, 0) 
 * Devices
 * 
 * Ship after taking 100 damage
 * Health: 175.0
 * Shields: 0.0
 * Position: (0, 0) 
 * Devices
 * 
 * Damage test success
 * 
 * Ship white box test
 * Health: 100.0
 * Shields: 80.0
 * Position: (0, 0) 
 * Devices
 *   [1], Warp Engines, Damage: -2.149951265549807
 *   [2], Sensors, Damage: 0.0
 * 
 * Ship white box test success
 * Ship test success
 * 
 */