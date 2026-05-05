#pragma once
#include <cstdint>
#include <vector>

/**
 * random.hpp includes operations for generating pseudo-random numbers
 * random.hpp's operations can be found inside the common namespace
 * 
 * Current operations include:
 *  - Generating a random 32-bit unsigned integer without a given range
 *  - Generating a random 32-bit signed integer with a given range (min, and a max)
 *  - Generating a random float between 0, and 1
 *  - Generating a random double between 0, and 1
 *  - Generates a random choice between options (all combining into 100%)
 *  - A test driver for all of the above operations
 * 
 * Both PRNG functions use mt19937 which is a Mersenne Twister PRNG. It has a period of 2^19937-1
 * 
 * The mt19937 generator is statically created, and public inside the common namespace.
 * It is created using a lambda which allows for multi-step initialization. It 
 * encapsulates the creation of a temporary std::random_device for a non-deterministic
 * seed. 
 * 
 */

namespace common
{

    double chanceOf();

    uint32_t generateRandom32();
    int randomInt(int min, int max);
    double randomInRange(double min, double max);

    float generateRandomPercent();

    int weightChoice(const std::vector<double>& weights);

    double RND();

    void randomTestDriver();

} // namespace common

/*

Sample Output

Random test
New random number: 0.111574
New random number (between 1, and 100): 15
New random number (between 1, and 100): 56
Weighted choice output: 0
Weighted choice output: 1
Random test success

*/