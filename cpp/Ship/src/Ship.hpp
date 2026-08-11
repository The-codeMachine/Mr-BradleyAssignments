#pragma once

#include <common/GameLib.hpp>

#include <vector>

/**
 * 
 * The Ship class is the base class to all moveable
 * objects within this game. This includes the Enterprise
 * and Klingons. Every Ship has a specific energy level
 * and location. Location is represented through the
 * Location class. 
 * 
 * Ship implements the following non-virtual functions:
 *  - Getting the current energy level
 *  - Adjusting the current energy level
 *  - Getting the current location
 * 
 * The rest of Ship's functions are virtual:
 *  - Checking whether the ship is destroyed (check through energy level <= 0)
 *  - Calculating the path of a ship based off a warp factor and direction
 *  - Moving the ship (simply adjusts the Ship's location and reduces the energy
 *      level based off how far it went).
 *  - Makes the ship take damage (reduces the energy levels).
 *  - Makes the ship fire its phasers (simply calculates the damage output).
 * 
 * All functions take base-0 coordinates. This includes the firePhasers function.
 * Despite it taking raw coordinates, unlike most classes where it would take
 * base-1 coordinates, this takes base-0 coordinates. 
 * 
 */
class Ship {
public:
    Ship();
    Ship(int energy);
    Ship(int energy, common::Location location);
    
    common::Location getLocation() const noexcept;

    int getEnergy() const noexcept;
    void adjustEnergy(int energy);

    virtual bool isDestroyed() const noexcept;

    virtual std::vector<common::Location> calculatePath(double warpFactor, double warpDirection);
    virtual void move(common::Location location, double warpFactor);

    virtual bool takeDamage(double phaserEnergy);
    virtual int firePhasers(double phaserEnergy, int x, int y);

private:
    static double calculateNextBoundaryX(double x, double dx);
    static double calculateNextBoundaryY(double y, double dy);

private:
    int energy;

    common::Location location;

    static constexpr int GRID_SIZE = 8;
};