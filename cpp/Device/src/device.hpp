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
 *  - random event (damage, or repair)
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

    void damage();
    void repair();

    void event();

    void reset();

#ifndef NDEBUG

    static void whiteBoxTest();

#endif

    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Device &d);

private:
    int id;
    std::string name;

    // 0.0 = healthy
    // negative = damaged
    double damageLevel;
};

/**
 *
 * The Devices classs manages all of the Devices. It is
 * designed for ships, and the Enterprise. Operations include:
 *  - Construction without an arraylist and with one
 *  - Adding a device
 *  - Removing a device
 *  - Getting a device
 *  - Damaging a device
 *  - Repairing a device
 *  - Damaging/repairing a random device by a random amount
 *  - Apply the repair for moving
 *  - Have a random damage/repair event occur
 *  - Take damage based off the energy, distance, and shields
 *  - Convert to string
 *
 */
class Devices
{
public:
    Devices();
    Devices(const std::vector<Device> &devices);

    void addDevice(int id, std::string name);
    void removeDevice(int index);
    Device &getDevice(int index);
    const Device &getDevice(int index) const;

    void randomDamageRepairEvent();

    void damage(int index, double amount);
    void repair(int index, double amount);

    void randomDamage(int index);
    void randomRepair(int index);

    void moveRepair(double warpFactor);
    void takeDamage(double phaserEnergy, double distance, int shields);

    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Devices &d);

private:
    Device& randomDevice();

private:
    std::vector<Device> devices;
};

/*
Sample Output

Getters test
Device constructed with specs: [1], Test Device, Damage: 0.000000
Device id: 1
Device name: Test Device
Device damage: 0
Getters test success

Device white box test
Device is fully working, and not damaged: [1], Warp Engines, Damage: 0.000000
Device might not be operational because it might have taken 2.5 damage (60% chance): [1], Warp Engines, Damage: -2.500000
Device might have (if it got damaged) 1.5 damage: [1], Warp Engines, Damage: -1.500000
Device is repaired fully, and operational again: [1], Warp Engines, Damage: 0.000000
Device white box test success
Device test success
Devices test
Devices getters test
Damage Report
[1], Warp Engines, Damage: 0.000000
[2], Phaser Control, Damage: 0.000000

[1], Warp Engines, Damage: 0.000000
Devices getters test success
Damage/Repair test
Damage test
Damage Report
[1], Warp Engines, Damage: -3.000000
[2], Phaser Control, Damage: 0.000000

Repair test
Damage Report
[1], Warp Engines, Damage: -1.000000
[2], Phaser Control, Damage: 0.000000
Damage/Repair test success
Devices random test
Devices random damage
Damage Report
[1], Warp Engines, Damage: -4.032789
[2], Phaser Control, Damage: 0.000000

Devices random repair
Damage Report
[1], Warp Engines, Damage: -1.944652
[2], Phaser Control, Damage: 0.000000
Devices random test success
Devices ship test
Damage test
Damage Report
[1], Warp Engines, Damage: 0.000000
[2], Phaser Control, Damage: -2.024373

Devices random event

Devices move repair
Damage Report
[1], Warp Engines, Damage: 0.000000
[2], Phaser Control, Damage: -1.724373
Devices ship test success
Devices test success

*/