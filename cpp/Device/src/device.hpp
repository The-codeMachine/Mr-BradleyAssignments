#pragma once

#include <string>
#include <ostream>

/**
 *
 * The Devices class holds the control for
 * the damage level for the devices within
 * the Enterprise. It allows the Enterprise 
 * to do certain actions. Each device 
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
 *  - Take Damage (random)
 *  - Take damage over time (based off the time)
 *  - Take hit damage (based off energy)
 *  - Make a damage event occur
 *  
 *  - Make a repair (based off index, and amount)
 *  - Repair all the devices (by an amount)
 *  - Repairs all the devices over a time
 *  - Make a repair event occur
 *  
 *  - Check the damage of a device
 *  - Check if a device is damaged or not
 * 
 *  - Print a status report / Convert to string
 *
 */
class Devices
{
public:
    Devices();
    
    void takeDamage();
    void damageOverTime(double time);
    void hitDamage(double phaserEnergy, double shields);
    void damageEvent();

    void makeRepair(int index, double amount);
    void repairAll(double amount);
    void repairOverTime(double time);
    void repairEvent();

    void randomEvent();

    bool isDamaged(int index) const;
    double getDamage(int index) const;
    
    std::string damageReport() const;

    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Devices &d);

private:
    void damage(int index, double amount);
    void repair(int index, double amount);
    void damage();
    void repair();

    bool anyDamaged() const;
    int randomDevice() const;
    bool isValidIndex(int index) const;

private:
    double devices[8];  
    
    static constexpr double FULLY_REPAIRED = 0.0;

};

/*
Sample Output

Devices test
Getters test
Warp engines damage: 0
Warp engines damage status: 0
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

WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: -1.521801
PHASER CONTROL: 0.000000
TORPEDO CONTROl: 0.000000
SHIELD CONTROL: -0.249914
DAMAGE CONTROL: -1.351348
COMPUTER SYSTEMS: 0.000000

Simulation test success
Devices test success

*/