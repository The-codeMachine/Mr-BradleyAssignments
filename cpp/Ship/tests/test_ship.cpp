#include "../src/Ship.hpp"

#include <common/IO.hpp>
#include <iostream>
#include <cassert>

int main() {
    common::IO::println("Ship test");

    Ship s(200, 4, 4, 2, 2);

    // N
    s.move(2, 1);

    int sectX, sectY, quadX, quadY;
    s.getLocalLocation(sectX, sectY);
    s.getGlobalLocation(quadX, quadY);

    assert(sectX == 4 && sectY == 4 && quadX == 2 && quadY == 4);
    common::IO::printf("The ship's new position is: (%d, %d) in (%d, %d)\n", sectX, sectY, quadX, quadY);

    // NE
    s.move(3, 2);
    s.getLocalLocation(sectX, sectY);
    s.getGlobalLocation(quadX, quadY);

    assert(sectX == 4 && sectY == 4 && quadX == 4 && quadY == 6);
    common::IO::printf("The ship's new position is: (%d, %d) in (%d, %d)\n", sectX, sectY, quadX, quadY);

    s = Ship(200, 7, 7, 8, 8);

    // NE                
    s.move(8, 2);
    s.getLocalLocation(sectX, sectY);
    s.getGlobalLocation(quadX, quadY);

    assert(sectX == 8 && sectY == 8 && quadX == 8 && quadY == 8);
    common::IO::printf("The ship's new position is: (%d, %d) in (%d, %d)\n", sectX, sectY, quadX, quadY);

    common::IO::println("Ship test success");
}