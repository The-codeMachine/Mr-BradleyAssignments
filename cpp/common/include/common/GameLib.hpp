#pragma once
#include "random.hpp"

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

    bool isBetween();

    void isBetweenTest();
    void testDriver();
    
} // namespace common

/*

Sample Output

Random test
New random number: 0.43 <- random numbers, so they may change
New random number (between 1, and 100): 25.00
Second new random number (between 1, and 100): 43.00
Second new random number (between 1, and 100): 63.84
Weighted choice output: 0
Weighted choice output: 2
Random test success
Is between test
Is between test success
GameLib test driver run success

*/