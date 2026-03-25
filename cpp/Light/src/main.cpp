#include "light.hpp"
#include <iostream>

int main() {
    std::cout << "Testing the Light class...\n";
    
    Light l;

    l.turnOn();
    
    std::cout << "\tA new instance of a light: " << l << "\n";
    
    l.brighten();
    std::cout << "\tlight (brighten): " << l << "\n";
    
    l.dim();
    std::cout << "\tlight (dim): " << l << "\n"; 
    
    l.turnOff();
    std::cout << "\tlight (off): " << l << "\n";

    l.brighten();
    std::cout << "\tlight: " << l << "\n";

    l.turnOn();

    for (int i = 0; i < 15; ++i)
        l.brighten();

    std::cout << "\tlight: " << l << "\n";

    for (int i = 0; i < 15; ++i)
        l.dim();

    std::cout << "\tlight: " << l << "\n";

    l.turnOff();
    
    std::cout << "END Run \n";

    // -rb- same use of Light<< overload
    std::cout << l;
    
    return 0;
}

/* Sample Run
Testing the Light class...
        A new instance of a light: Light is on: 1, luminonsity 50
        light (brighten): Light is on: 1, luminonsity 60
        light (dim): Light is on: 1, luminonsity 50
        light (off): Light is on: 0, luminonsity 0
        light: Light is on: 0, luminonsity 0
        light: Light is on: 1, luminonsity 100
        light: Light is on: 1, luminonsity 1
END Run
*/
