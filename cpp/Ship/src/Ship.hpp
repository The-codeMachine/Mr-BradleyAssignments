#pragma once

#include <common/GameLib.hpp>

#include <vector>

/**
 * 
 * TODO:
 * Resonably large issue. Not sure how to handle it currently,
 * but essentially, the QuadrantMap is upside-down. As y goes
 * up it goes down. This is backwards to what is normal. This
 * issue is currently fixed by adjusting the delta-y to be 
 * negative, this makes North = up, but we might want to flip
 * the QuadrantMap, maybe just in the printing section. Any 
 * ideas? 
 * 
 */

/**
 * 
 * This is the base Ship class. The ship class
 * consists of shield, and position information.
 * It handles movement calculation, damage reduction,
 * and phaser firing for all base ships. Other ships
 * like the Enterprise might use this as a super
 * class and work upon the current functions
 * (e.g. adding checks for devices).
 * Current list of operations consist of:
 * - Move (move the ship based off warp factor, and direction)
 * - Make the ship take damage
 * - Fire the ship's phasers
 * 
 * Ship's get location, construction, and fire phasers all
 * take base-1 as input.
 * 
 * Internally, all the variables are base-0.
 * 
 */
class Ship {
public:
    Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY);
    
    common::Location getLocation() const noexcept;
    double getShields() const noexcept;

    virtual std::vector<common::Location> calculatePath(double warpFactor, double warpDirection);
    virtual void move(common::Location location);

    virtual bool takeDamage(double phaserEnergy);
    virtual int firePhasers(double phaserEnergy, int x, int y, int numKlingons);

private:
    double shields;

    common::Location location;

    static constexpr int GRID_SIZE = 8;
};