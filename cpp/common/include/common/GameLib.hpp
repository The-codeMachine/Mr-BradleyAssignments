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
     * This is a helper class which represents a 
     * paired (x, y) coordinate. This allows public
     * access to its x, and y value. You can 
     * construct it with an inital x, and y value, or
     * none. If the location is unassigned it is = -1.
     * 
     * Both coordinates are stored as base-0. 
     * 
     */
    class Location {
    public:
        Location();
        Location(int sectorX, int sectorY, int quadrantX, int quadrantY);

        bool operator==(const Location& other) const;

        std::string toString() const;

        int quadrantX;
        int quadrantY;

        int sectorX;
        int sectorY;
        
    private:
        static constexpr int MIN = 0;
        static constexpr int MAX = 7;
    };

    bool isBetween();

    int toBase0(int c);
    int toBase1(int c);

    void isBetweenTest();
    void testDriver();
    
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