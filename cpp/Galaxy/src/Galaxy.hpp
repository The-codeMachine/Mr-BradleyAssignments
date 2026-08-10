#pragma once

#include <common/GameLib.hpp>

#include <quadrant.hpp>

#include <array>
#include <optional>
#include <ostream>

/**
 * A galaxy holds 64 Quadrants, using an 8 by 8 grid. 
 * A galaxy can only have 2 bases at most, and a minimum
 * of 1 base.
 * 
 * Operations include:
 *  - Constructing a galaxy (creates 64 quadrants, and ensures it has at least 1 base, and at most 2)
 *  - index the quadrant using a 2d map 
 *  - printing the map 
 *  - formats the map into a string
 * 
 */
class Galaxy {
public:
    Galaxy();

    Quadrant& getQuadrant(int index, int index2);
    const Quadrant& getQuadrant(int index, int index2) const;

    Quadrant& getQuadrant(common::Location loc);
    const Quadrant& getQuadrant(common::Location loc) const;

    int starBases() const noexcept;
    
    int getKlingons() const noexcept;
    void reduceKlingons(int x, int y);
    void reduceKlingons(common::Location location);

    void longRangeScan(common::Location location);
    void printScannedGalaxy() const noexcept;

    void printMap();

#ifndef NDEBUG

    static void whiteBoxTest();

#endif

friend std::ostream &operator<<(std::ostream &os, const Galaxy &g);

private:
    void populateGalaxy();

private:

    static inline constexpr int MAP_SIZE = 8;

    Quadrant map[MAP_SIZE][MAP_SIZE];
    std::array<std::array<std::optional<Quadrant>, MAP_SIZE>, MAP_SIZE> scannedGalaxy;

    int totalBases;
    int totalKlingons;

};

/*
Sample Output

white box test
106 004 006 006 205 005 006 001 <- maps may vary 
104 007 104 106 007 101 005 201 
105 204 004 006 102 208 004 005 
005 003 104 006 002 204 004 007 
003 102 003 005 007 006 101 004 
007 002 008 003 005 006 014 004 
001 004 012 104 206 006 005 003 
003 007 005 107 013 003 004 002 



106 004 006 006 205 005 006 001 
104 007 104 106 007 101 005 201 
105 204 004 006 102 208 004 005 
005 003 104 006 002 204 004 007 
003 102 003 005 007 006 101 004 
007 002 008 003 005 006 014 004 
001 004 012 104 206 006 005 003 
003 007 005 107 013 003 004 002 
Percent of 1 klingons: 18.75 <- percents may vary
Percent of 2 klingons: 9.375
Percent of 3 klingons: 0
Percent of bases: 1.5625
White box test success

*/