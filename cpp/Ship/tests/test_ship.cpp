#include "Ship.hpp"

#include <cassert>
#include <iostream>

void testGetters()
{
    std::cout << "Getters test\n";

    Ship s(100.0, 200.0, 3, 5);

#ifndef NDEBUG
    // manually add devices for testing
    s.addDevice(Device(1, "Warp Engines"));
    s.addDevice(Device(2, "Shield Control"));
#endif

    std::cout << "Ship constructed with specs:\n";
    std::cout << s.toString() << "\n";

    assert(s.getShields() == 100.0);
    std::cout << "Ship shields: " << s.getShields() << "\n";

    assert(s.getHealth() == 200.0);
    std::cout << "Ship health: " << s.getHealth() << "\n";

    assert(s.getX() == 3);
    assert(s.getY() == 5);

    std::cout << "Ship position: ("
              << s.getX() << ", "
              << s.getY() << ")\n";

#ifndef NDEBUG
    assert(s.totalDevices() == 2);

#endif

    std::cout << "Ship total devices: "
              << s.totalDevices() << "\n";

    std::cout << "Getters test success\n";
}

void testDamage()
{
    std::cout << "Damage test\n";

    Ship s(100.0, 200.0, 0, 0);

    s.takeDamage(25.0);

    assert(s.getShields() == 75.0);

    std::cout << "Ship after taking 25 damage:\n";
    std::cout << s.toString() << "\n";

    s.takeDamage(100.0);

    assert(s.getShields() == 0.0);
    assert(s.getHealth() == 175.0);

    std::cout << "Ship after taking 100 damage:\n";
    std::cout << s.toString() << "\n";

    std::cout << "Damage test success\n";
}

int main()
{
    std::cout << "Ship Test\n";

    testGetters();

    std::cout << "\n";

    testDamage();

#ifndef NDEBUG

    std::cout << "\n";

    Ship::whiteBoxTest();

#endif

    std::cout << "Ship test success\n";

    return 0;
}

/*
Sample Output

Ship Test
Getters test
Ship constructed with specs:
Health: 200.000000
Shields: 100.000000
Position: (3, 5) 
Devices
  [1], Warp Engines, Damage: 0.000000
  [2], Shield Control, Damage: 0.000000

Ship shields: 100
Ship health: 200
Ship position: (3, 5)
Ship total devices: 2
Getters test success

Damage test
Ship after taking 25 damage:
Health: 200.000000
Shields: 75.000000
Position: (0, 0) 
Devices

Ship after taking 100 damage:
Health: 175.000000
Shields: 0.000000
Position: (0, 0) 
Devices

Damage test success

Ship white box test
Health: 100.000000
Shields: 80.000000
Position: (0, 0) 
Devices
  [1], Warp Engines, Damage: -1.950934
  [2], Sensors, Damage: 0.000000

Ship white box test success
Ship test success
*/