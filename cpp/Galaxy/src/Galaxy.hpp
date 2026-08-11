#pragma once

#include <common/GameLib.hpp>

#include <quadrant.hpp>

#include <array>
#include <optional>
#include <string>
#include <string_view>
#include <ostream>

/**
 * 
 * The Galaxy represents this game's world. It encapsulates
 * 64 quadrants in an 8 x 8 grid. Every quadrant is accessible
 * through the getQuadrant function. All functions take base-1
 * coordinates or a Location (using base-0). 
 * 
 * The Galaxy ensures there is a maximum of 2 star bases and a 
 * minimum of 1 star base. You can get the total number of 
 * klingons in the galaxy, and reduce klingons from a specific
 * quadrant. 
 * 
 * The Galaxy implements the long range scan, and scanned galaxy
 * for the Enterprise. Based off previous long range scans the
 * Enterprise can see more of the scanned galaxy. This only 
 * updates if the Enterprise scans that quadrant again (like the
 * original game).
 * 
 * Every quadrant has a galatic region name, and galatic region
 * roman numeral. These can be accessed statically (since it does
 * not change between different galaxies). You can get a quadrant's
 * full name using the getGalaticRegionName function. 
 * 
 * Using the functions mentioned above, Galaxy allows you to print
 * the galatic region name map. 
 * 
 */
class Galaxy {
public:
    Galaxy();

    Quadrant& getQuadrant(int x, int y);
    const Quadrant& getQuadrant(int x, int y) const;

    Quadrant& getQuadrant(common::Location loc);
    const Quadrant& getQuadrant(common::Location loc) const;

    static std::string getQuadrantRegionName(int x, int y);
    static std::string getQuadrantRegionName(common::Location location);

    static std::string getQuadrantRomanNumeral(int x, int y);
    static std::string getQuadrantRomanNumeral(common::Location location);

    static std::string getGalaticRegionName(int x, int y);
    static std::string getGalaticRegionName(common::Location location);

    static void printGalaticRegionMap();

    int starBases() const noexcept;
    void reduceStarBases(int x, int y);
    void reduceStarBases(common::Location location);
    
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

    static bool validIndex(int index);

private:
    static inline constexpr int MIN_INDEX = 0;
    static inline constexpr int MAX_INDEX = 7;

    static inline constexpr int MAP_SIZE = 8;

    static inline constexpr std::array<std::array<std::string_view, 2>, 8> GALATIC_REGION_NAMES = {{
        {"ANTARES",     "SIRIUS"},
        {"RIGEL",       "DENEB"},
        {"PROCYON",     "CAPELLA"},
        {"VEGA",        "BETELGEUSE"},
        {"CANOPUS",     "ALDEBARAN"},
        {"ALTAIR",      "REGULUS"},
        {"SAGITTARIUS", "ARCTURUS"},
        {"POLLUX",      "SPICA"}
    }};

    static inline constexpr std::array<std::string_view, 4> numerals = {
        "I", "II", "III", "IV"
    };

private:
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