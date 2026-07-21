#include "Ship.hpp"

#include <common/GameLib.hpp>

#include <cmath>
#include <numbers>
#include <algorithm>

Ship::Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY) :
    shields(shields), sectorX(common::toBase0(sectorX)), sectorY(common::toBase0(sectorY)), 
    quadrantX(common::toBase0(quadrantX)), quadrantY(common::toBase0(quadrantY)) {}

// Gets the ship's local position (which 
// sector it is currently in).
void Ship::getLocalLocation(int& x, int& y) const noexcept {
    x = common::toBase1(sectorX);
    y = common::toBase1(sectorY);
}

// Gets which quadrant this ship is 
// located in currently.
void Ship::getGlobalLocation(int& x, int& y) const noexcept {
    x = common::toBase1(quadrantX);
    y = common::toBase1(quadrantY);
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
    if (warpFactor > 10.0)
        warpFactor = 10.0;

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

// Makes the ship take damage. Returns whether
// the damage destroys the ship or not. 
bool Ship::takeDamage(double phaserEnergy) {
    shields -= phaserEnergy;
    if (shields <= 0) {
        shields = 0;
        return true;
    }

    return false;
}

// Calculates the effective phaser energy
// based off how much is fired, how 
// far the ship is, and how many klingons
// are in the quadant currently.
int Ship::firePhasers(double phaserEnergy, int x, int y, int numKlingons) {
    double distance = std::sqrt((sectorX - x) * (sectorX - x) + (sectorY - y) * (sectorY - y));
    double h = phaserEnergy / numKlingons;

    return (h / distance) * (common::random() + 2);
}