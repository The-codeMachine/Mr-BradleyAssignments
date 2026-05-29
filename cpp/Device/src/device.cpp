#include "Device.hpp"

#include <cassert>
#include <iostream>
#include <iomanip>

Device::Device() : id(0), name("UNKNOWN"), damageLevel(0.0) {}

Device::Device(int id, const std::string& name) : id(id), name(name), damageLevel(0.0) {}

// gets the device's id
int Device::getId() const {
    return id;
}

// gets the device's name
const std::string& Device::getName() const {
    return name;
}

// gets the device's damage
double Device::getDamage() const {
    return damageLevel;
}

// checks if the device is operational
bool Device::isOperational() const {
    return damageLevel == 0.0;
}

// damages the device by an amount
void Device::damage(double amount) {
    assert(amount >= 0.0);

    damageLevel -= amount;
}

// repairs the device by an amount
void Device::repair(double amount) {
    assert(amount >= 0.0);

    damageLevel += amount;
    if (damageLevel > 0.0) {
        damageLevel = 0.0;
    }
}

// resets the device to its original damage
void Device::reset() {
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

    assert(!d.isOperational());
    assert(d.getDamage() == -2.5);
    std::cout << "Device is not operational because it has 2.5 damage: " << d << "\n";

    d.repair(1.0);

    assert(d.getDamage() == -1.5);
    std::cout << "Device has 1.5 damage: " << d << "\n";

    d.repair(100.0);

    assert(d.getDamage() == 0.0);
    assert(d.isOperational());
    std::cout << "Device is repair fully, and operational again: " << d << "\n";

    std::cout << "Device white box test success\n";
}

#endif

// converts the device to a string
std::string Device::toString() const {
    return "[" + std::to_string(id) + "], " + name + ", Damage: " + std::to_string(damageLevel);
}

// prints the device's specs
std::ostream& operator<<(std::ostream& os, const Device& d)
{
    os << d.toString();

    return os;
}