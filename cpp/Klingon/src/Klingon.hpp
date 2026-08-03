#pragma once

#include <Ship.hpp>

/**
 * 
 * The Klingon class extends Ship. It represents 
 * the enemy the Enterprise will run into during
 * all of its gameplay. It can fire and take damage.
 * Both come out of its energy pool. 
 * 
 */
class Klingon : public Ship {
public:
    Klingon();
    Klingon(common::Location loc);
    Klingon(common::Location loc, int energy);

    int firePhasers(int x, int y);

private:
    static int generateRandomEnergy();

private:

    static inline constexpr int BASE_ENERGY = 200;

};