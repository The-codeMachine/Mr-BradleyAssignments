#pragma once

#include <string>
#include <ostream>

/**
 *
 * The Devices class holds all the information for
 * the devices within the Enterprise. It allows the 
 * Enterprise to do certain actions. Each device 
 * index correlates to a different device: 
 * 0: WARP ENGINES 
 * 1: SHORT RANGE SENSORS
 * 2: LONG RANGE SENSORS
 * 3: PHASER CONTROL
 * 4: TORPEDO CONTROl
 * 5: SHIELD CONTROL
 * 6: DAMAGE CONTROL
 * 7: COMPUTER SYSTEMS
 * 
 * Devices' operations include:
 *  - Construction
 *  - Repairs the devices by an amount (equal to the warp factor)
 *  - Takes damage (based off a phaser)
 *  - Has a random damage/repair event occur
 *  - Repairs all the devices (for docking)
 *  - Checks if a device is operational
 *  - Prints a status report 
 *
 */
class Devices
{
public:
    Devices();
    
    void moveRepair(double warpFactor);
    void takeDamage(double phaserEnergy, int shields);
    
    void randomDamageRepairEvent();
    void repairAllDevices(double amount);

    bool isOperational(int id) const;
    double getDamage(int id) const;
    
    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Devices &d);

private:
    void damage(int index, double amount);
    void repair(int index, double amount);
    void damage();
    void repair();

    int randomDevice();

private:
    double devices[8];    

};

/*
Sample Output

Devices test
Getters test
Warp engines damage: 0
Warp engines operation status: 1
Getters test success
Simulation test
WARP ENGINES: 0
SHORT RANGE SENSORS: 0
LONG RANGE SENSORS: 0
PHASER CONTROL: 0
TORPEDO CONTROl: 0
SHIELD CONTROL: 0
DAMAGE CONTROL: 0
COMPUTER SYSTEMS: 0
Devices Status Report

WARP ENGINES: 0
SHORT RANGE SENSORS: 0
LONG RANGE SENSORS: 0
PHASER CONTROL: 0
TORPEDO CONTROl: 0
SHIELD CONTROL: 0
DAMAGE CONTROL: 0
COMPUTER SYSTEMS: 0
Devices Status Report

...

WARP ENGINES: 0
SHORT RANGE SENSORS: 0
LONG RANGE SENSORS: 0
PHASER CONTROL: 0
TORPEDO CONTROl: 0
SHIELD CONTROL: 0
DAMAGE CONTROL: 0
COMPUTER SYSTEMS: -0.6
Devices Status Report

Simulation test success
Devices test success

*/