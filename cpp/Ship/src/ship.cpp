#include "Ship.hpp"

#include <cmath>
#include <numbers>
#include <algorithm>

Ship::Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY) :
    shields(shields), sectorX(sectorX), sectorY(sectorY), 
    quadrantX(quadrantX), quadrantY(quadrantY) {}

// Gets the ship's local position (which 
// sector it is currently in).
void Ship::getLocalLocation(int& x, int& y) const noexcept {
    x = sectorX;
    y = sectorY;
}

// Gets which quadrant this ship is 
// located in currently.
void Ship::getGlobalLocation(int& x, int& y) const noexcept {
    x = quadrantX;
    y = quadrantY;
}

// Gets the shields of the ship and returns it.
double Ship::getShields() const noexcept {
    return shields;
}

// Makes the ship move based off a warp
// factor and direction. This uses exact
// trignonmetry to calculate the precise place
// the ship will end up. 
void Ship::move(double warpFactor, double warpDirection) {
    double currentGlobalX = quadrantX * GRID_SIZE + sectorX;
    double currentGlobalY = quadrantY * GRID_SIZE + sectorY;

    // warp speed == total sector distance
    double distanceInSectors = warpFactor * GRID_SIZE;

    // makes it base-0 from a base-1 input
    double angleDegrees = 90.0 - (warpDirection - 1.0) * 45.0;
    // convert direction to standard radians
    double radians = angleDegrees * (std::numbers::pi / 180.0);

    // calculate displacement vectors using trignonmetry
    double deltaX = distanceInSectors * std::cos(radians);
    double deltaY = distanceInSectors * std::sin(radians);

    // new global positions
    double newGlobalX = currentGlobalX + deltaX;
    double newGlobalY = currentGlobalY + deltaY;

    // edge cases for galaxy boundaries
    newGlobalX = std::clamp(newGlobalX, 0.0, 63.99);
    newGlobalY = std::clamp(newGlobalY, 0.0, 63.99);

    // convert to ints
    int newQuadX = (int) (newGlobalX / GRID_SIZE);
    int newQuadY = (int) (newGlobalY / GRID_SIZE);

    int newSectX = (int)newGlobalX % GRID_SIZE;
    int newSectY = (int)newGlobalY % GRID_SIZE;

    // assign new values
    quadrantX = newQuadX;
    quadrantY = newQuadY;

    sectorX = newSectX;
    sectorY = newSectY;
}

// Makes the ship take damage based off 
// effective phaser energy. 
void Ship::takeDamage(double phaserEnergy) {
    shields -= phaserEnergy;
    if (shields <= 0) {
        // destory ship IDK how to handle rn
    }
}

// Makes the ship fire phasers. This is
// based off the (x, y) value which is
// its destination. (Within one quadrant)
void Ship::firePhasers(double phaserEnergy, int x, int y) {
    
}