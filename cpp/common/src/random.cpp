#include "../include/common/random.hpp"
#include <random>
#include <cassert>
#include <iostream>

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


    // generates a random number between 0, and 1
    double random() {
        static std::uniform_real_distribution<double> dist(0.0, 1.0);

        return dist(gen);
    }

    // returns if the random number between 0 and 1 is <= percent
    bool chanceOf(double percent) {
        double r = random();

        return r <= percent;
    }

    // generates a random 32-bit unsigned integer (without any range)
    uint32_t generateRandom32()
    {
        return gen();
    }

    // Generates a random 32-bit unsigned integer with a specific range
    int randomInt(int min, int max)
    {
        assert(min < max);

        std::uniform_int_distribution<int> dist(min, max);
        // if this is used a lot you could add caching for the dist

        return dist(gen);
    }

    // Generates a random double between min, and max
    double randomInRange(double min, double max) {
        assert(min < max);

        return min + (max - min) * random();
    }

    // Generates a random float number between 0, and 1
    float generateRandomPercent() {
        static std::uniform_real_distribution<float> dist(0.0f, 1.0f);
        return dist(gen);
    }

    // Makes a weighted choice outputting the array's index for that chance
    int weightChoice(const std::vector<double>& weights) {
        if (weights.size() == 0)
            throw std::runtime_error("Weights vector is empty");

        double total = 0.0;
        for (double w : weights) {
            if (w < 0)
                throw std::runtime_error("Weights must be non-negative");

            total += w;
        }

        if (total <= 0) 
            std::runtime_error("Total weight must be > 0");

        double r = random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < weights.size(); ++i) {
            cumulative += weights[i];

            if (r < cumulative)
                return i;
        }

        // floating point precision, fallback to last index
        return weights.size() - 1;
    }

    double _rnd;
    /**
     * This function is the MS BASIC function of the same name
     * Specifically, if RND is called with a non-zero value a new random
     * value will be generated and stored in _rnd static variable (retaining its value)
     * across calls. If RND is called with a zero value, the last random value
     * generated is returned.
     *  
     * NB: support for setting the seed ( < 0 parameter value) has been omitted.
     */
    double RND(int n) {
        if( n != 0 ) 
            _rnd = random();  // generates a new rand
        
        return _rnd;                        // otherwise returns last
    }
    
    double RND() { 
        return RND(1); 
    }

    // tests the random number generators functions work
    void randomTestDriver() {
        std::cout << "Random test\n";

        double r = random();
        assert(r >= 0 && r <= 1);
        std::cout << "New random number: " << r << "\n";

        double ra = randomInt(1, 100);
        assert(ra >= 1 && ra <= 100);
        std::cout << "New random number (between 1, and 100): " << ra << "\n";
        
        double rb = randomInt(1, 100);
        assert(rb >= 1 && rb <= 100);
        std::cout << "New random number (between 1, and 100): " << rb << "\n";

        double rc = randomInRange(1, 100);
        assert(rc >= 1 && rc <= 100);
        std::cout << "Third new random number (between 1, and 100): " << rc << "\n";

        int i = weightChoice({0.73, 0.2, 0.05, 0.02});
        assert(i >= 0 && i <= 3);
        std::cout << "Weighted choice output: " << i << "\n";

        int ii = weightChoice({0.43, 0.4, 0.17});
        assert(ii >= 0 && ii <= 2);
        std::cout << "Weighted choice output: " << ii << "\n";

        std::cout << "Random test success\n";
    }

} // namespace common
