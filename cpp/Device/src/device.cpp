#include "device.hpp"

#include <iostream>
#include <cassert>

// Makes a device take damage by an amount
void Device::takeDamage(double amount)
{
    assert(amount > 0);

    damage += amount;
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

// Makes the device take damage (60% chance). If it succeeds,
// there will be between 1 to 5 damage.
void Device::damageEvent()
{
    if (common::chanceOf(0.6))
        takeDamage(common::randomInt(1, 5));
}

// Makes the device repair (40% chance). If is succeeds
// repairs the device between 1 to 3 damage.
void Device::repairEvent()
{
    if (common::chanceOf(0.4))
        repair(common::randomInt(1, 3));
}

// Checks if the device is broken
bool Device::isBroken() const
{
    return damage != 0.0;
}

#ifndef NDEBUG

// Tests that the device's events work correctly
void Device::whiteBoxTest()
{
    std::cout << "Starting white box test\n";

    Device d(10, 1, "test_device");

    bool damageOccurred = false;
    bool repairOccurred = false;
    for (int i = 0; i < 10000; ++i)
    {
        double previousDamage = d.damage;
        d.damageEvent();

        if (d.damage > previousDamage)
        {
            damageOccurred = true;
            double difference = d.damage - previousDamage;

            assert(difference >= 1 && difference <= 5);
        }

        previousDamage = d.damage;
        d.repairEvent();

        if (d.damage < previousDamage)
        {
            repairOccurred = true;
            double difference = previousDamage - d.damage;

            assert(difference >= 1 && difference <= 3);
        }

        assert(d.damage >= 0);

        if (damageOccurred && repairOccurred)
            break;
    }

    assert(damageOccurred);
    assert(repairOccurred);

    std::cout << "White box test passed\n";
}

#endif