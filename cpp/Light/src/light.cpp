#include "light.hpp"

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

Light::Light() : luminosity(50) {}

void Light::turnOn() {
    luminosity = MIN;
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