#pragma once

#include <Device.hpp>

#include <vector>
#include <ostream>

/**
 * Base class for all ships.
 *
 * A ship contains:
 *  - shields
 *  - hull health
 *  - location
 *  - devices
 *
 * Supports:
 *  - taking damage
 *  - repairing devices
 *  - docking
 *  - random device events
 *
 */
class Ship
{
public:
    Ship(double shields, double health, int x, int y);
    virtual ~Ship() = default;

    double getShields() const;
    double getHealth() const;
    int getX() const;
    int getY() const;

    void setPosition(int x, int y);

    bool isDestroyed() const;

    void takeDamage(double amount);

    void repairAllDevices(double amount);
    void resetDevices();

    // 20% chance of an event
    void randomDeviceEvent();

    Device &getDevice(int index);
    const Device &getDevice(int index) const;

    int totalDevices() const;

#ifndef NDEBUG

    void addDevice(const Device& d);

    static void whiteBoxTest();

#endif

    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Ship &s);

protected:
    std::vector<Device> devices;

    double shields;
    double health;

    int x;
    int y;
};

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