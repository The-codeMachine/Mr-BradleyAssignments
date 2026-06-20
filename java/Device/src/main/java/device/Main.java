package device;

public class Main {
    public static void main(String[] args) {
        System.out.println("Devices test");

        System.out.println("Getters test");

        Devices devices = new Devices();

        System.out.printf("Warp engines damage: %f\n", devices.getDamage("WARP ENGINES"));
        System.out.printf("Warp engines damage status: %b\n", devices.isDamaged("WARP ENGINES"));
        System.out.println(devices.getStatus("WARP ENGINES"));
        
        System.out.println(devices.damageReport());

        System.out.println("Getters test success");

        System.out.println("Simulation test");

        Devices d = new Devices();
        for (int i = 0; i < 100; ++i) {
            if (i % 25 == 0) {
                d.randomEvent();
                System.out.println(d);
            }

            if (i % 10 == 0) {
                d.repairOverTime(1.0);
                System.out.println(d);
            }

            // phaser hit test
            if (i % 15 == 0) {
                d.hitDamage(300, 3000);
            }

            // docking test
            if (i % 50 == 0) {
                d.repairAll(100.0);
                System.out.println(d);
            }


            if (i % 20 == 0) {
                d.damageOverTime(1.0);
                System.out.println(d);
            }
        }

        System.out.println("Simulation test success");

        System.out.println("Devices test success");
    }
}

/**
 * Sample Output
 * 
 * Devices test
 * Getters test
 * Warp engines damage: 0.000000
 * Warp engines damage status: false
 * WARP ENGINES: 0.0
 * Devices Status Report
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * Getters test success
 * Simulation test
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * ...
 * 
 * WARP ENGINES: 0.0
 * SHORT RANGE SENSORS: 0.0
 * LONG RANGE SENSORS: 0.0
 * PHASER CONTROL: 0.0
 * TORPEDO CONTROL: 0.0
 * SHIELD CONTROL: 0.0
 * DAMAGE CONTROL: 0.0
 * COMPUTER SYSTEMS: 0.0
 * 
 * Simulation test success
 * Devices test success
 * 
*/