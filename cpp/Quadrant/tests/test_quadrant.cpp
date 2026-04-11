#include "../src/quadrant.hpp"

#include <iostream>
#include <chrono>

// generate a quadrant with all the constructors, and ensures they work
void testQuadrantConstructors() {
    try {
        Quadrant q;

        std::cout << q << "\n";

        Quadrant qw(3, 1, 2);

        std::cout << qw << "\n";

        Quadrant qe(3, 1, 2);

        std::cout << qe << "\n";

        Quadrant qr(4, 9, 3);

        std::cout << qr << "\n";
    } catch (const std::exception& e) {
        std::cout << "There was a runtime exception: " << e.what() << "\n";
    }
}

// ensures all the getters are working correctly
void testQuadrantGetters() {
    Quadrant q(3, 1, 8);

    std::cout << "Klingons: " << q.klingons() << "\n";
    std::cout << "Bases: " << q.bases() << "\n";
    std::cout << "Stars: " << q.stars() << "\n";
}

// ensures all the setters are working corretly
void testQuadrantSetters() {
    Quadrant q(3, 1, 8);

    std::cout << q << "\n";

    q.setKlingons(1);
    q.setBases(0);
    q.setStars(7);

    std::cout << q << "\n";
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

int main() {
    testQuadrantConstructors();
    testQuadrantGetters();
    testQuadrantSetters();
    stressTestRandomGenerator();

    #ifndef NDEBUG

        Quadrant q;

        q.whiteBoxTest();

    #endif

    return 0;
}

/* Sample Output

Klingons: 2, Bases: 1, Stars: 6 <- this may change
Klingons: 3, Bases: 1, Stars: 2
Klingons: 3, Bases: 1, Stars: 2
There was a runtime exception: Klingon exceed MAX, or dropped under MIN
Klingons: 3
Bases: 1
Stars: 8
Klingons: 3, Bases: 1, Stars: 8
Klingons: 1, Bases: 0, Stars: 7
Time taken: 51 ms <- this may change
There was a runtime exception, success

 */