#include "../src/light.hpp"
#include <cassert>
#include <iostream>

#ifndef NDEBUG

// These functions consist of tests for all of the light class, as
// well as for a global clamp function

// tests that the constructor initialize the object properly
void testInitialState() {
    Light l;

    assert(l.getBrightness() == 50);
}

// tests that the brightness does not exceed its bounds
void testBrightnessBounds() {
    Light l;
    l.turnOn();

    for (size_t i = 0; i < 15; ++i) 
        l.brighten();

    assert(l.getBrightness() == 100);

    for (size_t i = 0; i < 15; ++i) 
        l.dim();

    assert(l.getBrightness() == 1);
}

// tests that the light's power behaviour is correct
void testPowerBehaviour() {
    Light l;

    assert(!l.isOn());

    l.turnOff();

    assert(!l.isOn());

    l.turnOn();

    assert(l.isOn());

    l.turnOn();

    assert(l.isOn());
}

// tests that brightness only changes when ON
void testAdjustWhileOff() {
    Light l;

    l.turnOff();

    int before = l.getBrightness();
    l.brighten();

    assert(l.getBrightness() == before);
}

// tests that the brightness is perserved even when the light is off
void testBrightnessPreservation() {
    Light l;
    l.turnOff();

    assert(l.getBrightness() == 50);
}

// tests that the switchPower function works
void testSwitchPower() {
    Light l;

    l.switchPower();

    assert(l.isOn());

    l.switchPower();

    assert(!l.isOn());
}

#endif

int main() {
    #ifndef NDEBUG

    std::cout << "Testing light class ... \n\n";

    // the tests now consist of asserts instead of logging 
    // to more easily tell if the tests failed

    testInitialState();
    testBrightnessBounds();
    testPowerBehaviour();
    testAdjustWhileOff();
    testBrightnessPreservation();
    testSwitchPower();

    Light l;
    l.testLightClass();

    std::cout << "END Run \n";
    
    #endif

    return 0;
}

/* Sample Run
Testing light class ... 

White-box tests passed.
END Run
*/
