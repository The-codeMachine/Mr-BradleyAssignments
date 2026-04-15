#include "../src/quadrant.hpp"

#include <iostream>
#include <sstream>
#include <cassert>
#include <chrono>

// ensures all the getters are working correctly
void testQuadrantGetters()
{
    std::cout << "Testing Quadrant getters\n";

    Quadrant q(3, 1, 8);

    assert(q.klingons() == 3 && q.bases() == 1 && q.stars() == 8);

    std::cout << "Quadrant getters success\n";
}

// generate a quadrant with all the constructors, and ensures they work
void testQuadrantConstructors()
{
    std::cout << "Testing Quadrant constructors\n";

    Quadrant q;

    assert(q.klingons() <= 3 && q.klingons() >= 0);
    assert(q.bases() <= 1 && q.bases() >= 0);
    assert(q.stars() <= 9 && q.stars() >= 1);

    Quadrant qw(3, 1, 2);

    assert(qw.klingons() == 3 && qw.bases() == 1 && qw.stars() == 2);

    std::cout << "Quadrant constructor success\n";
}

// tests that the << operator works correctly
void testOperator()
{
    std::cout << "Testing Quadrant << operator\n";
    
    Quadrant q(3, 1, 8);

    std::ostringstream oss;
    oss << q;

    std::string result = oss.str();

    assert(result == "318");

    Quadrant qu(0, 0, 1);

    oss.clear();
    oss << qu;

    result = oss.str();

    assert(result == "001");

    std::cout << "Quadrant << operator success\n";
}

// test that the reduceKlingon function removes klingons correctly
void testRemoveKlingons()
{
    std::cout << "Testing Quadrant reduceKlingons\n";

    Quadrant q(3, 0, 1);

    q.reduceKlingons();

    assert(q.klingons() == 2);

    q.reduceKlingons();

    assert(q.klingons() == 1);

    q.reduceKlingons();

    assert(q.klingons() == 0);

    q.reduceKlingons();

    assert(q.klingons() == 0);

    std::cout << "Quadrant reduceKlingons success\n";
}

// stress tests the random number generator
void stressTestRandomGenerator()
{
    std::cout << "Quadrant stress test\n";

    auto start = std::chrono::steady_clock::now();

    for (size_t j = 0; j < 1000000; ++j)
    {
        Quadrant q;
    }

    auto end = std::chrono::steady_clock::now();

    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

    std::cout << "Time taken: " << duration.count() << " ms\n";

    std::cout << "Quadrant stress test success\n";
}

int main()
{
    testQuadrantGetters();
    testQuadrantConstructors();
    testOperator();
    testRemoveKlingons();
    stressTestRandomGenerator();

#ifndef NDEBUG

    Quadrant::whiteBoxTest();

#endif

    return 0;
}

/* Sample Output

Testing Quadrant getters
Quadrant getters success
Testing Quadrant constructors
Quadrant constructor success
Testing Quadrant << operator
318
001
Quadrant << operator success
Testing Quadrant reduceKlingons
Quadrant reduceKlingons success
Quadrant stress test
Time taken: 9 ms <- this one may change depending on your system hardware
Quadrant stress test success
Quadrant whitebox test
Quadrant whitebox test success

 */