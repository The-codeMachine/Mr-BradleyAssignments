#include "Device.hpp"

#include <common/random.hpp>

#include <cassert>
#include <iostream>
#include <iomanip>
#include <iterator>

Devices::Devices() : devices{} {}

// Checks if there are any devices damaged.
bool Devices::anyDamaged() const
{
    for (int i = 0; i < std::size(devices); ++i)
    {
        if (isDamaged(i))
            return true;
    }

    return false;
}

// Returns a random number between 0 and # devices - 1 (inclusive)
int Devices::randomDevice() const
{
    return common::randomInt(0, std::size(devices) - 1);
}

// Checks if the index is valid.
bool Devices::isValidIndex(int index) const
{
    return index >= 0 && index <= std::size(devices) - 1;
}

// Checks if the amount is a valid amount
bool Devices::isValidAmount(double amount) const {
    return amount > 0;
}

// Damages the device (index) by an amount
void Devices::damage(int index, double amount)
{
    assert(isValidIndex(index) && isValidAmount(amount));

    devices[index] -= amount;
}

// Damages the device (index) by an amount
void Devices::repair(int index, double amount)
{
    assert(isValidIndex(index) && isValidAmount(amount));

    if (!isDamaged(index))
        return;

    devices[index] += amount;

    if (devices[index] > FULLY_REPAIRED)
        devices[index] = FULLY_REPAIRED;
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

// Makes a random device take damage. 60% chance that it will
// actually occur. [1, 6) damage may occur.
void Devices::takeDamage(int index, double amount)
{
    if (common::chanceOf(40))
        return;

    damage(index, amount);
}

// Makes damage occur to all devices over time.
void Devices::damageOverTime(double time)
{
    for (int i = 0; i < std::size(devices); ++i)
    {
        damage(i, time);
    }
}

// Damages a device based off the amount of phaser energy,
// and shields remaining.
void Devices::hitDamage(double phaserEnergy, double shields)
{
    if (phaserEnergy <= 20 || phaserEnergy / shields <= 0.02 || common::chanceOf(40))
        return;

    double damage = phaserEnergy / shields + 0.5 * common::random();
    this->damage(randomDevice(), damage);

    std::cout << damageReport() << "\n";
}

// Makes a damage event occur. [1, 6) damage to a random device.
void Devices::damageEvent()
{
    damage();
}

// Repairs a device by an amount
void Devices::makeRepair(int index, double amount)
{
    repair(index, amount);
}

// Repairs all devices by an amount
void Devices::repairAll(double amount)
{
    for (int i = 0; i < std::size(devices); ++i)
    {
        repair(i, amount);
    }
}

// Repairs all devices over a given time.
void Devices::repairOverTime(double time)
{
    repairAll(time);
}

// Makes a repair event occur. [1, 4) repair occurs to a random
// device.
void Devices::repairEvent()
{
    if (!anyDamaged())
        return;

    repair();
}

// Makes a random damage/repair event occur (60%/40% split).
// There is a 20% chance of one occurring. Will damage a 
// random device by [1, 6) or repair a random device by
// [1, 4).
void Devices::randomEvent()
{
    if (common::chanceOf(80))
        return;

    if (common::chanceOf(60))
    {
        damageEvent();
    }
    else
    {
        repairEvent();
    }
}


bool Devices::isDamaged(int index) const {
    assert(isValidIndex(index));

    return devices[index] != FULLY_REPAIRED;
}

// Returns the device's damage level
double Devices::getDamage(int index) const
{
    assert(isValidIndex(index));

    return devices[index];
}

// Makes a damager report of all the devices
std::string Devices::damageReport() const
{
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