#include "Enterprise.hpp"

#include <common/random.hpp>

// Generates the super class, and all the devices
Enterprise::Enterprise(double shields, double health, int x, int y) : Ship(shields, health, x, y)
{
    energy = 3000;
    torpedoes = 10;

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

void Enterprise::takeFire(double phaserEnergy, double distance)
{
    double hitDamage = phaserEnergy / distance;
    double hitToShieldRatio = hitDamage / shields;

    if (hitToShieldRatio >= 0.02) {
        Device d = devices[common::randomInt(1, 8)];
        d.takeDamage();
    }
}

// Docks the enterprise and resupplies all of its
// stuff (e.g. energy, torpedoes, repairs all devices)
void Enterprise::dock()
{
    energy = 3000;
    torpedoes = 10;

    // repairs all devices
    for (Device d : devices)
    {
        d.repair(9999);
    }
}

// Gives a 20% chance for an event to occur, if an
// event occurs there is a 60%/40% split for it to 
// be a damage/repair event
void Enterprise::event()
{
    if (common::chanceOf(0.8))
        return;

    if (common::chanceOf(0.6))
    {
        // damage event
        Device d = devices[common::randomInt(1, 8)];
        d.takeDamage();
    }
    else
    {
        // repair event
        Device d = devices[common::randomInt(1, 8)];
        d.repair(common::randomInt(1, 3));
    }
}

// Repairs the devices during warp 
void Enterprise::warpRepair(double warpFactor)
{
    double repairAmount = warpFactor > 1 ? 1 : warpFactor;

    for (Device d : devices) {
        d.repair(repairAmount);
    }
}