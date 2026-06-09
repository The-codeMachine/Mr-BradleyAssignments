#include "Device.hpp"

#include <cassert>
#include <iostream>

int main()
{
    std::cout << "Devices test\n";

    std::cout << "Getters test\n";
    
    Devices devices;

    std::cout << "Warp engines damage: " << devices.getDamage(0) << "\n";
    std::cout << "Warp engines operation status: " << devices.isOperational(0) << "\n";

    std::cout << "Getters test success\n";

    std::cout << "Simulation test\n";
    Devices d;
    for (int i = 0; i < 100; ++i) {
        if (i % 25 == 0) {
            d.randomDamageRepairEvent();
            std::cout << d << "\n";
        }

        if (i % 10 == 0) {
            d.moveRepair(1.0);
            std::cout << d << "\n";
        }

        if (i % 15 == 0) {
            d.takeDamage(300, 3000);
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
Warp engines operation status: 1
Getters test success
Simulation test
WARP ENGINES: 0
SHORT RANGE SENSORS: 0
LONG RANGE SENSORS: 0
PHASER CONTROL: 0
TORPEDO CONTROl: 0
SHIELD CONTROL: 0
DAMAGE CONTROL: 0
COMPUTER SYSTEMS: 0
Devices Status Report

WARP ENGINES: 0
SHORT RANGE SENSORS: 0
LONG RANGE SENSORS: 0
PHASER CONTROL: 0
TORPEDO CONTROl: 0
SHIELD CONTROL: 0
DAMAGE CONTROL: 0
COMPUTER SYSTEMS: 0
Devices Status Report

...

WARP ENGINES: 0
SHORT RANGE SENSORS: 0
LONG RANGE SENSORS: 0
PHASER CONTROL: 0
TORPEDO CONTROl: 0
SHIELD CONTROL: 0
DAMAGE CONTROL: 0
COMPUTER SYSTEMS: -0.6
Devices Status Report

Simulation test success
Devices test success

*/