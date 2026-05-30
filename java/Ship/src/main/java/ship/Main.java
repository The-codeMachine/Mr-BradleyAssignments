package ship;

import device.*;

public class Main {
    private static void testGetters() {
        System.out.println("Getters test");

        Ship s = new Ship(100.0, 200.0, 3, 5);

        s.addDevice(new Device(1, "Warp Engines"));
        s.addDevice(new Device(2, "Shield Control"));

        System.out.println("Ship constructed with specs");
        System.out.println(s);

        assert s.getShields() == 100.0 : "Ship did not start with correct shields";
        System.out.printf("Ship shields: %f\n", s.getShields());

        assert s.getHealth() == 200.0 : "Ship did not start with correct health";
        System.out.printf("Ship health: %f\n", s.getHealth());
        
        assert s.getX() == 3 : "Ship is not in correct x position";
        assert s.getY() == 5 : "Ship is not in correct y position";

        System.out.printf("Ship position: (%d, %d)\n", s.getX(), s.getY());

        assert s.totalDevices() == 2 : "Ship does not have correct amount of devices";

        System.out.printf("Ship total devices: %d\n", s.totalDevices());

        System.out.println("Getters test success");
    }

    private static void testDamage() {
        System.out.println("Damage test");

        Ship s = new Ship(100.0, 200.0, 0, 0);

        s.takeDamage(25.0, 1.0);

        assert s.getShields() == 75.0 : "Ship does not have correct shields amount";

        System.out.println("Ship after taking 25 damage");
        System.out.println(s);

        s.takeDamage(100.0, 1.0);

        assert s.getShields() == 0.0 : "Ship does not have correct shields amount";
        assert s.getHealth() == 175.0 : "Ship does not have correct health amount";

        System.out.println("Ship after taking 100 damage");
        System.out.println(s);

        System.out.println("Damage test success");
    }

    public static void main(String args[]) {
        System.out.println("Ship test");

        testGetters();

        System.out.println();

        testDamage();

        System.out.println();

        Ship.whiteBoxTest();

        System.out.println("Ship test success");
    }
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