#include "../src/quadrant.hpp"

#include <iostream>
#include <cassert>
#include <chrono>

// generate a quadrant with all the constructors, and ensures they work
void testQuadrantConstructors() {
    Quadrant q;

    std::cout << q << "\n";

    Quadrant qw(3, 1, 2);

    std::cout << qw << "\n";

    Quadrant qe(3, 1, 2);

    std::cout << qe << "\n";
}

// ensures all the getters are working correctly
void testQuadrantGetters() {
    Quadrant q(3, 1, 8);

    std::cout << "Klingons: " << q.klingons() << "\n";
    std::cout << "Bases: " << q.bases() << "\n";
    std::cout << "Stars: " << q.stars() << "\n";
}

// stress tests the random number generator
void stressTestRandomGenerator() {
    auto start = std::chrono::steady_clock::now();
    
    for (size_t j = 0; j < 1000000; ++j) {
        Quadrant q;
    }

    auto end = std::chrono::steady_clock::now();

    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

    std::cout << "Time taken: " << duration.count() << " ms\n";
}

// test that the reduceKlingon function removes klingons correctly
void testRemoveKlingons() {
    Quadrant q(3, 0, 1);

    q.reduceKlingons();
    
    std::cout << q << "\n";

    q.reduceKlingons();
                
    std::cout << q << "\n";


    q.reduceKlingons();
    
    std::cout << q << "\n";

    q.reduceKlingons();
}

void testOperator() {
    Quadrant q(3, 1, 8);

    std::cout << q << "\n";

    Quadrant qu(0, 0, 1);

    std::cout << qu << "\n";
}

int main() {
    testQuadrantConstructors();
    testQuadrantGetters();
    stressTestRandomGenerator();
    testOperator();

    #ifndef NDEBUG

        Quadrant::whiteBoxTest();

    #endif

    return 0;
}

/* Sample Output

003 <- this one may change
312
312
Klingons: 3
Bases: 1
Stars: 8
Time taken: 9 ms <- this one may change
318
001
Assertion failed: klingons >= KLINGON_MIN && klingons <= KLINGON_MAX && "Klingon out of range", file D:\Developer\Mr-BradleyAssignments\cpp\Quadrant\src\quadrant.cpp, line 81 <- this one may change based off where your file is located

 */