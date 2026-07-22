#include "Enterprise.hpp"

Enterprise::Enterprise(double shields, int sectorX, int sectorY,
                int quadrantX, int quadrantY, int energy, 
                int torpedoes, bool docked) :
                Ship(shields, sectorX, sectorY, quadrantX, quadrantY),
                energy(energy), torpedoes(torpedoes), docked(docked) {}

// Makes the Enterprise move based off warpFactor
// and warpDirection, but double checks that the
// warp engines are still capable. This still allows
// the user to use impulse engines if the warp
// engines are offline. 
std::vector<common::Location> Enterprise::calculatePath(double warpFactor, double warpDirection) {
    if (devices.isDamaged(Devices::WARP_ENGINES) && warpFactor >= 1.0)
        return {};

    return Ship::calculatePath(warpFactor, warpDirection);
}


// Makes the Enterprise take damage based off
// the effective phaser energy.
bool Enterprise::takeDamage(double phaserEnergy) {
    devices.hitDamage(phaserEnergy, this->getShields());
    return Ship::takeDamage(phaserEnergy);
}