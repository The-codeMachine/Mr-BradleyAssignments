#include "Klingon.hpp"

Klingon::Klingon() : Ship(generateRandomEnergy()) {}
Klingon::Klingon(common::Location loc) : Klingon(loc, generateRandomEnergy()) {}
Klingon::Klingon(common::Location loc, int energy) : Ship(energy, loc) {}

// Generates a random number to correlate to the Klingon's initial power
int Klingon::generateRandomEnergy() {
    return BASE_ENERGY * (0.5 + common::random());
}

// Calculates the amount of phaser damage to give the Enterprise
// based off the Enterprise's current location. Will deplete
// this Klingon's energy supply by diving it by (3, 4].
// Takes base-0 input
int Klingon::firePhasers(int x, int y) {
    int energy = getEnergy();
    adjustEnergy((energy / common::randomInRange(3, 4)) - energy);

    return Ship::firePhasers(energy, x, y);
}

// This moves the Klingon to a new random sector in the Quadrant.
// This function returns that random sector. This does NOT consume
// energy, and essentially teleports the Klingon (like the original)
// This does not actually move the klingon but rather returns the 
// sector where it should go. 
common::Location Klingon::calculateDestination() const {
    common::Location randomLocation = common::Location();
    
    return common::Location(randomLocation.sectorY, randomLocation.sectorX,
                            getLocation().quadrantY, getLocation().quadrantX);
}

// Moves the klingon to a new location
void Klingon::move(common::Location loc) {
    Ship::move(loc);
}