#include "Enterprise.hpp"

#include <common/IO.hpp>

Enterprise::Enterprise(double shields, int sectorX, int sectorY,
                int quadrantX, int quadrantY, int energy, 
                int torpedoes, bool docked) :
                Ship(shields, sectorX, sectorY, quadrantX, quadrantY),
                energy(energy), torpedoes(torpedoes), docked(docked) {}

// Returns if the Enterprise is destroyed.
// Will return true if the Enterprise is destoryed.
bool Enterprise::isDestroyed() const noexcept {
    return getShields() <= 0;
}

// Makes the Enterprise move based off warpFactor
// and warpDirection, but double checks that the
// warp engines are still capable. This still allows
// the user to use impulse engines if the warp
// engines are offline. 
std::vector<common::Location> Enterprise::calculatePath(double warpFactor, double warpDirection) {
    if (devices.isDamaged(Devices::WARP_ENGINES) && warpFactor >= 0.2) {
        common::IO::println("Warp engines are damaged, maximum warp is 0.2");
        return {};
    }
    
    int energyUsed = (int)(warpFactor * 8 + 0.5);
    if (energy < energyUsed) {
        common::IO::println("Insufficient energy for warp");
        return {};
    }

    energy -= energyUsed;
    return Ship::calculatePath(warpFactor, warpDirection);
}


// Makes the Enterprise take damage based off
// the effective phaser energy.
bool Enterprise::takeDamage(double phaserEnergy) {
    devices.hitDamage(phaserEnergy, this->getShields());
    return Ship::takeDamage(phaserEnergy);
}

std::string Enterprise::toString() const {
    return "Energy: " + std::to_string(energy) +
                "\nLocation: " + getLocation().toString() +
                "\nTorpedoes: " + std::to_string(torpedoes) +
                "\nShields: " + std::to_string(getShields()) +
                "\nDocked: " + std::to_string(docked); 
}