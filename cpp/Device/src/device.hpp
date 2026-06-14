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
    
    std::string damageReport() const;

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
Devices Status Report
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROl: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

Getters test success
Simulation test
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROl: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROl: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

...

WARP ENGINES: -1.513411
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: -0.600000
TORPEDO CONTROl: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

Simulation test success
Devices test success

*/