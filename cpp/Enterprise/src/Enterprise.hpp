#pragma once

#include <Ship.hpp>
#include <Device.hpp>

/**
 * 
 * The Enterprise represents the player's ship. It inherits
 * publicly from the Ship class. This class extends its
 * functionalities by adding:
 * 
 *  - Shields
 *  - Torpedoes
 *  - Docking capabilities
 *  - Device functionality
 * 
 * You can adjust the Enterprise's shields, check whether it 
 * is destroyed, or a device is destroyed. You can calculate
 * its movement path based off warp factor and direction.
 * You can update its dock value, print a damage report, or
 * reduce its torpedoes. 
 * 
 * This functionality is more or less forwarded to the Game
 * class which uses the Enterprise's public functions to 
 * allows the user to do specific commands.  
 * 
 */
class Enterprise : public Ship {
public:
    Enterprise(int energy, common::Location location, double shields, int torpedoes, bool docked);
    Enterprise(int energy, double shields, int torpedoes, bool docked);

    bool isDestroyed() const noexcept override;
    void kill();

    std::vector<common::Location> calculatePath(double warpFactor, double warpDirection) override;
    void move(common::Location loc, double warpFactor);
    
    bool takeDamage(double phaserEnergy) override;
    int firePhasers(double phaserEnergy, int x, int y, int numKlingons);

    int getTorpedoes() const noexcept;
    void reduceTorpedoes();

    bool getDocked() const noexcept;
    void updateDocked(bool value);

    double getShields() const noexcept;
    void adjustShields(double shields);

    bool isDeviceBroken(const std::string& str) const;
    
    void repairDevices();
    double estimateRepairDevices() const;
    void damageReport() const;

    std::string toString() const;

private:
    void dock();

private:
    Devices devices;
    double randomRepairModifier;

    double shields;
    int torpedoes;
    bool docked;

    static inline constexpr double RANDOM_MODIFIER_MIN = 0;
    static inline constexpr double RANDOM_MODIFIER_MAX = 0.5;
};