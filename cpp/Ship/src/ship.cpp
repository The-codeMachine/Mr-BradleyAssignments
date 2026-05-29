#include "Ship.hpp"

#include <common/random.hpp>

#include <cassert>
#include <iostream>

Ship::Ship(double shields, double health, int x, int y)
    : shields(shields), health(health), x(x), y(y) {}

// Gets the ship's shields
double Ship::getShields() const
{
    return shields;
}

// Gets the ship's health
double Ship::getHealth() const
{
    return health;
}

// Gets the ship's x position
int Ship::getX() const
{
    return x;
}

// Gets the ship's y position
int Ship::getY() const
{
    return y;
}

// sets a new ship position
void Ship::setPosition(int x, int y)
{
    this->x = x;
    this->y = y;
}

// checks if the ship is destroyed
bool Ship::isDestroyed() const
{
    return health <= 0.0;
}

// Makes the ship take damage
void Ship::takeDamage(double amount)
{
    assert(amount >= 0.0);

    if (shields > 0.0)
    {
        shields -= amount;

        if (shields < 0.0)
        {
            health += shields;
            shields = 0.0;
        }
    }
    else
    {
        health -= amount;
    }

    // possible device damage (60% chance)
    if (common::chanceOf(0.6) && devices.size() > 0)
    {
        int index = common::randomInt(0, devices.size() - 1);
        double damageAmount = common::randomInRange(1.0, 5.0);

        devices[index].damage(damageAmount);
    }
}

// Repairs all the device on a ship
void Ship::repairAllDevices(double amount)
{
    for (Device &d : devices)
    {
        d.repair(amount);
    }
}

// Resets all the ship's devices
void Ship::resetDevices()
{
    for (Device &d : devices)
    {
        d.reset();
    }
}

// Random event that occurs on a ship
void Ship::randomDeviceEvent()
{
    // 20% chance
    if (common::chanceOf(0.8))
        return;

    int index = common::randomInt(0, devices.size() - 1);

    // 60% damage, 40% repair
    if (common::chanceOf(0.6))
    {
        double amount = common::randomInRange(1.0, 5.0);
        devices[index].damage(amount);
    }
    else
    {
        double amount = common::randomInRange(1.0, 3.0);
        devices[index].repair(amount);
    }
}

// gets a device from devices
Device &Ship::getDevice(int index)
{
    assert(index >= 0 && index < devices.size());

    return devices[index];
}

// gets a device from devices
const Device &Ship::getDevice(int index) const
{
    assert(index >= 0 && index < devices.size());

    return devices[index];
}

// returns the size of devices
int Ship::totalDevices() const
{
    return devices.size();
}

#ifndef NDEBUG

// adds a device to the ship (for testing)
void Ship::addDevice(const Device& d) {
    devices.push_back(d);
}

// tests the ship's private functions
void Ship::whiteBoxTest()
{
    std::cout << "Ship white box test\n";

    Ship s(100.0, 100.0, 0, 0);

    s.devices.push_back(Device(1, "Warp Engines"));
    s.devices.push_back(Device(2, "Sensors"));

    s.takeDamage(20.0);

    assert(s.getShields() == 80.0);

    s.randomDeviceEvent();

    std::cout << s << "\n";

    std::cout << "Ship white box test success\n";
}

#endif

// converts the ship's information to a string
std::string Ship::toString() const
{
    std::string out;

    out += "Health: " + std::to_string(health) + "\n";
    out += "Shields: " + std::to_string(shields) + "\n";
    out += "Position: (" + std::to_string(x) + ", " + std::to_string(y) + ") \n";

    out += "Devices\n";

    for (const Device &d : devices)
    {
        out += "  " + d.toString() + "\n";
    }

    return out;
}

std::ostream &operator<<(std::ostream &os, const Ship &s)
{
    os << s.toString();

    return os;
}