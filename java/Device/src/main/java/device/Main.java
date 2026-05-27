package device;

public class Main {
    private static void testConstructors() {
        System.out.println("Constructor test");

        Device d = new Device(1, "cool_name");
        assert !d.isBroken() : "Device started without the correct damage";
        System.out.println("Device is created with 0 damage when specified");

        Device dd = new Device(10, 1, "cooler_name");
        assert dd.isBroken() : "Device started without the specified damage";
        System.out.println("Device is started with the specified damage");
    
        System.out.println("Constructor test success");
    }

    private static void testRepairDamage() {
        System.out.println("Repair/Damage test");

        Device d = new Device(10, 1, "cool_name");
        d.repair(10);
        assert !d.isBroken() : "Device did not get repaired";
        System.out.println("Device got repaired");

        d.takeDamage(5);
        assert d.isBroken() : "Device did not get damaged";
        System.out.println("Device got damaged");

        d.repair(50);
        assert !d.isBroken() : "Device did not get repaired correctly";
        System.out.println("Device got repaired");

        d.takeDamage(100);
        d.repair(50);
        assert d.isBroken() : "Device should still be broken";
        System.out.println("Device got repaired, but is still broken");

        System.out.println("Repair/Damage test success");
    }
    
    public static void main(String[] args) {
        System.out.println("Device Test");
        
        testConstructors();
        testRepairDamage();

        Device.whiteBoxTest();

        System.out.println("Device test success");
    }
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