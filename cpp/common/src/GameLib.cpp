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
    Location::Location(int sectorY, int sectorX, int quadrantY, int quadrantX) :
        sectorY(sectorY), sectorX(sectorX), quadrantY(quadrantY), quadrantX(quadrantX) {}

    // Generates a random sector (x, y) and quadrant (x, y) position. Makes sure it is valid.   
    Location::Location() : sectorY(common::randomInt(MIN_INDEX_1, MAX_INDEX_1)), sectorX(common::randomInt(MIN_INDEX_1, MAX_INDEX_1)),
        quadrantY(common::randomInt(MIN_INDEX_1, MAX_INDEX_1)), quadrantX(common::randomInt(MIN_INDEX_1, MAX_INDEX_1)) {}

    // Checks whether or not this location has the same values as another, returns
    // true if it does 
    bool Location::operator==(const Location& other) const {
        return (other.sectorY == sectorY && other.sectorX == sectorX 
            && quadrantY == other.quadrantY && quadrantX == other.quadrantX);
    }

    // Checks whether or not this location has the same values as another, returns
    // true if it does not
    bool Location::operator!=(const Location& other) const {
        return !(*this == other);
    }

    // Checks that another location and this location are in the same quadrant
    bool Location::sameQuadrant(const Location& other) const {
        return quadrantY == other.quadrantY && quadrantX == other.quadrantX;
    }

    // Checks that another location and this location are in the same sector
    bool Location::sameSector(const Location& other) const {
        return sectorY == other.sectorY && sectorX == other.sectorX;
    }

    // Returns a string representing this location's current sector. Returns
    // an empty string if the sector values at invalid
    std::string Location::sectorString() const {
        if (sectorY != INVALID && sectorX != INVALID) {
            return "Sector (" + std::to_string(sectorX) + ", " + std::to_string(sectorY) + ")";
        }

        return "";
    }

    // Returns a string representing this location's current quadrant. Returns
    // an empty string if the quadrant values at invalid
    std::string Location::quadrantString() const {
        if (quadrantY != INVALID && quadrantX != INVALID) {
            return "Quadrant (" + std::to_string(quadrantX) + ", " + std::to_string(quadrantY) + ")";
        }

        return "";
    }

    // Converts the location into a string. Checks that the positions are valid
    // and converts them to (row, column) notation and to base-1. 
    std::string Location::toString() const {
        std::string out;
        
        // if sector string is empty then just do the
        // quadrant. 
        out += sectorString();
        if (out == "")
            return quadrantString();

        // if quadrant string is empty then simply return the
        // sector string, else add the "in" word. 
        std::string qStr = quadrantString();

        if (qStr == "")
            return out;
        out += " in " + qStr;
        
        return out;
    }

    // Checks whether or not a value is between low and high (inclusive)
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