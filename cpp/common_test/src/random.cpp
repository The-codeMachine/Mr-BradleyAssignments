#include <common/random.hpp>
#include <iostream>
#include <cassert>

int main() {
    std::cout << "Testing random number generation\n";

    uint32_t num = common::generateRandom32();

    std::cout << num << "\n";

    num = common::generateRandom32();

    std::cout << num << "\n";

    num = common::generateRandom32Range(1, 100);

    assert(num >= 1 && num <= 100);
    std::cout << "Num: " << num << ", expected between 1 and 100\n";

    num = common::generateRandom32Range(204, 10012);

    assert(num >= 204 && num <= 10012);
    std::cout << "Num: " << num << ", expected between 204 and 10012\n";

    std::cout << "Random number generation success\n";

    return 0;
}

/* Sample Output

Testing random number generation
2692175046
3808816226
Num: 11, expected between 1 and 100
Num: 3710, expected between 204 and 10012
Random number generation success

*/