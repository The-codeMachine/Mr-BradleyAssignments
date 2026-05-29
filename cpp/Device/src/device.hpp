#pragma once

#include <common/random.hpp>

#include <string>
#include <ostream>

/**
 * A device represents one of the Enterprise's ship systems.
 *
 * Rules:
 *  - Damage is represented as a NEGATIVE value.
 *  - 0.0 means fully operational.
 *  - The more negative the value, the more damaged the device is.
 *
 * Operations include:
 *  - damaging the device
 *  - repairing the device
 *  - checking operational status
 *  - printing device information
 *
 */
class Device
{
public:
    Device();
    Device(int id, const std::string &name);

    int getId() const;
    const std::string &getName() const;
    double getDamage() const;
    bool isOperational() const;

    void damage(double amount);
    void repair(double amount);

    void reset();

#ifndef NDEBUG

    static void whiteBoxTest();

#endif

    std::string toString() const;

    friend std::ostream& operator<<(std::ostream& os, const Device& d);

private:
    int id;
    std::string name;

    // 0.0 = healthy
    // negative = damaged
    double damageLevel;
};

/*
Sample Output

Device Test
Getters test
Device constructed with specs: [1], Test Device, Damage: 0.000000
Device id: 1
Device name: Test Device
Device damage: 0
Getters test success
Device white box test
Device is fully working, and not damaged: [1], Warp Engines, Damage: 0.000000
Device is not operational because it has 2.5 damage: [1], Warp Engines, Damage: -2.500000
Device has 1.5 damage: [1], Warp Engines, Damage: -1.500000
Device is repair fully, and operational again: [1], Warp Engines, Damage: 0.000000
Device white box test success
Device test success

*/