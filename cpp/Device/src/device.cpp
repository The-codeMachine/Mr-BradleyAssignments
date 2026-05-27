#include "Device.hpp"

#include <iostream>
#include <cassert>

// Makes a device take damage by an amount
void Device::takeDamage()
{
    if (common::chanceOf(0.6))
        damage += common::randomInt(1, 5);
}

// Repairs the device by an amount. Ensures it
// does not exceed 0
void Device::repair(double amount)
{
    assert(amount > 0);

    damage -= amount;
    if (damage < 0)
        damage = 0;
}

// Checks if the device is broken
bool Device::isBroken() const
{
    return damage != 0.0;
}