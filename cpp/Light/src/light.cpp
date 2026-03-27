#include "light.hpp"
#include <iostream>

int Light::clamp(int value) {
    if (value > MAX) 
        return MAX;
    else if (value < MIN)
        return MIN;

    return value;
}

Light::Light() : luminosity(0) {
    setBrightness(50);
    turnOn();
}

void Light::turnOn() {
    // forces bit 0 to 1, leaves other bits unchanged
    // example:         01100100 | 00000001 -> 01100101
    luminosity = luminosity | POWER_MASK;
}

void Light::turnOff() {
    // forces bit 0 to 0, leaves other bits unchanged
    // example:         01100101 & 11111110 -> 01100100
    luminosity = luminosity & ~POWER_MASK;
}

bool Light::isOn() const {
    // AND isolated bit 0   
    // if result != 0 then bit is 1 (on)
    // example: 01100101 & 00000001 -> 00000001
    return (luminosity & POWER_MASK) != 0; 
}

void Light::dim() {
    adjustBrightness(-ADJUSTMENT);
}

void Light::brighten() {
    adjustBrightness(ADJUSTMENT);
}

int Light::getBrightness() const {
    // luminosity & 0xFF
    // promotes byte to int without sign extension (unsigned)
    
    // >> 1
    // shifts all bits right by 1
    // moves bits 1-7 into 0-6
    // example: 01100101 -> 00110010 (50)
    return luminosity & BRIGHTNESS_MASK;
}

bool Light::isDim() const {
    return getBrightness() <= DIM;
}

bool Light::isBright() const {
    return getBrightness() > DIM;
}

void Light::setBrightness(int value) {
    value = clamp(value);

      // clear brightness bits
    luminosity = luminosity & POWER_MASK; // keeps only bit 0
        
    // set new brightness
    luminosity = luminosity | value; // shifts bits and then inserts new bits (not affecting bit 0)
}

/*
    The adjustBrightness function changes the luminosity variable by the amount given as an argument (lumens).
    This function also checks to ensure that the luminosity does not exceed its range (1-100), and ensures
    the light is on.
*/
void Light::adjustBrightness(int lumens) {
    if (!isOn())
        return;

    setBrightness(getBrightness() + lumens);
}

// Uses a friend function to overload the insertion operator to easily print Light (enables syntax std::cout << LightObject)
std::ostream& operator<<(std::ostream& os, Light& lt) {
    os << "Light is on: " << lt.isOn() << ", luminosity " << lt.getBrightness();
    return os;
}
