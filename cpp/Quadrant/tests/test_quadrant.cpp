#include "../src/quadrant.hpp"

#include <iostream>

int main() {
    for (size_t i = 0; i < 64; ++i) {
        Quadrant q;
    
        std::cout << "klingons: " << q.klingons() << "\n";
        std::cout << "bases: " << q.bases() << "\n";
        std::cout << "stars: " << q.stars() << "\n";
    }

    try {
        Quadrant q(402);
        Quadrant qe(2, 1, 5);
        Quadrant qr(3, 4, 5);
    } catch (const std::exception& e) {
        std::cout << "Error occurred: " << e.what() << "\n";
    }

    Quadrant q(319);
    std::cout << "klingons: " << q.klingons() << "\n";
    std::cout << "bases: " << q.bases() << "\n";
    std::cout << "stars: " << q.stars() << "\n";

    q.setKlingons(5);
    q.setBases(100);
    q.setStars(-24);

    std::cout << "klingons: " << q.klingons() << "\n";
    std::cout << "bases: " << q.bases() << "\n";
    std::cout << "stars: " << q.stars() << "\n";

    return 0;
}