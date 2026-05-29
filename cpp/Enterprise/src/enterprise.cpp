#include "Enterprise.hpp"

#include <cassert>
#include <iostream>

Enterprise::Enterprise(double shields, double health, int x, int y)
    : Ship(shields, health, x, y), energy(3000), torpedoes(10), docked(false)
{
    devices.reserve(8);

    devices.push_back(Device(1, "Warp Engines"));
    devices.push_back(Device(2, "Short Range Sensors"));
    devices.push_back(Device(3, "Long Range Sensors"));
    devices.push_back(Device(4, "Phaser Control"));
    devices.push_back(Device(5, "Torpedo Control"));
    devices.push_back(Device(6, "Shield Control"));
    devices.push_back(Device(7, "Damage Control"));
    devices.push_back(Device(8, "Computer Systems"));
}

// get's the enterprise's energy
int Enterprise::getEnergy() const
{
    return energy;
}

// get's the enterprise's torpedoes
int Enterprise::getTorpedoes() const
{
    return torpedoes;
}

// get's the enterprise's dock status
bool Enterprise::isDocked() const
{
    return docked;
}

// moves the enterprise
void Enterprise::move(int newX, int newY, int warpFactor)
{
    assert(warpFactor >= 0);

    x = newX;
    y = newY;

    energy -= warpFactor * 10;

    // travelling repairs devices
    repairAllDevices(warpFactor);

    randomDeviceEvent();
}

// docks the enterprise
void Enterprise::dock()
{
    docked = true;

    energy = 3000;
    torpedoes = 10;

    resetDevices();
}

// fires the enterprise's phaser (does not actually deal damage yet)
void Enterprise::firePhasers(int amount)
{
    assert(amount >= 0);
    assert(amount <= energy);

    energy -= amount;
}

// fires the enterprise's torpedoes (does not actually do damage yet)
void Enterprise::fireTorpedo()
{
    assert(torpedoes > 0);

    torpedoes--;
}

// converts the enterprise's information to a string
std::string Enterprise::toString() const {
    std::string out;

    out += static_cast<const Ship&>(*this).toString();

    out += "Energy: " + std::to_string(energy) + "\n";
    out += "Torpedoes: " + std::to_string(torpedoes) + "\n";
    out += "Docked: " + std::string(docked ? "Yes" : "No") + "\n";

    return out;
}

#ifndef NDEBUG

// tests the enterprise's private functions
void Enterprise::whiteBoxTest()
{
    std::cout << "Enterprise white box test\n";

    Enterprise e(1000.0, 1000.0, 0, 0);

    assert(e.getEnergy() == 3000);
    assert(e.getTorpedoes() == 10);

    e.firePhasers(500);

    assert(e.getEnergy() == 2500);

    e.fireTorpedo();

    assert(e.getTorpedoes() == 9);

    e.move(5, 5, 3);

    assert(e.getX() == 5);
    assert(e.getY() == 5);

    e.dock();

    assert(e.getEnergy() == 3000);
    assert(e.getTorpedoes() == 10);

    std::cout << e << "\n";

    std::cout << "Enterprise white box test success\n";
}

#endif

std::ostream& operator<<(std::ostream& os, const Enterprise& e)
{
    os << e.toString();

    return os;
}