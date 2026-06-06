package device;

import java.util.ArrayList;

import common.*;

/**
 * 
 * The Devices classs manages all of the Devices. It is
 * designed for ships, and the Enterprise. Operations include:
 *  - Construction without an arraylist and with one
 *  - Adding a device
 *  - Removing a device
 *  - Getting a device
 *  - Damaging a device
 *  - Repairing a device 
 *  - Damaging/repairing a random device by a random amount
 *  - Apply the repair for moving
 *  - Have a random damage/repair event occur
 *  - Take damage based off the energy, distance, and shields 
 *  - Convert to string
 * 
 */
public class Devices {
    public Devices() {
        devices = new ArrayList<>();
    }

    public Devices(ArrayList<Device> devices) {
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
    Device getDevice(int id) {
        return devices.get(id);
    }

    /**
     * 
     * Damages the device with id by an amount
     * 
     */
    public void randomDamageRepairEvent() {
        if (devices.size() <= 0 || GameLib.chanceOf(0.8))
            return;

        randomDevice().event();

        System.out.println(this);
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
        if (devices.size() <= 0 || amount <= 0 || devices.size() <= id)
            return;

        devices.get(id).damage(amount);

        System.out.println(this);
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
        if (devices.size() <= 0 || amount <= 0 || devices.size() <= id)
            return;

        devices.get(id).damage(amount);

        System.out.println(this);
    }

    /**
     * 
     * Damages a device (with id) by a random amount (between 1-6)
     * 
     * @param id
     * 
     */
    public void randomDamage(int id) {
        if (devices.size() <= 0 || devices.size() <= id)
            return;

        devices.get(id).damage();

        System.out.println(this);
    }

    /**
     * 
     * Repairs a device (with id) by a random amonut (between 1-4)
     * 
     * @param id
     * 
     */
    public void randomRepair(int id) {
        if (devices.size() <= 0 || devices.size() <= id)
            return;

        devices.get(id).repair();

        System.out.println(this);
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

        for (Device d : devices) {
            d.repair(warpFactor);
        }

        System.out.println(this);
    }

    /**
     * 
     * Makes the devices take damage, but only if there is enough enery
     * 
     * @param phaserEnergy
     * @param double
     * @param shields
     * 
     */
    public void takeDamage(double phaserEnergy, double distance, int shields) {
        if (phaserEnergy < 0 || distance <= 0 || shields < 0 || devices.size() <= 0)
            return;

        double hitPoints = phaserEnergy / distance;
        if (hitPoints <= 20 || hitPoints / shields <= 0.02 || GameLib.chanceOf(0.4))
            return;

        randomDevice().damage();

        System.out.println(this);
    }

    /**
     * 
     * Gets a random device from the devices ArrayList
     * 
     */
    private Device randomDevice() {
        int index = GameLib.randomInt(0, devices.size() - 1);
        return devices.get(index);
    }

    @Override
    public String toString() {
        String out = "Damage Report\n";

        for (Device d : devices) {
            out += d.toString();
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