#include "light.hpp"
#include <cassert>
#include <iostream>

// These functions consist of tests for all of the light class, as
// well as for a global clamp function

// tests that the constructor initialize the object properly
void testInitialState() {
    Light l;

    assert(l.getBrightness() == 50);
    assert(l.isOn());
}

// tests that the brightness does not exceed its bounds
void testBrightnessBounds() {
    Light l;

    for (size_t i = 0; i < 15; ++i) 
        l.brighten();

    assert(l.getBrightness() < 101);

    for (size_t i = 0; i < 15; ++i) 
        l.dim();

    assert(l.getBrightness() > 0);
}

// tests that the light's power behaviour is correct
void testPowerBehaviour() {
    Light l;

    assert(l.isOn());

    l.turnOff();

    assert(!l.isOn());

    l.turnOff();

    assert(!l.isOn());

    l.turnOn();

    assert(l.isOn());

    l.turnOn();

    assert(l.isOn());
}

// tests that the clamp function works as expected
void testClamp() {
    assert(clamp(-12, 1, 100) == 1);

    assert(clamp(1234, 1, 199) == 199);
}

int main() {
    std::cout << "Testing light class ... \n\n";

    // the tests now consist of asserts instead of logging 
    // to more easily tell if the tests failed

    testInitialState();
    testBrightnessBounds();
    testPowerBehaviour();
    testClamp();

    std::cout << "END Run \n";
    
    return 0;
}

/* Sample Run
Testing light class ...

END Run 
*/
