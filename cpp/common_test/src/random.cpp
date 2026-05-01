#include <common/random.hpp>
#include <iostream>
#include <cassert>

int main() {
    std::cout << "Testing random number generation\n";

    uint32_t num = common::generateRandom32();

    std::cout << num << "\n";

    num = common::generateRandom32();

    std::cout << num << "\n";

    num = common::randomInt(1, 100);

    assert(num >= 1 && num <= 100);
    std::cout << "Num: " << num << ", expected between 1 and 100\n";

    num = common::randomInt(204, 10012);

    assert(num >= 204 && num <= 10012);
    std::cout << "Num: " << num << ", expected between 204 and 10012\n";

    float f_num = common::generateRandomPercent();
    assert(f_num >= 0.0f && f_num <= 1.0f);

    std::cout << "Random floating point number: " << f_num << ", expected between 0, and 1\n";

    f_num = common::generateRandomPercent();
    assert(f_num >= 0.0f && f_num <= 1.0f);

    std::cout << "Random floating point number: " << f_num << ", expected between 0, and 1\n";

    std::cout << "Random number generation success\n";

    return 0;
}

/* Sample Output

Testing random number generation
1633244240
1818941541
Num: 16, expected between 1 and 100
Num: 7402, expected between 204 and 10012
Random floating point number: 0.0459369, expected between 0, and 1
Random floating point number: 0.487281, expected between 0, and 1
Random number generation success

*/