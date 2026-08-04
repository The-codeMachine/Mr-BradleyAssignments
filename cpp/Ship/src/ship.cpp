#include "Ship.hpp"

#include <common/GameLib.hpp>
#include <common/IO.hpp>

#include <cmath>
#include <numbers>
#include <algorithm>

Ship::Ship() : energy(0) {}

Ship::Ship(int energy) : energy(energy) {}

Ship::Ship(int energy, common::Location location) : energy(energy), 
    location(location) {}


// Calculates the next boundary the ship will pass
// through on the x axis. This is based off the
// direction, and current x position.
//
// Returns a infinity is there is none. This makes
// the ship overshoot the warp factor making it stop
// the calculation.
double Ship::calculateNextBoundaryX(double x, double dx) {
    if (dx > 0)
        return (std::floor(x) + 1 - x) / dx;

    if (dx < 0 ) 
        return (std::ceil(x) - 1 - x) / dx;

    return 1e199;
}

// Calculates the next boundary the ship will pass
// through on the y axis. This is based off the
// direction, and current y position.
//
// Returns a infinity is there is none. This makes
// the ship overshoot the warp factor making it stop
// the calculation.
double Ship::calculateNextBoundaryY(double y, double dy) {
    if (dy > 0)
        return (std::floor(y) + 1 - y) / dy;

    if (dy < 0 ) 
        return (std::ceil(y) - 1 - y) / dy;

    return 1e199;
}

// Gets the ship's local position (which
// sector it is currently in).
common::Location Ship::getLocation() const noexcept {
    return location;
}

// Gets the energy of the ship and returns it
int Ship::getEnergy() const noexcept {
    return energy;
}

// Adjusts the energy by the amount. Does not check that
// the energy can go into negatives. 
void Ship::adjustEnergy(int amount) {
    energy += amount;
}

// Checks whether the ship is destroyed based off its energy (true if it is)
bool Ship::isDestroyed() const noexcept {
    return energy <= 0;
}

// Makes the ship move based off a warp
// factor and direction. This uses exact
// trignonmetry to calculate the precise place
// the ship will end up. This uses a ray. 
// 
// The path will be returned as base-0.
// 
// This converts the warp direction into radians
// (degrees). Based off these degrees it calculates
// two movement vectors (one for x, and another for y).
// Based off these vectors we calculate the next 
// horizontal and vertical boundary we move the ship
// and add it to the path. 
// 
// Cardinal Directions:
// 
//         3   
//     4       2   
// 5               1
//     6       8
//         7
// 
// These are calculated into angle degrees through:
// (warpDirection - 1.0) * 45;
// 
// The 45 makes every warpDirection be a different 
// cardinal direction (e.g. N = 3, SW = 6). The
// subtraction from one converts warpDirection to
// base-0 from base-1.
std::vector<common::Location> Ship::calculatePath(double warpFactor, double warpDirection) {
    std::vector<common::Location> path;

    warpFactor = std::min(warpFactor, 8.0);

    // Warp 1 = 8 sectors
    int distance = (int) std::round(warpFactor * GRID_SIZE);

    // Convert direction into angle
    double angleDegrees = (warpDirection - 1.0) * 45.0;
    double radians = angleDegrees * (std::numbers::pi / 180);

    // Direction ratio
    double dx = std::cos(radians);
    double dy = -std::sin(radians);

    // Normalize
    double length = std::sqrt(dx * dx + dy * dy);

    dx /= length;
    dy /= length;

    // Current galaxy position (makes it start in the middle of the sector)
    double x = location.quadrantX * GRID_SIZE + location.sectorX;
    double y = location.quadrantY * GRID_SIZE + location.sectorY;

    int lastX = (int) std::floor(x);
    int lastY = (int) std::floor(y);

    double travelled = 0;

    while (travelled < distance) {

        double distanceToXBoundary = calculateNextBoundaryX(x, dx);
        double distanceToYBoundary = calculateNextBoundaryY(y, dy);

        double movement = std::min(distanceToXBoundary, distanceToYBoundary);

        // Prevent overshooting warp distance
        if (travelled + movement > distance)
            movement = distance - travelled;

        x += dx * movement;
        y += dy * movement;

        travelled += movement;

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
void Ship::move(common::Location location, double warpFactor) {
    int energyUsed = (int) (warpFactor * 8 + 0.5);
    if (energy < energyUsed) {
        common::IO::println("Insufficient energy for warp");
        return;
    }

    adjustEnergy(-energyUsed);
    this->location = location;
}

// Makes the ship take damage. Returns whether
// the damage destroys the ship or not. Based off the
// ship's energy (because of how klingons work).
bool Ship::takeDamage(double phaserEnergy) {
    adjustEnergy(-phaserEnergy);
    if (energy <= 0) {
        energy = 0;
        return true;
    }

    return false;
}

// Calculates the effective phaser energy
// based off how much is fired, how 
// far the ship is, and how many klingons
// are in the quadant currently.
int Ship::firePhasers(double phaserEnergy, int x, int y) {
    double distance = std::sqrt((location.sectorX - x) * (location.sectorX - x) + 
        (location.sectorY - y) * (location.sectorY - y));

    return (phaserEnergy / distance) * (common::random() + 2);
}