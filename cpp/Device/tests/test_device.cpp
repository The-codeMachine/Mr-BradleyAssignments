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

int main()
{

    std::cout << "Device Test\n";

    testGetters();

    std::cout << "\n";

#ifndef NDEBUG

    Device::whiteBoxTest();

#endif

    std::cout << "Device test success\n";

    return 0;
}

/*
Sample Output

Device Test
Getters test
Device constructed with specs: [1], Test Device, Damage: 0.000000
Device id: 1
Device name: Test Device
Device damage: 0
Getters test success

Device white box test
Device is fully working, and not damaged: [1], Warp Engines, Damage: 0.000000
Device is not operational because it has 2.5 damage: [1], Warp Engines, Damage: -2.500000
Device has 1.5 damage: [1], Warp Engines, Damage: -1.500000
Device is repair fully, and operational again: [1], Warp Engines, Damage: 0.000000
Device white box test success
Device test success

*/