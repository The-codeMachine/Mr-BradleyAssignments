#pragma once

#include <common/GameLib.hpp>

#include <vector>

/**
 * 
 * This is the base Ship class. The ship class
 * consists of energy, and position information.
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
    virtual int firePhasers(double phaserEnergy, int x, int y, int numKlingons);

private:
    static double calculateNextBoundaryX(double x, double dx);
    static double calculateNextBoundaryY(double y, double dy);

private:
    int energy;

    common::Location location;

    static constexpr int GRID_SIZE = 8;
};