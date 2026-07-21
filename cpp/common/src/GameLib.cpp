#include "../include/common/GameLib.hpp"
#include <iostream>
#include <chrono>
#include <cassert>

namespace common
{

    bool isBetween(double value, double low, double high) {
        return low <= value && value <= high;
    }

    // Converts c to base-0. Exepcts a 
    // base-1 argument. 
    int toBase0(int c) {
        return c - 1;
    }

    // Converts c to base-1. Exepcts a 
    // base-0 argument. 
    int toBase1(int c) {
        return c + 1;
    }

    void isBetweenTest()
    {
        std::cout << "Is between test\n";

        assert(isBetween(4, 1, 10));

        assert(!isBetween(-4, 1, 10));

        assert(!isBetween(14, 1, 10));

        std::cout << "Is between test succes\n";
    }

    void testDriver()
    {
        std::cout << "GameLib test driver run\n";

        randomTestDriver();
        isBetweenTest();
        stringUtilsTestDriver();

        std::cout << "GameLib test driver run success\n";
    }

} // namespace common