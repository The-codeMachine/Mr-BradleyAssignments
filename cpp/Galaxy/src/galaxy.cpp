#include "Galaxy.hpp"

#include <common/GameLib.hpp>

#include <iostream>
#include <cassert>
#include <iomanip>
#include <string>

Galaxy::Galaxy() : totalBases(0)
{
    populateGalaxy();
}

// Gets the quadrant located at [index][index2]. Takes base-1 coordinates
// (reference)
Quadrant& Galaxy::getQuadrant(int index, int index2)
{
    assert(index >= 1 && index <= 8 && index2 >= 1 && index2 <= 8);
    return map[common::toBase0(index)][common::toBase0(index2)];
}

// Gets the quadrant located at [index][index2]. Takes base-1 coordinates.
// (const reference)
const Quadrant& Galaxy::getQuadrant(int index, int index2) const 
{
    assert(index >= 1 && index <= 8 && index2 >= 1 && index2 <= 8);
    return map[common::toBase0(index)][common::toBase0(index2)];
}

// Gets a Quadrant from the map. Takes the base-0 coordinates through location 
// (reference)
Quadrant& Galaxy::getQuadrant(common::Location loc) {
    return map[loc.quadrantX][loc.quadrantY];
}

// Gets a Quadrant from the map. Takes the base-0 coordinates through location
// (const reference)
const Quadrant& Galaxy::getQuadrant(common::Location loc) const {
    return map[loc.quadrantX][loc.quadrantY];
}

// Prints the map into the console
void Galaxy::printMap()
{
    std::cout << *this << "\n";
}

// Populates the galaxy with quadrants (ensures there is at least 1 base)
void Galaxy::populateGalaxy()
{
    totalBases = 0;

    for (int i = 0; i < 8; ++i)
    {
        for (int j = 0; j < 8; ++j)
        {
            map[i][j] = Quadrant();

            if (map[i][j].hasBase()) {
                if (totalBases < 2) {
                    totalBases++;
                } else {
                    map[i][j].removeBase();
                }
            }
        }
    }

    // Ensure at least 1 base exists
    if (totalBases < 1)
    {
        int i = common::randomInt(0, 7);
        int j = common::randomInt(0, 7);

        map[i][j].putBase();
        totalBases = 1;
    }
}

#ifndef NDEBUG

void Galaxy::whiteBoxTest()
{
    std::cout << "white box test\n";

    Galaxy g;
    g.printMap();

    std::cout << "\n\n"; // padding between maps

    std::cout << g;

    // verifies there is the correct number of klingons, and bases
    int klingon1 = 0;
    int klingon2 = 0;
    int klingon3 = 0;
    for (int i = 0; i < 8; ++i)
    {
        for (int j = 0; j < 8; ++j)
        {
            const Quadrant q = g.getQuadrant(common::toBase1(i), common::toBase1(j));

            int klingons = q.klingons();
            if (klingons == 1)
            {
                klingon1++;
            }
            else if (klingons == 2)
            {
                klingon2++;
            }
            else if (klingons == 3)
            {
                klingon3++;
            }
        }
    }

    double klingon1Percent = klingon1 * 100.0 / 64;
    double klingon2Percent = klingon2 * 100.0 / 64;
    double klingon3Percent = klingon3 * 100.0 / 64;
    double basePercent = g.totalBases * 100.0 / 64;

    std::cout << "Percent of 1 klingons: " << klingon1Percent << "\n";
    std::cout << "Percent of 2 klingons: " << klingon2Percent << "\n";
    std::cout << "Percent of 3 klingons: " << klingon3Percent << "\n";
    std::cout << "Percent of bases: " << basePercent << "\n";

    std::cout << "White box test success\n";
}

#endif

// turns the galaxy int a string similar to:
/*
004 104 014 006 008 005 002 001 
002 105 002 206 007 002 102 008 
008 004 008 005 103 004 005 008 
001 205 104 003 003 004 018 007 
001 006 003 003 108 005 001 005 
008 006 106 006 002 003 002 006 
015 001 001 105 001 006 008 004 
004 003 204 002 108 002 205 106 
*/
std::ostream &operator<<(std::ostream &os, const Galaxy &g)
{
    for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < 8; ++j) {
            os << g.map[i][j] << " ";
        }

        os << "\n";
    }

    return os;
}