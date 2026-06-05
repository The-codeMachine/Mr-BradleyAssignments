package device;

public class Main {

    // tests the device's getters
    private static void testGetters() {
        System.out.println("Getters test");

        Device d = new Device(1, "Test Device");

        System.out.println("Device constructed with specs: " + d.toString());

        assert d.getId() == 1 : "Device has not constructed with id == 1";
        System.out.printf("Device id: %d \n", d.getId());

        assert d.getName().equals("Test Device") : "Device has not constructed with name == \"Test Device\"";
        System.out.printf("Device id: %s \n", d.getName());

        assert d.getDamage() == 0.0 : "Device has not constructed with damage == 0.0";
        System.out.printf("Device id: %f \n", d.getDamage());

        System.out.println("Getters test success");
    }

    // tests the devices' getters
    private static void testDevicesGetters() {
        System.out.println("Devices getters test");

        Devices d = new Devices();

        d.addDevice(1, "Warp Engines");
        d.addDevice(2, "Phaser Control");

        System.out.println(d.toString());

        assert d.toString().equals("""
        Damage Report
        [1], Warp Engines, Damage: 0.0
        [2], Phaser Control, Damage: 0.0
        """) : "Devices toString function does not work properly";

        Device device = d.getDevice(0);
        System.out.println(device.toString());
                                         
        assert device.toString().equals("[1], Warp Engines, Damage: 0.0\n") : "Device toString does not work properly";

        System.out.println("Devices getters test success");
    }
    
    // tests the damage/repair functions
    private static void testDamageRepair() {
        System.out.println("Damage/Repair test");
        
        Devices d = new Devices();

        d.addDevice(1, "Warp Engines");
        d.addDevice(2, "Phaser Control");
        
        System.out.println("Damage test");
        d.damage(0, 3.0);

        System.out.println("\nRepair test");
        d.repair(0, 2.0);   

        System.out.println("Damage/Repair test success");
    }

    // tests the random events
    private static void testRandomEvents() {
        System.out.println("Devices random test");

        Devices d = new Devices();

        d.addDevice(1, "Warp Engines");
        d.addDevice(2, "Phaser Control");
        
        System.out.println("Devices random damage");
        d.randomDamage(0);

        System.out.println("\nDevices random repair");
        d.randomRepair(0);

        System.out.println("Devices random test success");
    }

    // tests the ship related device functions
    private static void testShipFunctions() {
        System.out.println("Devices ship test");

        Devices d = new Devices();
        d.addDevice(1, "Warp Engines");
        d.addDevice(2, "Phaser Control");
        
        System.out.println("Damage test");
        d.takeDamage(300.0, 1.0, 3000);

        System.out.println("\nDevices random event");
        d.randomDamageRepairEvent();

        System.out.println("\nDevices move repair");
        d.moveRepair(0.3);

        System.out.println("Devices ship test success");
    }

    public static void main(String[] args) {
        System.out.println("Device Test");
        
        testGetters();

        Device.whiteBoxTest();

        System.out.println("Device test success");

        System.out.println("Devices test");

        testDevicesGetters();
        testDamageRepair();
        testRandomEvents();
        testShipFunctions();

        System.out.println("Devices test success");
    }
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