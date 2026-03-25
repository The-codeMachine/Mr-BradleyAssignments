#include "light.hpp"
#include <iostream>

Light::Light() {
    luminosity = 50;
}

/*
    The adjustBrightness function changes the luminosity variable by the amount given as an argument (lumens).
    This function also checks to ensure that the luminosity does not exceed its range (1-100), and ensures
    the light is on.
*/
void Light::adjustBrightness(int lumens) {
    if (!isOn())
        return;

    luminosity += lumens;

    if (luminosity < MIN) {
        luminosity = MIN;
    } else if (luminosity > MAX) {
        luminosity = MAX;
    }
}

void Light::turnOn() {
    luminosity = 50;
}

void Light::turnOff() {
    luminosity = OFF;
}

bool Light::isOn() const {
    return luminosity > OFF; 
}

void Light::dim() {
    adjustBrightness(-ADJUSTMENT);
}

void Light::brighten() {
    adjustBrightness(ADJUSTMENT);
}

int Light::getLuminosity() const {
    return luminosity;
}

bool Light::isDim() const {
    return luminosity <= DIM;
}

bool Light::isBright() const {
    return luminosity > DIM;
}

// Uses a friend function to overload the insertion operator to easily print Light (enables syntax std::cout << LightObject)
std::ostream& operator<<(std::ostream& os, Light& lt) {
    os << "List is on: " << lt.isOn() << ", liminosity " << lt.luminosity;
    return os;
}
