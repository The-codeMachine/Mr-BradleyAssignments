#include "Ship.hpp"

#include <common/GameLib.hpp>

#include <cmath>
#include <numbers>
#include <algorithm>

Ship::Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY) :
    shields(shields), location(common::toBase0(sectorX), common::toBase0(sectorY), 
    common::toBase0(quadrantX), common::toBase0(quadrantY)) {}

// Gets the ship's local position (which
// sector it is currently in).
common::Location Ship::getLocation() const noexcept {
    return location;
}

// Gets the shields of the ship and returns it.
double Ship::getShields() const noexcept {
    return shields;
}

// Makes the ship move based off a warp
// factor and direction. This uses exact
// trignonmetry to calculate the precise place
// the ship will end up.
// 
// The path will be returned as base-0.
// 
// This converts the warp direction into radians
// (degrees). Based off these degrees, it constructs
// a ratio of x sectors to y sectors travelled. It
// then simulates travelling through all these sectors
// and adds it to the path which it returns. 
std::vector<common::Location> Ship::calculatePath(double warpFactor, double warpDirection) {
    std::vector<common::Location> path;

    warpFactor = std::min(warpFactor, 10.0);

    // Warp 1 = 8 sectors
    int distance = (int) std::round(warpFactor * GRID_SIZE);

    // Convert direction into angle
    double angleDegrees = 90.0 + (warpDirection - 3.0) * 45.0;
    double radians = angleDegrees * (std::numbers::pi / 180);

    // Direction ratio
    double dx = std::cos(radians);
    double dy = -std::sin(radians);

    // Normalize
    double length = std::sqrt(dx * dx + dy * dy);

    dx /= length;
    dy /= length;

    // Current galaxy position
    double x = location.quadrantX * GRID_SIZE + location.sectorX;
    double y = location.quadrantY * GRID_SIZE + location.sectorY;

    int lastX = (int) x;
    int lastY = (int) y;

    double travelled = 0;

    while (travelled < distance) {

        x += dx;
        y += dy;

        travelled++;

        // Outside galaxy
        if (x < 0 || x >= 64 ||
                y < 0 || y >= 64) {
            break;
        }

        int globalX = (int) std::floor(x);
        int globalY = (int) std::floor(y);

        if (globalX != lastX || globalY != lastY) {

            path.push_back({
                    globalX % GRID_SIZE,
                    globalY % GRID_SIZE,
                    globalX / GRID_SIZE,
                    globalY / GRID_SIZE});

            lastX = globalX;
            lastY = globalY;
        }
    }

    return path;
}

// Moves the ship to the new location.
// Does not do any checks to validate
// that the location is a valid position.
void Ship::move(common::Location location) {
    this->location = location;
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
    double distance = std::sqrt((location.sectorX - x) * (location.sectorX - x) + 
        (location.sectorY - y) * (location.sectorY - y));
    double h = phaserEnergy / numKlingons;

    return (h / distance) * (common::random() + 2);
}