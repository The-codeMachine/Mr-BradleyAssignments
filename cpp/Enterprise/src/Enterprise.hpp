#pragma once

#include <Ship.hpp>
#include <Device.hpp>

/**
 * The Enterprise represents the player's
 * ship. It can do everything other ships
 * can, and more. This includes:
 * - Moving
 * - Firing phasers
 * - Firing torpedoes
 * - Docking
 * 
 * If the Enterprise is destroyed, the game
 * ends for the player.
 * 
 * If a certain device is destroyed, then that
 * specific game mechanic is unavailable for the
 * player.
 * 
 * The Enterprise is constructed using a random
 * location, or a specified location. 
 * 
 */
class Enterprise : public Ship {
public:
    Enterprise(int energy, common::Location location, double shields, int torpedoes, bool docked);
    Enterprise(int energy, double shields, int torpedoes, bool docked);

    bool isDestroyed() const noexcept override;

    std::vector<common::Location> calculatePath(double warpFactor, double warpDirection) override;
    bool takeDamage(double phaserEnergy) override;

    int firePhasers(double phaserEnergy, int x, int y, int numKlingons);

    bool getDocked() const noexcept;
    void updateDocked(bool value);

    void adjustShields(double shields);

    void damageReport() const;

    std::string toString() const;

private:
    void dock();

private:
    Devices devices;

    double shields;
    int torpedoes;
    bool docked;
};