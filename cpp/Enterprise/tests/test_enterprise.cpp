#include "Enterprise.hpp"

#include <cassert>
#include <iostream>

// tests the enterprise's getters
void testGetters()
{
  std::cout << "Getters test\n";

  Enterprise e(1000.0, 500.0, 2, 4);

  std::cout << "Enterprise constructed with specs:\n";
  std::cout << e.toString() << "\n";

  assert(e.getShields() == 1000.0);
  std::cout << "Enterprise shields: "
            << e.getShields() << "\n";

  assert(e.getHealth() == 500.0);
  std::cout << "Enterprise health: "
            << e.getHealth() << "\n";

  assert(e.getEnergy() == 3000);
  std::cout << "Enterprise energy: "
            << e.getEnergy() << "\n";

  assert(e.getTorpedoes() == 10);
  std::cout << "Enterprise torpedoes: "
            << e.getTorpedoes() << "\n";

  assert(e.totalDevices() == 8);
  std::cout << "Enterprise devices: "
            << e.totalDevices() << "\n";

  std::cout << "Getters test success\n";
}

// tests the enterprise's movement
void testMovement()
{
  std::cout << "Movement test\n";

  Enterprise e(1000.0, 500.0, 0, 0);

  e.move(5, 7, 3);

  assert(e.getX() == 5);
  assert(e.getY() == 7);

  std::cout << "Enterprise moved to: ("
            << e.getX() << ", "
            << e.getY() << ")\n";

  std::cout << e.toString() << "\n";

  std::cout << "Movement test success\n";
}

// tests the enterprise's weapons
void testWeapons()
{
  std::cout << "Weapons test\n";

  Enterprise e(1000.0, 500.0, 0, 0);

  e.firePhasers(500);

  assert(e.getEnergy() == 2500);

  std::cout << "Enterprise fired 500 phaser energy\n";
  std::cout << "Remaining energy: "
            << e.getEnergy() << "\n";

  e.fireTorpedo();

  assert(e.getTorpedoes() == 9);

  std::cout << "Enterprise fired 1 torpedo\n";
  std::cout << "Remaining torpedoes: "
            << e.getTorpedoes() << "\n";

  std::cout << "Weapons test success\n";
}

// tests the docking of the enterprise
void testDocking()
{
  std::cout << "Docking test\n";

  Enterprise e(1000.0, 500.0, 0, 0);

  e.firePhasers(1000);
  e.fireTorpedo();

  e.getDevice(0).damage(3.0);

  std::cout << "Enterprise before docking:\n";
  std::cout << e.toString() << "\n";

  e.dock();

  assert(e.getEnergy() == 3000);
  assert(e.getTorpedoes() == 10);
  assert(e.getDevice(0).getDamage() == 0.0);

  std::cout << "Enterprise after docking:\n";
  std::cout << e.toString() << "\n";

  std::cout << "Docking test success\n";
}

int main()
{
  std::cout << "Enterprise Test\n";

  testGetters();

  std::cout << "\n";

  testMovement();

  std::cout << "\n";

  testWeapons();

  std::cout << "\n";

  testDocking();

#ifndef NDEBUG

  std::cout << "\n";

  Enterprise::whiteBoxTest();

#endif

  std::cout << "Enterprise test success\n";

  return 0;
}

/*
Sample Output

Enterprise Test
Getters test
Enterprise constructed with specs:
Health: 500.000000
Shields: 1000.000000
Position: (2, 4)
Devices
  [1], Warp Engines, Damage: 0.000000
  [2], Short Range Sensors, Damage: 0.000000
  [3], Long Range Sensors, Damage: 0.000000
  [4], Phaser Control, Damage: 0.000000
  [5], Torpedo Control, Damage: 0.000000
  [6], Shield Control, Damage: 0.000000
  [7], Damage Control, Damage: 0.000000
  [8], Computer Systems, Damage: 0.000000
Energy: 3000
Torpedoes: 10
Docked: No

Enterprise shields: 1000
Enterprise health: 500
Enterprise energy: 3000
Enterprise torpedoes: 10
Enterprise devices: 8
Getters test success

Movement test
Enterprise moved to: (5, 7)
Health: 500.000000
Shields: 1000.000000
Position: (5, 7)
Devices
  [1], Warp Engines, Damage: 0.000000
  [2], Short Range Sensors, Damage: 0.000000
  [3], Long Range Sensors, Damage: 0.000000
  [4], Phaser Control, Damage: 0.000000
  [5], Torpedo Control, Damage: 0.000000
  [6], Shield Control, Damage: 0.000000
  [7], Damage Control, Damage: 0.000000
  [8], Computer Systems, Damage: 0.000000
Energy: 2970
Torpedoes: 10
Docked: No

Movement test success

Weapons test
Enterprise fired 500 phaser energy
Remaining energy: 2500
Enterprise fired 1 torpedo
Remaining torpedoes: 9
Weapons test success

Docking test
Enterprise before docking:
Health: 500.000000
Shields: 1000.000000
Position: (0, 0)
Devices
  [1], Warp Engines, Damage: -3.000000
  [2], Short Range Sensors, Damage: 0.000000
  [3], Long Range Sensors, Damage: 0.000000
  [4], Phaser Control, Damage: 0.000000
  [5], Torpedo Control, Damage: 0.000000
  [6], Shield Control, Damage: 0.000000
  [7], Damage Control, Damage: 0.000000
  [8], Computer Systems, Damage: 0.000000
Energy: 2000
Torpedoes: 9
Docked: No

Enterprise after docking:
Health: 500.000000
Shields: 1000.000000
Position: (0, 0)
Devices
  [1], Warp Engines, Damage: 0.000000
  [2], Short Range Sensors, Damage: 0.000000
  [3], Long Range Sensors, Damage: 0.000000
  [4], Phaser Control, Damage: 0.000000
  [5], Torpedo Control, Damage: 0.000000
  [6], Shield Control, Damage: 0.000000
  [7], Damage Control, Damage: 0.000000
  [8], Computer Systems, Damage: 0.000000
Energy: 3000
Torpedoes: 10
Docked: Yes

Docking test success

Enterprise white box test
Health: 1000.000000
Shields: 1000.000000
Position: (5, 5)
Devices
  [1], Warp Engines, Damage: 0.000000
  [2], Short Range Sensors, Damage: 0.000000
  [3], Long Range Sensors, Damage: 0.000000
  [4], Phaser Control, Damage: 0.000000
  [5], Torpedo Control, Damage: 0.000000
  [6], Shield Control, Damage: 0.000000
  [7], Damage Control, Damage: 0.000000
  [8], Computer Systems, Damage: 0.000000
Energy: 3000
Torpedoes: 10
Docked: Yes

Enterprise white box test success
Enterprise test success
*/