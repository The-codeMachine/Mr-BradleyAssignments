#pragma once

#include <Ship.hpp>

/**
 * The Enterprise is the player's ship.
 *
 * Additional functionality:
 *  - energy
 *  - torpedoes
 *  - docking
 *  - movement
 *
 */
class Enterprise : public Ship
{
public:
    Enterprise(double shields, double health, int x, int y);

    int getEnergy() const;
    int getTorpedoes() const;

    bool isDocked() const;

    void move(int newX, int newY, int warpFactor);

    void takeDamage(double phaserEnergy, double distance) override;

    void dock();

    void firePhasers(int energy);
    void fireTorpedo();

#ifndef NDEBUG

    static void whiteBoxTest();

#endif

    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Enterprise &e);

private:
    int energy;
    int torpedoes;

    bool docked;
};

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