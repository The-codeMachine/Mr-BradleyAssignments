#pragma once

#include <string>
#include <ostream>
#include <map>

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

    void takeDamage(const std::string_view &deviceName, double amount);
    void damageOverTime(double time);
    void hitDamage(double phaserEnergy, double shields);
    void damageEvent();

    void makeRepair(const std::string_view &deviceName, double amount);
    void repairAll(double amount);
    void repairOverTime(double time);
    void repairEvent();

    void randomEvent();

    bool isDamaged(const std::string_view &deviceName) const;
    double getDamage(const std::string_view &deviceName) const;
    std::string getStatus(const std::string_view &deviceName) const;
    int numDamaged() const;

    std::string damageReport() const;

    std::string toString() const;

    friend std::ostream &operator<<(std::ostream &os, const Devices &d);

    static inline constexpr std::string_view WARP_ENGINES = "WARP ENGINES";
    static inline constexpr std::string_view SHORT_RANGE_SENSORS = "SHORT RANGE SENSORS";
    static inline constexpr std::string_view LONG_RANGE_SENSORS = "LONG RANGE SENSORS";
    static inline constexpr std::string_view PHASER_CONTROL = "PHASER CONTROL";
    static inline constexpr std::string_view TORPEDO_CONTROL = "TORPEDO CONTROL";
    static inline constexpr std::string_view SHIELD_CONTROL = "SHIELD CONTROL";
    static inline constexpr std::string_view DAMAGE_CONTROL = "DAMAGE CONTROL";
    static inline constexpr std::string_view COMPUTER_SYSTEMS = "COMPUTER SYSTEMS";

private:
    void damage(int index, double amount);
    void repair(int index, double amount);
    void repair();

    bool anyDamaged() const;
    int randomDevice() const;
    bool isValidIndex(int index) const;
    bool isValidAmount(double amount) const;

    int convertToIndex(const std::string_view &deviceName) const;
    std::string_view getNameByIndex(int index) const;

private:
    double devices[8];

    const std::map<std::string_view, int> map = {
        {WARP_ENGINES, 0},
        {SHORT_RANGE_SENSORS, 1},
        {LONG_RANGE_SENSORS, 2},
        {PHASER_CONTROL, 3},
        {TORPEDO_CONTROL, 4},
        {SHIELD_CONTROL, 5},
        {DAMAGE_CONTROL, 6},
        {COMPUTER_SYSTEMS, 7}
    };

    static inline constexpr double UNDAMAGED = 0.0;
};

/*
Sample Output

Devices test
Getters test
Warp engines damage: 0
Warp engines damage status: 0
WARP ENGINES: 0.000000
Devices Status Report
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

Getters test success
Simulation test
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: 0.000000
COMPUTER SYSTEMS: 0.000000

...

Devices Status Report
WARP ENGINES: 0.000000
SHORT RANGE SENSORS: 0.000000
LONG RANGE SENSORS: 0.000000
PHASER CONTROL: 0.000000
TORPEDO CONTROL: 0.000000
SHIELD CONTROL: 0.000000
DAMAGE CONTROL: -0.101284
COMPUTER SYSTEMS: 0.000000

Simulation test success
Devices test success

*/