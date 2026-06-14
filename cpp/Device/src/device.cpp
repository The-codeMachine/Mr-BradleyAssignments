#include "Device.hpp"

#include <common/random.hpp>

#include <cassert>
#include <iostream>
#include <iomanip>

Devices::Devices() : devices{} {}

// Returns a random number between 0 and 7 (inclusive)
int Devices::randomDevice()
{
    return common::randomInt(0, 7);
}

// Damages the device (index) by an amount
void Devices::damage(int index, double amount)
{
    assert(index >= 0 && index <= 7 && amount > 0);

    devices[index] -= amount;
}

// Damages the device (index) by an amount
void Devices::repair(int index, double amount)
{
    assert(index >= 0 && index <= 7 && amount > 0);

    devices[index] += amount;

    if (devices[index] > 0.0)
        devices[index] = 0.0;
}

// Damages the device (random device) by a random amount (1 <= x < 6)
void Devices::damage()
{
    int index = randomDevice();

    damage(index, common::randomInRange(1, 6));
}

// Repairs the device (random device) by a random amount (1 <= x < 4)
void Devices::repair()
{
    int index = randomDevice();

    repair(index, common::randomInRange(1, 4));
}

// Repairs all devices by an amount equal to the warp factor
void Devices::moveRepair(double warpFactor)
{
    if (warpFactor > 1.0)
        warpFactor = 1.0;

    repairAllDevices(warpFactor);
}

// Checks if there is enough damage to damage a device,
// and if so then damage one
void Devices::takeDamage(double phaserEnergy, int shields)
{
    if (phaserEnergy <= 10 || phaserEnergy / shields <= 0.02)
        return;

    double damage = phaserEnergy / shields + 0.5;
    this->damage(randomDevice(), damage);
}

// Makes a random device event occur (60%/40% split, 20% chance)
void Devices::randomDamageRepairEvent()
{
    if (common::chanceOf(0.8))
        return;

    // damage
    if (common::chanceOf(0.6))
    {
        damage();
    } // repair
    else
    {
        repair();
    }
}

// Repairs all devices by an amount
void Devices::repairAllDevices(double amount)
{
    for (int i = 0; i < 8; ++i)
    {
        repair(i, amount);
    }
}

// Checks if a device is operational
bool Devices::isOperational(int index) const
{
    if (index < 0 || index > 7)
        return false;

    return devices[index] == 0.0;
}

// Gets the damage a device has
double Devices::getDamage(int index) const
{
    if (index < 0 || index > 7)
        return 1.0; // error

    return devices[index];
}

// Makes a damager report of all the devices
std::string Devices::damageReport() const {
    return "Devices Status Report\n" + toString();
}

std::string Devices::toString() const
{
    std::string out = "";

    out += "WARP ENGINES: " + std::to_string(devices[0]) + "\n";
    out += "SHORT RANGE SENSORS: " + std::to_string(devices[1]) + "\n";
    out += "LONG RANGE SENSORS: " + std::to_string(devices[2]) + "\n";
    out += "PHASER CONTROL: " + std::to_string(devices[3]) + "\n";
    out += "TORPEDO CONTROl: " + std::to_string(devices[4]) + "\n";
    out += "SHIELD CONTROL: " + std::to_string(devices[5]) + "\n";
    out += "DAMAGE CONTROL: " + std::to_string(devices[6]) + "\n";
    out += "COMPUTER SYSTEMS: " + std::to_string(devices[7]) + "\n";

    return out;
}

std::ostream &operator<<(std::ostream &os, const Devices &d)
{
    os << d.toString();

    return os;
}