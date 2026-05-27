#include "Device.hpp"

#include <iostream>
#include <cassert>

void testConstructors()
{
    std::cout << "Constructor test\n";

    Device d(1, "cool_name");
    assert(!d.isBroken());
    std::cout << "Device is created with 0 damage when specified\n";

    Device dd(10, 1, "cooler_name");
    assert(dd.isBroken());
    std::cout << "Device started with the specified damage\n";

    std::cout << "Constructor test success\n";
}

void testRepairDamage()
{
    std::cout << "Repair/Damage test\n";

    Device d(10, 1, "cool_name");
    d.repair(10);
    assert(!d.isBroken());
    std::cout << "Device got repaired\n";

    d.takeDamage();
    assert(d.isBroken());
    std::cout << "Device got damaged\n";

    d.repair(50);
    assert(!d.isBroken());
    std::cout << "Device got repaired\n";

    std::cout << "Repair/Damage test success\n";
}

int main()
{

    std::cout << "Device Test\n";

    testConstructors();
    testRepairDamage();

    std::cout << "Device test success\n";

    return 0;
}

/*
Sample Output

Device Test
Constructor test
Device is created with 0 damage when specified
Device started with the specified damage
Constructor test success
Repair/Damage test
Device got repaired
Device got damaged
Device got repaired
Device test success

*/