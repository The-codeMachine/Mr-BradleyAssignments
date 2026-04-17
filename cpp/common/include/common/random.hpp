#pragma once
#include <cstdint>

/**
 * random.hpp includes operations for generating pseudo-random numbers
 * random.hpp's operations can be found inside the common namespace
 * 
 * Current operations include:
 *  - Generating a random 32-bit unsigned integer without a given range
 *  - Generating a random 32-bit unsigned integer with a given range (min, and a max)
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

    uint32_t generateRandom32();
    uint32_t generateRandom32Range(int min, int max);
} // namespace common