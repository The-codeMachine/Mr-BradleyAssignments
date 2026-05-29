package device;

public class Main {
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
    
    public static void main(String[] args) {
        System.out.println("Device Test");
        
        testGetters();

        Device.whiteBoxTest();

        System.out.println("Device test success");
    }
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