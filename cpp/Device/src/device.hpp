#pragma once
#include <common/random.hpp>

#include <string>

/**
 *
 * The device holds the functionality to damage, and repair
 * itself. It allows the Enterprise's gameplay mechanics to
 * work (e.g. if the warp engines are broken, then the player
 * cannot use the warping functionality). Each device has its
 * own name, and id.
 * Operations include:
 *  - Constructing with a specified: id, name
 *  - Constructing with a specified: damage, id, name
 *  - damaging the device
 *  - repairing the device
 *  - damage/repair event (60%/40% chance respectively for each)
 *  - checking if the device is damaged
 *
 * Damage is a double value so that the device may repair during
 * warp as necessary.
 *
 */

class Device
{
public:
    // Creates a new device with a fully working system (damage = 0), and a
    // custom deviceId, and name.
    Device(int assignedDeviceId, std::string assignedDeviceName) 
    : damage(0), deviceId(assignedDeviceId), deviceName(assignedDeviceName) {}

    // Creates a device with a specified damage, id, and name.
    Device(double assignedDamage, int assignedDeviceId, std::string assignedDeviceName) 
    : damage(assignedDamage), deviceId(assignedDeviceId), deviceName(assignedDeviceName) {}

    void takeDamage(double amount);
    void repair(double amount);

    void damageEvent();
    void repairEvent();

    bool isBroken() const;

#ifndef NDEBUG

    static void whiteBoxTest();

#endif

private:
    double damage;

    int deviceId;
    std::string deviceName;
};

/*
Sample Output

Device Test
Constructor test
Device is created with 0 damage when specified
Device started with the specified damage
Constructor test success
Repair/Damage test
Device got repaired
Device got damaged
Device got repairedDevice got repaired, but is still broken
Repair/Damage test success
Starting white box test
White box test passed
Device test success

*/