package enterprise;

public class Main {
    // Tests the Enterprise's getters
    public static void testGetters() {
        System.out.println("Getters test");
        Enterprise e = new Enterprise(1000.0, 500.0, 2, 4);
        System.out.println("Enterprise constructed with specs:");
        System.out.println(e.toString());
        
        assert e.getShields() == 1000.0;
        System.out.println("Enterprise shields: " + e.getShields());
        
        assert e.getHealth() == 500.0;
        System.out.println("Enterprise health: " + e.getHealth());
        
        assert e.getEnergy() == 3000;
        System.out.println("Enterprise energy: " + e.getEnergy());
        
        assert e.getTorpedoes() == 10;
        System.out.println("Enterprise torpedoes: " + e.getTorpedoes());
        
        assert e.totalDevices() == 8;
        System.out.println("Enterprise devices: " + e.totalDevices());
        
        System.out.println("Getters test success\n");
    }

    // Tests the Enterprise's movement
    public static void testMovement() {
        System.out.println("Movement test");
        Enterprise e = new Enterprise(1000.0, 500.0, 0, 0);
        e.move(5, 7, 3);
        
        assert e.getX() == 5;
        assert e.getY() == 7;
        System.out.println("Enterprise moved to: (" + e.getX() + ", " + e.getY() + ")");
        System.out.println(e.toString());
        
        System.out.println("Movement test success\n");
    }

    // Tests the Enterprise's weapons
    public static void testWeapons() {
        System.out.println("Weapons test");
        Enterprise e = new Enterprise(1000.0, 500.0, 0, 0);
        
        e.firePhasers(500);
        assert e.getEnergy() == 2500;
        System.out.println("Enterprise fired 500 phaser energy");
        System.out.println("Remaining energy: " + e.getEnergy());
        
        e.fireTorpedo();
        assert e.getTorpedoes() == 9;
        System.out.println("Enterprise fired 1 torpedo");
        System.out.println("Remaining torpedoes: " + e.getTorpedoes());
        
        System.out.println("Weapons test success\n");
    }

    // Tests the docking of the Enterprise
    public static void testDocking() {
        System.out.println("Docking test");
        Enterprise e = new Enterprise(1000.0, 500.0, 0, 0);
        
        e.firePhasers(1000);
        e.fireTorpedo();
        e.getDevice(0).damage(3.0);

        System.out.println("Enterprise before docking: ");
        System.out.println(e);

        e.dock();

        System.out.println("Enterprise after docking: ");
        System.out.println(e);
        
        System.out.println("Docking test execution complete\n");
    }

    public static void main(String args[]) {
        System.out.println("Enterprise test");

        testGetters();
        testMovement();
        testWeapons();
        testDocking();

        Enterprise.whiteBoxTest();
        
        System.out.println("Enterprise test success");
    }

}

/**
 * Sample output
 * 
 * Enterprise test
 * Getters test
 * Enterprise constructed with specs:
 * Health: 500.0
 * Shields: 1000.0
 * Position: (2, 4) 
 * Devices
 *   [1], Warp Engines, Damage: 0.0
 *   [2], Short Range Sensors, Damage: 0.0
 *   [3], Long Range Sensors, Damage: 0.0
 *   [4], Phaser Control, Damage: 0.0
 *   [5], Torpedo Control, Damage: 0.0
 *   [6], Shield Control, Damage: 0.0
 *   [7], Damage Control, Damage: 0.0
 *   [8], Computer Systems, Damage: 0.0
 * Energy: 3000
 * Torpedoes: 10
 * Docked: No
 * 
 * Enterprise shields: 1000.0
 * Enterprise health: 500.0
 * Enterprise energy: 3000
 * Enterprise torpedoes: 10
 * Enterprise devices: 8
 * Getters test success
 * 
 * Movement test
 * Enterprise moved to: (5, 7)
 * Health: 500.0
 * Shields: 1000.0
 * Position: (5, 7) 
 * Devices
 *   [1], Warp Engines, Damage: 0.0
 *   [2], Short Range Sensors, Damage: 0.0
 *   [3], Long Range Sensors, Damage: 0.0
 *   [4], Phaser Control, Damage: 0.0
 *   [5], Torpedo Control, Damage: 0.0
 *   [6], Shield Control, Damage: 0.0
 *   [7], Damage Control, Damage: 0.0
 *   [8], Computer Systems, Damage: 0.0
 * Energy: 2970
 * Torpedoes: 10
 * Docked: No
 * 
 * Movement test success
 * 
 * Weapons test
 * Enterprise fired 500 phaser energy
 * Remaining energy: 2500
 * Enterprise fired 1 torpedo
 * Remaining torpedoes: 9
 * Weapons test success
 * 
 * Docking test
 * Enterprise before docking:
 * Health: 500.000000
 * Shields: 1000.000000
 * Position: (0, 0)
 * Devices
 *   [1], Warp Engines, Damage: -3.000000
 *   [2], Short Range Sensors, Damage: 0.000000
 *   [3], Long Range Sensors, Damage: 0.000000
 *   [4], Phaser Control, Damage: 0.000000
 *   [5], Torpedo Control, Damage: 0.000000
 *   [6], Shield Control, Damage: 0.000000
 *   [7], Damage Control, Damage: 0.000000
 *   [8], Computer Systems, Damage: 0.000000
 * Energy: 2000
 * Torpedoes: 9
 * Docked: No
 * 
 * Enterprise after docking:
 * Health: 500.000000
 * Shields: 1000.000000
 * Position: (0, 0)
 * Devices
 *   [1], Warp Engines, Damage: 0.000000
 *   [2], Short Range Sensors, Damage: 0.000000
 *   [3], Long Range Sensors, Damage: 0.000000
 *   [4], Phaser Control, Damage: 0.000000
 *   [5], Torpedo Control, Damage: 0.000000
 *   [6], Shield Control, Damage: 0.000000
 *   [7], Damage Control, Damage: 0.000000
 *   [8], Computer Systems, Damage: 0.000000
 * Energy: 3000
 * Torpedoes: 10
 * Docked: Yes
 * 
 * Enterprise white box test
 * Health: 1000.0
 * Shields: 1000.0
 * Position: (5, 5) 
 * Devices
 *   [1], Warp Engines, Damage: 0.0
 *   [2], Short Range Sensors, Damage: 0.0
 *   [3], Long Range Sensors, Damage: 0.0
 *   [4], Phaser Control, Damage: 0.0
 *   [5], Torpedo Control, Damage: 0.0
 *   [6], Shield Control, Damage: 0.0
 *   [7], Damage Control, Damage: 0.0
 *   [8], Computer Systems, Damage: 0.0
 * Energy: 3000
 * Torpedoes: 10
 * Docked: Yes
 * 
 * Enterprise white box test success
 * Enterprise test success
 * 
 */