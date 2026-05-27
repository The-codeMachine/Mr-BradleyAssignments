package device;

import common.*;

/**
 * 
 * The device holds the functionality to damage, and repair
 * itself. It allows the Enterprise's gameplay mechanics to
 * work (e.g. if the warp engines are broken, then the player
 * cannot use the warping functionality). Each device has its
 * own name, and id. 
 * Operations include:
 *  - Constructing with a specified: id, name
 *  - Constructing with a specified: damage, id, name
 *  - damaging the device
 *  - repairing the device
 *  - damage/repair event (60%/40% chance respectively for each)
 *  - checking if the device is damaged
 * 
 * Damage is a double value so that the device may repair during 
 * warp as necessary. 
 * 
 */

public class Device {

    /**
     * 
     * Creates a new device with a fully working system (damage = 0), and a 
     * custom deviceId, and name.
     * 
     */
    public Device(int assignedDeviceId, String assignedDeviceName) {
        damage = 0;
        deviceId = assignedDeviceId;
        deviceName = assignedDeviceName;
    }

    /**
     * 
     * Creates a device with a specified damage, id, and name.
     * 
     */
    public Device(int assignedDamage, int assignedDeviceId, String assignedDeviceName) {
        damage = assignedDamage;
        deviceId = assignedDeviceId;
        deviceName = assignedDeviceName;
    }

    /**
     * 
     * Makes the device take damage by an amount
     * 
     * @param amount
     * 
     */
    public void takeDamage(double amount) {
        assert amount > 0 : "Amount must be a positive number";

        damage += amount;
    }

    /**
     * 
     * Repairs the device by an amount. Ensures it
     * does not exceed 0
     * 
     * @param amount
     * 
     */
    public void repair(double amount) {
        assert amount > 0 : "Amount must be a positive number";

        damage -= amount;
       
        if (damage < 0) 
            damage = 0;
    }

    /**
     * 
     * Makes the device take damage (60% chance). If it succeeds, 
     * there will be between 1 to 5 damage. 
     * 
     */
    public void damageEvent() {
        if (GameLib.chanceOf(0.6))
            takeDamage(GameLib.randomInt(1, 5));
        
    }

    /**
     * 
     * Makes the device repair (40% chance). If it succeeds then it 
     * repairs the device between 1 to 3 damage. 
     * 
     */
    public void repairEvent() {
        if (GameLib.chanceOf(0.4)) 
            repair(GameLib.randomInt(1, 3));
    }

    /**
     * 
     * Checks if the device is broken
     * 
     */
    public boolean isBroken() {
        return damage != 0.0;
    }

    public static void whiteBoxTest() {
        System.out.println("Starting white box test");

        Device d = new Device(10, 1, "test_device");

        boolean damageOccurred = false;
        boolean repairOccurred = false;

        for (int i = 0; i < 10000; i++) {
            double previousDamage = d.damage;
            d.damageEvent();

            if (d.damage > previousDamage) {
              damageOccurred = true;
                double difference = d.damage - previousDamage;

                assert difference >= 1 && difference <= 5 : "Damage event caused invalid damage amount";
            }

            previousDamage = d.damage;
            d.repairEvent();

            if (d.damage < previousDamage) {
                repairOccurred = true;
                double difference = previousDamage - d.damage;

                assert difference >= 1 && difference <= 3 : "Repair event repaired invalid amount";
            }

            assert d.damage >= 0 : "Damage became negative";

            if (damageOccurred && repairOccurred)
                break;
        }

        assert damageOccurred : "Damage event never triggered";

        assert repairOccurred : "Repair event never triggered";

        System.out.println("White box test passed");
    }

    private double damage;

    private int deviceId;
    private String deviceName;
}

/**
 * Sample Output
 * 
 * Device Test
 * Constructor test
 * Device is created with 0 damage when specified
 * Device is started with the specified damage
 * Constructor test success
 * Repair/Damage test
 * Device got repaired
 * Device got damaged
 * Device got repaired
 * Device got repaired, but is still broken
 * Repair/Damage test success
 * Starting white box test
 * White box test passed
 * Device test success
 * 
 */