#pragma once
#include "random.hpp"

/**
 * GameLib.hpp holds all the test drivers. It also holds functions that are used
 * across the Super Star Trek project. Includes:
 * - genKlingons(), genBases(), genStars() (generates the number of klingons, bases, and stars for a quadrant)
 * - testDriver (the main test driver, tests all functions inside the common namespace)
 * 
 */

namespace common {

    int genKlingons();
    int genBases();
    int genStars();

    void genTestDriver();
    void testDriver();
    
} // namespace common

/*

Sample Output

GameLib test driver run
Random test
New random number: 0.111574
New random number (between 1, and 100): 15
New random number (between 1, and 100): 56
Weighted choice output: 0
Weighted choice output: 1
Random test success
Generation test
Number of quadrants with 1 klingon: 20%
Number of quadrants with 2 klingon: 4%
Number of quadrants with 3 klingon: 2%
Number of quadrants with bases: 0%
Time taken: 316 ms
Generation test success
GameLib test driver run success

*/