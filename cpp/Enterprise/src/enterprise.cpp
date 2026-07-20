#include "Enterprise.hpp"

Enterprise::Enterprise(double shields, int sectorX, int sectorY,
                int quadrantX, int quadrantY, int energy, 
                int torpedoes, bool docked) :
                Ship(shields, sectorX, sectorY, quadrantX, quadrantX),
                energy(energy), torpedoes(torpedoes), docked(docked) {}

// Makes the Enterprise move based off warpFactor
// and warpDirection, but double checks that the
// warp engines are still capable. This still allows
// the user to use impulse engines if the warp
// engines are offline. 
void Enterprise::move(double warpFactor, double warpDirection) {
    if (devices.isDamaged(Devices::WARP_ENGINES) && warpFactor >= 1.0)
        return;
    
    Ship::move(warpFactor, warpDirection);
}

// Makes the Enterprise take damage based off
// the effective phaser energy.
void Enterprise::takeDamage(double phaserEnergy) {
    devices.hitDamage(phaserEnergy, this->getShields());
    Ship::takeDamage(phaserEnergy);
}