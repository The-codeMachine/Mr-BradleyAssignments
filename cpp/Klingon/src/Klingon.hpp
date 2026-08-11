#pragma once

#include <Ship.hpp>

#include <common/GameLib.hpp>

/**
 * 
 * The Klingon class extends publicly from Ship. It
 * extends functionality minimally. Fire phasers now
 * reduces the Klingon's energy pool. The Klingon can
 * now also generate a random energy pool. 
 * 
 * This random energy pool is based off the algorithm:
 * 
 * ENERGY = BASE_ENERGY x (0.5 + RND(1))
 * 
 * Where BASE_ENERGY is equal to 200 and RND(1) returns 
 * a random number [0, 1). This allows a range between
 * [100, 300). 
 * 
 */
class Klingon : public Ship {
public:
    Klingon();
    Klingon(common::Location loc);
    Klingon(common::Location loc, int energy);

    int firePhasers(int x, int y);

    common::Location calculateDestination() const;
    void move(common::Location loc);

private:
    static int generateRandomEnergy();

private:
    static inline constexpr int BASE_ENERGY = 200;

};