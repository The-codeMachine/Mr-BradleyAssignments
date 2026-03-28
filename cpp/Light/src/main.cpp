#include "light.hpp"
#include <cassert>
#include <iostream>

int main() {
    std::cout << "Testing the Light class...\n";
    
    Light l;

    l.turnOn();
    
    std::cout << "\tA new instance of a light: " << l << "\n";
    
    l.brighten();
    std::cout << "\tlight (brighten): " << l << "\n";

    assert(l.getBrightness() == 60);
    
    l.dim();
    std::cout << "\tlight (dim): " << l << "\n"; 
    
    assert(l.getBrightness() == 50);

    l.turnOff();
    std::cout << "\tlight (off): " << l << "\n";

    l.brighten();
    std::cout << "\tlight: " << l << "\n";

    l.turnOn();

    for (int i = 0; i < 15; ++i)
        l.brighten();

    assert(l.getBrightness() == 100);
    
    std::cout << "\tlight: " << l << "\n";
    
    for (int i = 0; i < 15; ++i)
        l.dim();
    
    assert(l.getBrightness() == 1);
    
    std::cout << "\tlight: " << l << "\n";

    l.turnOff();

    int returnValue = clamp(-12, 1, 100);

    std::cout << "\tTesting clamp (-12, 1, 100): " << returnValue << "\n";

    returnValue = clamp(1010, 1, 100);

    std::cout << "\tTesting clamp (1010, 1, 100): " << returnValue << "\n";

    std::cout << "END Run \n";
    
    return 0;
}

/* Sample Run
Testing the Light class...
        A new instance of a light: Light is on: 1, luminosity 50
        light (brighten): Light is on: 1, luminosity 60
        light (dim): Light is on: 1, luminosity 50
        light (off): Light is on: 0, luminosity 50
        light: Light is on: 0, luminosity 50
        light: Light is on: 1, luminosity 100
        light: Light is on: 1, luminosity 1
        Testing clamp (-12, 1, 100): 1
        Testing clamp (1010, 1, 100): 100
END Run
*/
