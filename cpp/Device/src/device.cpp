#include "Device.hpp"

#include <common/random.hpp>

#include <cassert>
#include <iostream>
#include <iomanip>

Device::Device() : id(0), name("UNKNOWN"), damageLevel(0.0) {}

Device::Device(int id, const std::string &name) : id(id), name(name), damageLevel(0.0) {}

// gets the device's id
int Device::getId() const
{
    return id;
}

// gets the device's name
const std::string &Device::getName() const
{
    return name;
}

// gets the device's damage
double Device::getDamage() const
{
    return damageLevel;
}

// checks if the device is operational
bool Device::isOperational() const
{
    return damageLevel == 0.0;
}

// damages the device by an amount
void Device::damage(double amount)
{
    if (common::chanceOf(0.4))
    return;
    
    assert(amount >= 0.0);
    
    damageLevel -= amount;
}

// repairs the device by an amount
void Device::repair(double amount)
{
    assert(amount >= 0.0);
    
    damageLevel += amount;
    if (damageLevel > 0.0)
    {
        damageLevel = 0.0;
    }
}

// Damages the device by a random amount (between 1 - 5.99..)
void Device::damage() {
    double amount = common::randomInRange(1, 5);
    damage(amount);
}

// Repairs the device by a random amount (between 1 - 3.99..)
void Device::repair() {
    double amount = common::randomInRange(1, 3);
    repair(amount);
}

// make a random event occur (60/40 split between damage/repair)
void Device::event()
{
    // 60% damage, 40% repair
    if (common::chanceOf(0.6))
    {
        double amount = common::randomInRange(1, 5);
        damage(amount);
    }
    else
    {
        double amount = common::randomInRange(1, 3);
        repair(amount);
    }
}

// resets the device to its original damage
void Device::reset()
{
    damageLevel = 0.0;
}

#ifndef NDEBUG

// checks that all of the functions work properly
void Device::whiteBoxTest()
{
    std::cout << "Device white box test\n";
    
    Device d(1, "Warp Engines");
    
    assert(d.toString() == "[1], Warp Engines, Damage: 0.000000");
    
    assert(d.isOperational());
    assert(d.getDamage() == 0.0);
    std::cout << "Device is fully working, and not damaged: " << d << "\n";
    
    d.damage(2.5);
    
    std::cout << "Device might not be operational because it might have taken 2.5 damage (60% chance): " << d << "\n";
    
    d.repair(1.0);
    
    std::cout << "Device might have (if it got damaged) 1.5 damage: " << d << "\n";
    
    d.repair(100.0);
    
    assert(d.getDamage() == 0.0);
    assert(d.isOperational());
    std::cout << "Device is repaired fully, and operational again: " << d << "\n";
    
    std::cout << "Device white box test success\n";
}

#endif

// converts the device to a string
std::string Device::toString() const
{
    return "[" + std::to_string(id) + "], " + name + ", Damage: " + std::to_string(damageLevel);
}

// prints the device's specs
std::ostream &operator<<(std::ostream &os, const Device &d)
{
    os << d.toString();
    
    return os;
}

Devices::Devices() {}
Devices::Devices(const std::vector<Device> &devices) : devices(devices) {}

// Gets a random device from devices
Device& Devices::randomDevice() {
    int index = common::randomInt(0, devices.size() - 1);
    return devices[index];
}

// Adds a device to the vector (devices)
void Devices::addDevice(int id, std::string name)
{
    devices.push_back(Device(id, name));
}

// Removes a device from the vector (devices)
void Devices::removeDevice(int id)
{
    devices.erase(devices.begin() + id);
}

// Returns a device from the vector (devices)
Device &Devices::getDevice(int id)
{
    assert(id >= 0 && id < devices.size());

    return devices[id];
}

// Returns a read-only copy of a device from the vector (devices)
const Device &Devices::getDevice(int id) const
{
    assert(id >= 0 && id < devices.size());

    return devices[id];
}

// Makes a random event occur
void Devices::randomDamageRepairEvent()
{
    if (devices.size() <= 0 || common::chanceOf(0.8))
        return;

    randomDevice().event();

    std::cout << toString();
}

// Damages the device with id by an amount
void Devices::damage(int id, double amount)
{
    if (devices.size() <= 0 || amount <= 0 || devices.size() <= id)
        return;

    devices[id].damage(amount);

    std::cout << toString();
}

// Repairs the device with id by an amount
void Devices::repair(int id, double amount)
{
    if (devices.size() <= 0 || amount <= 0 || devices.size() <= id)
        return;

    devices[id].repair(amount);

    std::cout << toString();
}

// Damages a device (with id) by a random amount (between 1-6)
void Devices::randomDamage(int id)
{
    if (devices.size() <= 0 || devices.size() <= id)
        return;

    devices[id].damage();

    std::cout << toString();
}

// Repairs a device (with id) by a random amount (between 1-3)
void Devices::randomRepair(int id)
{
    if (devices.size() <= 0 || devices.size() <= id)
        return;

    devices[id].repair();

    std::cout << toString();
}

// Repairs all devices by an amount by the warpFactor
void Devices::moveRepair(double warpFactor)
{
    if (warpFactor > 1.0)
        warpFactor = 1.0;

    for (auto& d : devices) {
        d.repair(warpFactor);
    }

    std::cout << toString();
}

// Makes the devices take damage, but only if there is enough energy
void Devices::takeDamage(double phaserEnergy, double distance, int shields)
{
    if (phaserEnergy < 0 || distance <= 0 || shields < 0 || devices.size() <= 0)
        return;

    double hitPoints = phaserEnergy / distance;
    if (hitPoints / shields <= 0.02)
        return;

    if (common::chanceOf(0.6)) {
        randomDevice().damage();

        std::cout << toString();
    }
}

std::string Devices::toString() const
{
    std::string out = "Damage Report\n";

    for (const auto &d : devices)
    {
        out += d.toString() + "\n";
    }

    return out;
}

std::ostream &operator<<(std::ostream &os, const Devices &d)
{
    os << d.toString();

    return os;
}