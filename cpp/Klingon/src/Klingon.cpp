#include "Klingon.hpp"

Klingon::Klingon() : Ship(generateRandomEnergy()) {}
Klingon::Klingon(common::Location loc) : Klingon(loc, generateRandomEnergy()) {}
Klingon::Klingon(common::Location loc, int energy) : Ship(energy, loc) {}

// Generates a random number to correlate to the Klingon's initial power
int Klingon::generateRandomEnergy() {
    return BASE_ENERGY + (0.5 * common::random());
}

// Calculates the amount of phaser damage to give the Enterprise
// based off the Enterprise's current location. Will deplete
// this Klingon's energy supply by diving it by (3, 4].
int Klingon::firePhasers(int x, int y) {
    int damage = Ship::firePhasers(getEnergy(), x, y);
    
    adjustEnergy((getEnergy() / common::randomInRange(3, 4)) - getEnergy());

    return damage;
}