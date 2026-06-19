#include "Device.hpp"

#include <cassert>
#include <iostream>

int main()
{
    std::cout << "Devices test\n";

    std::cout << "Getters test\n";
    
    Devices devices;

    std::cout << "Warp engines damage: " << devices.getDamage("WARP ENGINES") << "\n";
    std::cout << "Warp engines damage status: " << devices.isDamaged("WARP ENGINES") << "\n";
    std::cout << devices.getStatus("WARP ENGINES") << "\n";

    std::cout << devices.damageReport() << "\n";

    std::cout << "Getters test success\n";

    std::cout << "Simulation test\n";
    Devices d;
    for (int i = 0; i < 100; ++i) {
        if (i % 25 == 0) {
            d.randomEvent();
            std::cout << d << "\n";
        }

        if (i % 10 == 0) {
            d.repairOverTime(1.0);
            std::cout << d << "\n";
        }

        // phaser hit test
        if (i % 15 == 0) {
            d.hitDamage(300, 3000);
        }

        // docking test
        if (i % 50 == 0) {
            d.repairAll(100.0);
            std::cout << d << "\n";
        }

        if (i % 20 == 0) {
            d.damageOverTime(1.0);
            std::cout << d << "\n";
        }
    }

    std::cout << "Simulation test success\n";

    std::cout << "Devices test success\n";

    return 0;
}

/*
Sample Output

Devices test
Getters test
Warp engines damage: 0
Warp engines damage status: 0
WARP ENGINES: 0.000000
Devices Status Report
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

Getters test success
Simulation test
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

...

Devices Status Report
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: -0.101284
COMPUTER SYSTEMS: 0.000000

Simulation test success
Devices test success

*/