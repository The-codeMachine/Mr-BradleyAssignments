#pragma once
#include "random.hpp"
#include "StringUtils.hpp"

/**
 * 
 * GameLib holds a bunch of random number generation functions, 
 * as well as a few test drivers, and verification tests. 
 * 
 * Operations include:
 *  - Generate a random number between 0, and 1
 *  - Generate a random int between min, and max
 *  - Generate a random number between min, and max
 *  - Make a weighted choice 
 *  - Check if a number is between two
 * 
 */


namespace common {

    /**
     * 
     * Location is a helper class which represents a complete
     * position within this game. It includes quadrant
     * coordinates, and sector coordinates. Both are valid 
     * [0, 7]. As such, this class represents base-0 coordinates.
     * Everything is stored in (column, row) notation. 
     * 
     * Construction does not check or validate the coordinates 
     * inputted. 
     * 
     * -1 is a valid value. It represents something that is not
     * represented. toString will skip any value set as -1. E.g.
     * if one of the quadrant coordinates are set to -1 it will 
     * only print the sector coordinates (given that neither of
     * those are equal to -1 as well).
     * 
     * Output through toString prints in (row, column) notation
     * as base-1. This is to help the users understand it better. 
     * 
     */
    class Location {
    public:
        Location();
        Location(int sectorX, int sectorY, int quadrantX, int quadrantY);

        bool operator!=(const Location& other) const;
        bool operator==(const Location& other) const;

        bool sameQuadrant(const Location& other) const;
        bool sameSector(const Location& other) const;

        std::string toString() const;

        int quadrantX;
        int quadrantY;

        int sectorX;
        int sectorY;
    };

    bool isBetween();

    int toBase0(int c);
    int toBase1(int c);

    double radians(double degrees);
    double degrees(double radian);

    void isBetweenTest();
    void testDriver();

    // represents the size of one size
    static inline constexpr int MAP_SIZE = 8;
    
    // the number of rows
    static inline constexpr int ROWS = 8;
    
    // the number of columns
    static inline constexpr int COLS = 8;
    
    // the 0-based indices
    static inline constexpr int MIN_INDEX_0 = 0;
    static inline constexpr int MAX_INDEX_0 = 7;

    // the 1-based indices
    static inline constexpr int MIN_INDEX_1 = 1;
    static inline constexpr int MAX_INDEX_1 = 8;

    
} // namespace common

/*

Sample Output

GameLib test driver run
Random test
New random number: 0.934307
New random number (between 1, and 100): 48
New random number (between 1, and 100): 68
Third new random number (between 1, and 100): 6.08033
Weighted choice output: 1
Weighted choice output: 0
Random test success
Is between test
Is between test succes
String utils test 
Message padded left: >    something cool<
Message padded right: >something cool      <
Message padded center: >   something cool   <
Zero filled (str): 00123
String utils test success
GameLib test driver run success

*/