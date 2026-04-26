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

// generates a galaxy
void generatesAGalaxy()
{
    std::cout << "Quadrant galaxy construction test\n";

    auto start = std::chrono::steady_clock::now();

    static constexpr int ITERATIONS = 1000000; // iterations high to reduce noise 
    int numOf1Klingons = 0;
    int numOf2Klingons = 0;
    int numOf3Klingons = 0;
    int numOfBases = 0;
    for (size_t i = 0; i < ITERATIONS; ++i)
    {
        Quadrant q;

        if (q.klingons() == 1)
            numOf1Klingons++;
        else if (q.klingons() == 2)
            numOf2Klingons++;
        else if (q.klingons() == 3)
            numOf3Klingons++;

        if (q.bases() == 1)
            numOfBases++;
    }
    
    auto end = std::chrono::steady_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
    
    assert(numOfBases >= 1 && numOfBases <= 2);
    
    float percent1 = numOf1Klingons * 100 / ITERATIONS;
    float percent2 = numOf2Klingons * 100 / ITERATIONS;
    float percent3 = numOf3Klingons * 100 / ITERATIONS;
    float percent4 = numOfBases * 100 / ITERATIONS;

    std::cout << "Number of quadrants with 1 klingon: "  << percent1 << "%\n";
    std::cout << "Number of quadrants with 2 klingon: "  << percent2 << "%\n";
    std::cout << "Number of quadrants with 3 klingon: "  << percent3 << "%\n";
    std::cout << "Number of quadrants with bases: "  << percent4 << "%\n";

    std::cout << "Time taken: " << duration.count() << " ms\n";

    std::cout << "Quadrant galaxy construction success\n";
}

int main()
{
    testQuadrantGetters();
    testQuadrantConstructors();
    testOperator();
    testRemoveKlingons();
    generatesAGalaxy();

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
Got 0 klingons, expected between 0-3
Got 0 bases, expected between 0-1
Got 8 stars, expected between 1-8
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
Quadrant galaxy construction test
Number of quadrants with 1 klingon: 20% <- these may change due to statistical noise
Number of quadrants with 2 klingon: 5% <- these may change due to statistical noise
Number of quadrants with 3 klingon: 2% <- these may change due to statistical noise
Number of quadrants with bases: 0% <- these may change due to statistical noise
Time taken: 49 ms <- this one may change based off your system's hardware
Quadrant galaxy construction success
Quadrant whitebox test
Got 318, expected 318
Got 212, expected 212
Quadrant whitebox test success

 */