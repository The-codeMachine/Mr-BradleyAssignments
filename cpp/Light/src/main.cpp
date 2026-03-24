#include "light.hpp"
#include <iostream>

int main() {
    std::cout << "Testing the Light class...\n";
    
    Light l;

    l.turnOn();
    
    std::cout << "\tA new instance of a light: " + l.toString() << "\n";
    
    l.brighten();
    std::cout << "\tlight (brighten): " + l.toString() << "\n";
    
    l.dim();
    std::cout << "\tlight (dim): " + l.toString() << "\n"; 
    
    l.turnOff();
    std::cout << "\tlight (off): " + l.toString() << "\n";

    l.brighten();
    std::cout << "\tlight: " + l.toString() << "\n";

    l.turnOn();

    for (int i = 0; i < 15; ++i)
        l.brighten();

    std::cout << "\tlight: " + l.toString() << "\n";

    for (int i = 0; i < 15; ++i)
        l.dim();

    std::cout << "\tlight: " + l.toString() << "\n";

    l.turnOff();
    
    std::cout << "END Run \n";

    return 0;
}

/* Sample Run
Testing the Light class...
        A new instance of a light: Light is on: 1, luminonsity 1
        light (brighten): Light is on: 1, luminonsity 11
        light (dim): Light is on: 1, luminonsity 1
        light (off): Light is on: 0, luminonsity 0
        light: Light is on: 0, luminonsity 0
        light: Light is on: 1, luminonsity 100
        light: Light is on: 1, luminonsity 1
END Run
*/