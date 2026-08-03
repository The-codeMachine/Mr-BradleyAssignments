#pragma once

#include <Ship.hpp>

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