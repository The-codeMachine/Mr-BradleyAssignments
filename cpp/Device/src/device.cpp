#include "Device.hpp"

#include <common/random.hpp>
#include <common/IO.hpp>

#include <cassert>
#include <iostream>
#include <iomanip>
#include <iterator>
#include <cctype>

Devices::Devices() : devices{} {}

// Checks if there are any devices damaged.
bool Devices::anyDamaged() const
{
    for (int i = 0; i < std::size(devices); ++i)
    {
        if (devices[i] == UNDAMAGED)
            continue;

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
bool Devices::isValidAmount(double amount) const
{
    return amount > 0;
}

// Damages the device (index) by an amount
void Devices::damage(int index, double amount)
{
    assert(isValidIndex(index) && isValidAmount(amount));

    devices[index] -= amount;
    common::IO::printf("Damaged %s by: %.3f\n", std::string(getNameByIndex(index)).c_str(), amount);
}

// Damages the device (index) by an amount
void Devices::repair(int index, double amount)
{
    assert(isValidIndex(index) && isValidAmount(amount));

    amount = std::min(-devices[index], amount);

    if (devices[index] == UNDAMAGED)
        return;

    devices[index] += amount;
    common::IO::printf("Repaired %s by: %.3f\n", std::string(getNameByIndex(index)).c_str(), amount);
}

// Repairs the device (random device) by a random amount (1 <= x < 4)
void Devices::repair()
{
    int index = randomDevice();

    repair(index, common::randomInRange(1, 4));
}

// Converts a device name to an index
int Devices::convertToIndex(const std::string_view& deviceName) const {
    return map.at(deviceName);
}

// Converts an index to a device name
std::string_view Devices::getNameByIndex(int index) const {
    assert(isValidIndex(index));
    
    for (const auto& [name, index] : map) {
        if (index == index) 
            return name;
    }

    return "UNKNOWN";
}

// Makes a random device take damage. 60% chance that it will
// actually occur. [1, 6) damage may occur.
void Devices::takeDamage(const std::string_view &deviceName, double amount)
{
    if (common::chanceOf(60))
        damage(convertToIndex(deviceName), amount);
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
    if (phaserEnergy <= 20 || phaserEnergy / shields <= 0.02)
        return;

    if (common::chanceOf(60))
    {
        double damage = phaserEnergy / shields + 0.5 * common::random();
        this->damage(randomDevice(), damage);

        std::cout << damageReport() << "\n";
    }
}

// Makes a damage event occur. [1, 6) damage to a random device.
void Devices::damageEvent()
{
    int index = randomDevice();
    damage(index, common::randomInRange(1, 6));
}

// Repairs a device by an amount
void Devices::makeRepair(const std::string_view &deviceName, double amount)
{
    repair(convertToIndex(deviceName), amount);
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
    if (anyDamaged())
        repair();
}

// Makes a random damage/repair event occur (60%/40% split).
// There is a 20% chance of one occurring. Will damage a
// random device by [1, 6) or repair a random device by
// [1, 4).
void Devices::randomEvent()
{
    if (common::chanceOf(20))
    {
        if (common::chanceOf(60))
        {
            damageEvent();
        }
        else
        {
            repairEvent();
        }
    }
}

// Checks if a device is damaged
bool Devices::isDamaged(const std::string_view &deviceName) const
{
    assert(isValidIndex(convertToIndex(deviceName)));

    return devices[convertToIndex(deviceName)] != UNDAMAGED;
}

// Returns the device's damage level
double Devices::getDamage(const std::string_view &deviceName) const
{
    assert(isValidIndex(convertToIndex(deviceName)));

    return devices[convertToIndex(deviceName)];
}

// Returns a device's status as a string
std::string Devices::getStatus(const std::string_view &deviceName) const
{
    assert(isValidIndex(convertToIndex(deviceName)));

    return std::string(deviceName) + ": " + std::to_string(devices[convertToIndex(deviceName)]);
}

// Gets the number of damaged devices
int Devices::numDamaged() const {
    int out = 0;
    
    for (int i = 0; i < 8; ++i) {
        if (devices[i] < 0)
            out++;
    }

    return out;
}

// Makes a damager report of all the devices
std::string Devices::damageReport() const
{
    return "Devices Status Report\n" + toString();
}

std::string Devices::toString() const
{
    std::string out;

    out += getStatus(WARP_ENGINES) + "\n";
    out += getStatus(SHORT_RANGE_SENSORS) + "\n";
    out += getStatus(LONG_RANGE_SENSORS) + "\n";
    out += getStatus(PHASER_CONTROL) + "\n";
    out += getStatus(TORPEDO_CONTROL) + "\n";
    out += getStatus(SHIELD_CONTROL) + "\n";
    out += getStatus(DAMAGE_CONTROL) + "\n";
    out += getStatus(COMPUTER_SYSTEMS) + "\n";

    return out;
}

std::ostream &operator<<(std::ostream &os, const Devices &d)
{
    os << d.toString();

    return os;
}