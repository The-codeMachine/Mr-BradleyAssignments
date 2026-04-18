#include "../include/common/random.hpp"
#include <random>
#include <cassert>

namespace common
{

    // Generates a random 32-bit value using a Mersenne Twister PRNG
    // std::mt19937 is a fast, deterministic pseudo-random number generator
    // with a very long period (2^19937-1). It is seeded using
    // std::random_device to introduce non-determinism
    static std::mt19937 gen([]
                            {
    std::random_device rd; // non-deterministic seed source
    return std::mt19937(rd()); }());

    // generates a random 32-bit unsigned integer (without any range)
    uint32_t generateRandom32()
    {
        return gen();
    }

    // Generates a random 32-bit unsigned integer with a specific range
    uint32_t generateRandom32Range(int min, int max)
    {
        assert(min < max);

        std::uniform_int_distribution<int> dist(min, max);
        // if this is used a lot you could add caching for the dist

        return dist(gen);
    }

    // Generates a random float number between 0, and 1
    float generateRandomPercent() {
        static std::uniform_real_distribution<float> dist(0.0f, 1.0f);
        return dist(gen);
    }

} // namespace common