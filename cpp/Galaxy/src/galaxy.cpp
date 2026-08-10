#include "Galaxy.hpp"

#include <common/GameLib.hpp>
#include <common/IO.hpp>

#include <iostream>
#include <cassert>
#include <iomanip>
#include <string>

Galaxy::Galaxy() : totalBases(0), totalKlingons(0)
{
    populateGalaxy();
}

// Gets the quadrant located at [index][index2]. Takes base-1 coordinates
// (reference)
Quadrant& Galaxy::getQuadrant(int x, int y)
{
    x = common::toBase0(x);
    y = common::toBase0(y);
    
    assert(validIndex(x) && validIndex(y));
    return map[x][y];
}

// Gets the quadrant located at [index][index2]. Takes base-1 coordinates.
// (const reference)
const Quadrant& Galaxy::getQuadrant(int x, int y) const 
{
    x = common::toBase0(x);
    y = common::toBase0(y);
    
    assert(validIndex(x) && validIndex(y));
    return map[x][y];
}

// Gets a Quadrant from the map. Takes the base-0 coordinates through location 
// (reference). 
Quadrant& Galaxy::getQuadrant(common::Location loc) {
    assert(validIndex(loc.quadrantX) && validIndex(loc.quadrantY));
    
    return map[loc.quadrantX][loc.quadrantY];
}

// Gets a Quadrant from the map. Takes the base-0 coordinates through location
// (const reference)
const Quadrant& Galaxy::getQuadrant(common::Location loc) const {
    assert(validIndex(loc.quadrantX) && validIndex(loc.quadrantY));
    
    return map[loc.quadrantX][loc.quadrantY];
}

// Returns the galatic region name of a particular quadrant. Takes base-1 coordinates.
std::string Galaxy::getQuadrantRegionName(int x, int y) {
    return getQuadrantRegionName({-1, -1, common::toBase0(x), common::toBase0(y)});
}

// Returns the galatic region name of a particular quadrant. Takes base-0 coordinates
// through Location.
std::string Galaxy::getQuadrantRegionName(common::Location location) {
    const int x = location.quadrantX;
    const int y = location.quadrantY;
    
    if (!validIndex(x) || !validIndex(y))
        return "";

    return std::string(GALATIC_REGION_NAMES[y][static_cast<int>(x > 4)]);
}

// Gets the Quadrant's roman numeral for a particular region. Takes base-1 coordinates.
std::string Galaxy::getQuadrantRomanNumeral(int x, int y) {
    return getQuadrantRomanNumeral({-1, -1, common::toBase0(x), common::toBase0(y)});
}

// Gets the Quadrant's roman numeral for a particular region. Takes base-0 coordinates
// through Location.
std::string Galaxy::getQuadrantRomanNumeral(common::Location location) {
    const int x = location.quadrantX;
    const int y = location.quadrantY;
    
    if (!validIndex(x) || !validIndex(y))
        return "";

    return std::string(numerals[x % 4]);    
}

// Gets a quadrant's full galatic region name based off a location. Takes base-1 
// coordinates.
std::string Galaxy::getGalaticRegionName(int x, int y) {
    return getGalaticRegionName({-1, -1, common::toBase0(x), common::toBase0(y)});
}

// Gets a quadrant's full galatic region name based off a location. Takes base-0 
// coordinates through Location.
std::string Galaxy::getGalaticRegionName(common::Location location) {
    std::string name = getQuadrantRegionName(location);
    if (name.empty())
        return "";

    name += ' ';
    name += getQuadrantRomanNumeral(location);

    return name.empty() ? "" : name;
}

// Prints the galatic region map. This includes only the names of the regions, and
// not their roman numerals. 
void Galaxy::printGalaticRegionMap() {
    common::IO::println(common::padLeft(common::padCenter("The Galaxy", 48), 52));
    common::IO::println(common::padLeft("  1     2     3     4     5     6     7     8  ", 52));
    common::IO::println(common::padLeft("----- ----- ----- ----- ----- ----- ----- -----", 52));

    for (int y = MIN_INDEX; y <= MAX_INDEX; ++y) {
        common::IO::printf("%d   %s%s\n",
                            common::toBase1(y), 
                            common::padCenter(getQuadrantRegionName({-1, -1, 0, y}), 24).c_str(), 
                            common::padCenter(getQuadrantRegionName({-1, -1, 5, y}), 24).c_str()
                        );
        common::IO::println(common::padLeft("----- ----- ----- ----- ----- ----- ----- -----", 52));
    }
    
}

// Gets and returns the total number of starbases in the galaxy 
int Galaxy::starBases() const noexcept {
    return totalBases;
}

// Gets and returns the total number of klingons in the galaxy
int Galaxy::getKlingons() const noexcept {
    return totalKlingons;
}

// Reduces the amount of klingons in both the specific Quadrant and the
// total number of klingons. Checks that there is actually a klingon in
// that quadrant. Takes base-1 coordinates. 
void Galaxy::reduceKlingons(int x, int y) {
    Quadrant& q = getQuadrant(x, y);
    if (q.klingons() >= 1 && totalKlingons > 0) {
        q.reduceKlingons();
        totalKlingons -= 1;
    }
}

// Reduces the amount of klingons in both the specific Quadrant and the
// total number of klingons. Checks that there is actually a klingon in
// that quadrant. Takes base-0 coordinates through Location. 
void Galaxy::reduceKlingons(common::Location location) {
    reduceKlingons(common::toBase1(location.quadrantX), common::toBase1(location.quadrantY));
}

// Makes a long range scan around the Enterprise (inputted as location). 
// Updates the scanned galaxy. 
void Galaxy::longRangeScan(common::Location location) {
    const int startY = std::clamp(location.quadrantY - 1, MIN_INDEX, MAX_INDEX);
    const int endY   = std::clamp(location.quadrantY + 1, MIN_INDEX, MAX_INDEX);
    const int startX = std::clamp(location.quadrantX - 1, MIN_INDEX, MAX_INDEX);
    const int endX   = std::clamp(location.quadrantX + 1, MIN_INDEX, MAX_INDEX);

    for (int y = startY; y <= endY; ++y) {
        for (int x = startX; x <= endX; ++x)
            common::IO::print("+-----");

        common::IO::println("+");

        for (int x = startX; x <= endX; ++x) {
            Quadrant q = getQuadrant(common::toBase1(x), common::toBase1(y));
            common::IO::print("| " + q.toString() + " ");
            scannedGalaxy[y][x] = q;
        }
        common::IO::println("|");
    }

    for (int i = startX; i <= endX; ++i) 
        common::IO::print("+-----");

    common::IO::println("+");
}

// Prints the entire scanned galaxy. 
void Galaxy::printScannedGalaxy() const noexcept {
    common::IO::println("\n+-----+-----+-----+-----+-----+-----+-----+-----+");

    for (int y = 0; y < MAP_SIZE; ++y) {
        for (int x = 0; x < MAP_SIZE; ++x) {
            auto q = scannedGalaxy[y][x];
            if (q == std::nullopt) {
                common::IO::printf("| --- ");
                continue;
            }

            common::IO::printf("| %s ", q.value().toString().c_str());
        }

        common::IO::println("|\n+-----+-----+-----+-----+-----+-----+-----+-----+");
    }
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

            totalKlingons += map[i][j].klingons();
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

// Checks whether index is a valid index for the galaxy. Takes base-0 coordinates. 
bool Galaxy::validIndex(int index) {
    return index >= MIN_INDEX && index <= MAX_INDEX;
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