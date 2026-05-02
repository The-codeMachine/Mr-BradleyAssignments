#include "../include/common/GameLib.hpp"
#include <iostream>
#include <chrono>
#include <cassert>

namespace common
{

    int totalBases = 0;

    // Generates the number of klingons in a quadrant
    int genKlingons()
    {
        // klingons: 0 1 2 3
        // 73% 20% 5% 2%
        int r = weightChoice({0.73, 0.2, 0.05, 0.02});
        return r;
    }

    // Generates the number of bases in a quadrant
    int genBases()
    {
        // 4% chance of a quadrant having a base
        if (common::random() <= 0.04)
        {
            return 1;
        }

        return 0;
    }

    // Randomly generates a random number of stars between 1-8
    int genStars()
    {
        return common::randomInt(1, 8);
    }

    void genTestDriver()
    {
        std::cout << "Generation test\n";

        auto start = std::chrono::steady_clock::now();

        static constexpr int ITERATIONS = 1000000; // iterations high to reduce noise
        int numOf1Klingons = 0;
        int numOf2Klingons = 0;
        int numOf3Klingons = 0;
        int numOfBases = 0;
        for (size_t i = 0; i < ITERATIONS; ++i)
        {
            double r = genKlingons();

            if (r == 1)
                numOf1Klingons++;
            else if (r == 2)
                numOf2Klingons++;
            else if (r == 3)
                numOf3Klingons++;

            if (genBases() == 1)
                numOfBases++;
        }

        auto end = std::chrono::steady_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

        float percent1 = numOf1Klingons * 100 / ITERATIONS;
        float percent2 = numOf2Klingons * 100 / ITERATIONS;
        float percent3 = numOf3Klingons * 100 / ITERATIONS;
        float percent4 = numOfBases * 100 / ITERATIONS;

        std::cout << "Number of quadrants with 1 klingon: " << percent1 << "%\n";
        std::cout << "Number of quadrants with 2 klingon: " << percent2 << "%\n";
        std::cout << "Number of quadrants with 3 klingon: " << percent3 << "%\n";
        std::cout << "Number of quadrants with bases: " << percent4 << "%\n";

        std::cout << "Time taken: " << duration.count() << " ms\n";

        std::cout << "Generation test success\n";
    }

    void testDriver()
    {
        std::cout << "GameLib test driver run\n";

        randomTestDriver();
        genTestDriver();

        std::cout << "GameLib test driver run success\n";
    }

} // namespace common