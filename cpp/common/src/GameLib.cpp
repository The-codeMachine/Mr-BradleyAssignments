#include "../include/common/GameLib.hpp"

#include <iostream>
#include <chrono>
#include <algorithm>
#include <numbers>
#include <cassert>

namespace common
{

    // Creates a location from the predefined sector (x, y) and quadrant (x, y) values. Does
    // not check that they are valid. You can enter invalid values (e.g. -1 to represent nothing)
    Location::Location(int sectorX, int sectorY, int quadrantX, int quadrantY) :
        sectorX(sectorX), sectorY(sectorY), quadrantX(quadrantX), quadrantY(quadrantY) {}

    // Generates a random sector (x, y) and quadrant (x, y) position. Makes sure it is valid.   
    Location::Location() : sectorX(common::randomInt(MIN, MAX)), sectorY(common::randomInt(MIN, MAX)),
        quadrantX(common::randomInt(MIN, MAX)), quadrantY(common::randomInt(MIN, MAX)) {}

    // Checks whether or not this location has the same values as another, returns
    // true if it does 
    bool Location::operator==(const Location& other) const {
        return (other.sectorX == sectorX && other.sectorY == sectorY 
            && quadrantX == other.quadrantX && quadrantY == other.quadrantY);
    }

    // Checks whether or not this location has the same values as another, returns
    // true if it does not
    bool Location::operator!=(const Location& other) const {
        return !(*this == other);
    }

    // Checks that another location and this location are in the same quadrant
    bool Location::sameQuadrant(const Location& other) const {
        return quadrantX == other.quadrantX && quadrantY == other.quadrantY;
    }

    // Checks that another location and this location are in the same sector
    bool Location::sameSector(const Location& other) const {
        return sectorX == other.sectorX && sectorY == other.sectorY;
    }

    // Converts the location into a string. Checks that the positions are valid
    // and converts them to (row, column) notation and to base-1.
    std::string Location::toString() const {
        std::string out;
        
        if (sectorX != -1 && sectorY != -1) {
            out += "(" + std::to_string(toBase1(sectorY)) + ", " + std::to_string(toBase1(sectorX)) + ")";
        }
        if (quadrantX != -1 && quadrantY != -1) {
            out += " in ("+ std::to_string(toBase1(quadrantY)) + ", " + std::to_string(toBase1(quadrantX)) + ")";
        }
        
        return out;
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

    // Converts degrees to radians
    double radians(double degrees) {
        return degrees * (std::numbers::pi / 180);
    }

    // Converts radians to degrees
    double degrees(double radians) {
        return radians * (180.0 / std::numbers::pi);
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