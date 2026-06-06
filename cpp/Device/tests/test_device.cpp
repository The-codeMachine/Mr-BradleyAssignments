#include "Device.hpp"

#include <cassert>
#include <iostream>

// tests the device's getters
void testGetters() {
    std::cout << "Getters test\n";

    Device d(1, "Test Device");

    std::cout << "Device constructed with specs: " << d << "\n";

    assert(d.getId() == 1);
    std::cout << "Device id: " << d.getId() << "\n";

    assert(d.getName() == "Test Device");
    std::cout << "Device name: " << d.getName() << "\n";

    assert(d.getDamage() == 0.0);
    std::cout << "Device damage: " << d.getDamage() << "\n";

    std::cout << "Getters test success\n";
}

// tests the devices' getters
void testDevicesGetters() {
    std::cout << "Devices getters test\n";

    Devices d;

    d.addDevice(1, "Warp Engines");
    d.addDevice(2, "Phaser Control");

    std::cout << d.toString() << "\n";

    assert(d.toString() == "Damage Report\n"
    "[1], Warp Engines, Damage: 0.000000\n"
    "[2], Phaser Control, Damage: 0.000000\n");

    Device device = d.getDevice(0);
    std::cout << device << "\n";

    assert(device.toString() == "[1], Warp Engines, Damage: 0.000000");

    std::cout << "Devices getters test success\n";
}

// tests the damage/repair functions
void testDamageRepair() {
    std::cout << "Damage/Repair test\n";

    Devices d;

    d.addDevice(1, "Warp Engines");
    d.addDevice(2, "Phaser Control");
    
    std::cout << "Damage test\n";
    d.damage(0, 3.0);

    std::cout << "\nRepair test\n";
    d.repair(0, 2.0);

    std::cout << "Damage/Repair test success\n";
}

// tests the random events
void testRandomEvent() {
    std::cout << "Devices random test\n";

    Devices d;

    d.addDevice(1, "Warp Engines");
    d.addDevice(2, "Phaser Control");
    
    std::cout << "Devices random damage\n";
    d.randomDamage(0);

    std::cout << "\nDevices random repair\n";
    d.randomRepair(0);
    
    std::cout << "Devices random test success\n";
}

// tests the ship related device functions
void testShipFunctions() {
    std::cout << "Devices ship test\n";

    Devices d;
    d.addDevice(1, "Warp Engines");
    d.addDevice(2, "Phaser Control");
    
    std::cout << "Damage test\n";
    d.takeDamage(300.0, 1.0, 3000);

    std::cout << "\nDevices random event\n";
    d.randomDamageRepairEvent();

    std::cout << "\nDevices move repair\n";
    d.moveRepair(0.3);

    std::cout << "Devices ship test success\n";
}

int main()
{

    std::cout << "Device Test\n";

    testGetters();

    std::cout << "\n";

#ifndef NDEBUG

    Device::whiteBoxTest();

#endif

    std::cout << "Device test success\n";

    std::cout << "Devices test\n";

    testDevicesGetters();
    testDamageRepair();
    testRandomEvent();
    testShipFunctions();

    std::cout << "Devices test success\n";

    return 0;
}

/*
Sample Output

Getters test
Device constructed with specs: [1], Test Device, Damage: 0.000000
Device id: 1
Device name: Test Device
Device damage: 0
Getters test success

Device white box test
Device is fully working, and not damaged: [1], Warp Engines, Damage: 0.000000
Device might not be operational because it might have taken 2.5 damage (60% chance): [1], Warp Engines, Damage: -2.500000
Device might have (if it got damaged) 1.5 damage: [1], Warp Engines, Damage: -1.500000
Device is repaired fully, and operational again: [1], Warp Engines, Damage: 0.000000
Device white box test success
Device test success
Devices test
Devices getters test
Damage Report
[1], Warp Engines, Damage: 0.000000
[2], Phaser Control, Damage: 0.000000

[1], Warp Engines, Damage: 0.000000
Devices getters test success
Damage/Repair test
Damage test
Damage Report
[1], Warp Engines, Damage: -3.000000
[2], Phaser Control, Damage: 0.000000

Repair test
Damage Report
[1], Warp Engines, Damage: -1.000000
[2], Phaser Control, Damage: 0.000000
Damage/Repair test success
Devices random test
Devices random damage
Damage Report
[1], Warp Engines, Damage: -4.032789
[2], Phaser Control, Damage: 0.000000

Devices random repair
Damage Report
[1], Warp Engines, Damage: -1.944652
[2], Phaser Control, Damage: 0.000000
Devices random test success
Devices ship test
Damage test
Damage Report
[1], Warp Engines, Damage: 0.000000
[2], Phaser Control, Damage: -2.024373

Devices random event

Devices move repair
Damage Report
[1], Warp Engines, Damage: 0.000000
[2], Phaser Control, Damage: -1.724373
Devices ship test success
Devices test success

*/