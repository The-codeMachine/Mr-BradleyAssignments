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
    std::cout << "Got " << q.klingons() << " klingons, expected 3\n";
    std::cout << "Got " << q.bases() << " bases, expected 1\n";
    std::cout << "Got " << q.stars() << " stars, expected 8\n";

    std::cout << "Quadrant getters success\n";
}

// generate a quadrant with all the constructors, and ensures they work
void testQuadrantConstructors()
{
    std::cout << "Testing Quadrant constructors\n";

    Quadrant q;

    assert(q.klingons() <= 3 && q.klingons() >= 0);
    std::cout << "Got " << q.klingons() << " klingons, expected between 0-3\n";

    assert(q.bases() <= 1 && q.bases() >= 0);
    std::cout << "Got " << q.bases() << " bases, expected between 0-1\n";

    assert(q.stars() <= 9 && q.stars() >= 1);
    std::cout << "Got " << q.stars() << " stars, expected between 1-8\n";

    Quadrant qw(3, 1, 2);

    assert(qw.klingons() == 3 && qw.bases() == 1 && qw.stars() == 2);
    std::cout << "Got Quadrant: Klingons(" << qw.klingons() << "), Bases(" << qw.bases() << "), Stars(" << qw.stars() << "), expected Quadrant: Klingons(3), Bases(1), Stars(2)\n";

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
    std::cout << "Got " << result << ", expected \"318\"\n";

    Quadrant qu(0, 0, 1);

    oss.str("");
    oss.clear();
    oss << qu;

    result = oss.str();

    assert(result == "001");
    std::cout << "Got " << result << ", expected \"001\"\n";

    std::cout << "Quadrant << operator success\n";
}

// test that the reduceKlingon function removes klingons correctly
void testRemoveKlingons()
{
    std::cout << "Testing Quadrant reduceKlingons\n";

    Quadrant q(3, 0, 1);

    q.reduceKlingons();

    assert(q.klingons() == 2);
    std::cout << "Got " << q.klingons() << " klingons, expected 2\n";

    q.reduceKlingons();

    assert(q.klingons() == 1);
    std::cout << "Got " << q.klingons() << " klingons, expected 1\n";

    q.reduceKlingons();

    assert(q.klingons() == 0);
    std::cout << "Got " << q.klingons() << " klingons, expected 0\n";

    q.reduceKlingons();

    assert(q.klingons() == 0);
    std::cout << "Got " << q.klingons() << " klingons, expected 0\n";

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
Got 3 klingons, expected 3
Got 1 bases, expected 1
Got 8 stars, expected 8
Quadrant getters success
Testing Quadrant constructors
Got 1 klingons, expected between 0-3
Got 1 bases, expected between 0-1
Got 1 stars, expected between 1-8
Got Quadrant: Klingons(3), Bases(1), Stars(2), expected Quadrant: Klingons(3), Bases(1), Stars(2)
Quadrant constructor success
Testing Quadrant << operator
Got 318, expected "318"
Got 001, expected "001"
Quadrant << operator success
Testing Quadrant reduceKlingons
Got 2 klingons, expected 2
Got 1 klingons, expected 1
Got 0 klingons, expected 0
Got 0 klingons, expected 0
Quadrant reduceKlingons success
Quadrant stress test
Time taken: 9 ms
Quadrant stress test success
Quadrant whitebox test
Got 318, expected 318
Got 212, expected 212
Quadrant whitebox test success

 */