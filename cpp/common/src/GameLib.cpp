#include "../include/common/GameLib.hpp"

#include <iostream>
#include <chrono>
#include <algorithm>
#include <cassert>

namespace common
{

    Location::Location(int sectorX, int sectorY, int quadrantX, int quadrantY) :
        sectorX(std::clamp(sectorX, MIN, MAX)), sectorY(std::clamp(sectorY, MIN, MAX)),
        quadrantX(std::clamp(quadrantX, MIN, MAX)), quadrantY(std::clamp(quadrantY, MIN, MAX)) {}

    Location::Location() : Location(-1, -1, -1, -1) {}

    bool Location::operator==(const Location& other) const {
        return (other.sectorX == sectorX && other.sectorY == sectorY 
            && quadrantX == other.quadrantX && quadrantY == other.quadrantY);
    }

    std::string Location::toString() const {
        return "(" + std::to_string(sectorY) + ", " + std::to_string(sectorX) + ") in ("
         + std::to_string(quadrantY) + ", " + std::to_string(quadrantX) + ")";
    }

    bool isBetween(double value, double low, double high) {
        return low <= value && value <= high;
    }

    // Converts c to base-0. Exepcts a 
    // base-1 argument. 
    int toBase0(int c) {
        return c - 1;
    }

    // Converts c to base-1. Exepcts a 
    // base-0 argument. 
    int toBase1(int c) {
        return c + 1;
    }

    void isBetweenTest()
    {
        std::cout << "Is between test\n";

        assert(isBetween(4, 1, 10));

        assert(!isBetween(-4, 1, 10));

        assert(!isBetween(14, 1, 10));

        std::cout << "Is between test succes\n";
    }

    void testDriver()
    {
        std::cout << "GameLib test driver run\n";

        randomTestDriver();
        isBetweenTest();
        stringUtilsTestDriver();

        std::cout << "GameLib test driver run success\n";
    }

} // namespace common