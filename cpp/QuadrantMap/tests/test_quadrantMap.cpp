#include <QuadrantMap.hpp>

#include <iostream>

int main() {
    std::cout << "QuadrantMap test\n";
    
    QuadrantMap m(3, 1, 2);

    std::cout << m << "\n";

    std::cout << "(4, 2): < " << m.at(4, 2) << " >\n";
    std::cout << "Is (4, 2) empty: " << m.empty(4, 2) << "\n";

    std::cout << "QuadrantMap test success\n";

    return 0;
}

/*
Sample Output

QuadrantMap test
----------------------------------
   |   |   |   |   |   |>!<|   |
----------------------------------
   | * |   |   |+K+|   |   |   |
----------------------------------
   |   |   |   |+K+|   |   |   |
----------------------------------
   |   |<*>|   |   |   |   |   |
----------------------------------
   |   |   |+K+|   |   |   |   |
----------------------------------
   | * |   |   |   |   |   |   |
----------------------------------
   |   |   |   |   |   |   |   |
----------------------------------
   |   |   |   |   |   |   |   |

(4, 2): < +K+ >
Is (4, 2) empty: 0
QuadrantMap test success

*/