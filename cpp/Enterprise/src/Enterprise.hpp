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
 */
class Enterprise : public Ship {
public:
    Enterprise(double shields, int sectorX, int sectorY,
                int quadrantX, int quadrantY, int energy,
                int torpedoes, bool docked);

    std::vector<common::Location> calculatePath(double warpFactor, double warpDirection) override;
    bool takeDamage(double phaserEnergy) override;

private:
    Devices devices;

    int energy;
    int torpedoes;
    bool docked;
};