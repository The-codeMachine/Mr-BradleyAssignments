#include "../../common/include/common/GameLib.hpp"
#include "../../common/include/common/random.hpp"

int main() {
    common::testDriver();

    return 0;
}

/*

Sample Output

GameLib test driver run
Random test
New random number: 0.654865 <- these may change as they are random numbers
New random number (between 1, and 100): 81 
New random number (between 1, and 100): 48 
Weighted choice output: 0 
Weighted choice output: 0 
Random test success
Generation test
Number of quadrants with 1 klingon: 20% <- may change due to 1% statistical noise
Number of quadrants with 2 klingon: 5% <- may change due to 1% statistical noise
Number of quadrants with 3 klingon: 2% <- may change due to 1% statistical noise
Number of quadrants with bases: 0% <- may change due to 1% statistical noise
Time taken: 316 ms <- may change depending on system hardware
Generation test success
GameLib test driver run success


*/