#include "light.hpp"

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

bool Light::isOn() {
    return luminosity > OFF; 
}

void Light::dim() {
    adjustBrightness(-ADJUSTMENT);
}

void Light::brighten() {
    adjustBrightness(ADJUSTMENT);
}

int Light::getLuminosity() {
    return luminosity;
}

bool Light::isDim() {
    return luminosity <= DIM;
}

bool Light::isBright() {
    return luminosity > DIM;
}

std::string Light::toString() {
    return "Light is on: " + std::to_string(isOn()) + ", luminonsity " + std::to_string(luminosity);
}