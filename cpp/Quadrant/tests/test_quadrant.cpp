#include "../src/quadrant.hpp"

#include <iostream>

int main() {
    Quadrant q(365);

    std::cout << "klingons: " << q.klingons() << "\n";
    std::cout << "bases: " << q.bases() << "\n";
    std::cout << "stars: " << q.stars() << "\n";

    return 0;
}