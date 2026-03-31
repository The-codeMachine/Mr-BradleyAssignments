#include "light.hpp"
#include <iostream>

Light::Light() : luminosity(0) {
    turnOn();
    setBrightness(50);
}

void Light::turnOn() {
    // forces bit 7 to 1, leaves other bits unchanged
    // example: 01100100 | 10000000 -> 11100100
    luminosity |= POWER_MASK;
}

void Light::turnOff() {
    // forces bit 7 to 0, leaves other bits unchanged
    // example: 11100100 & 01111111 -> 01100100
    luminosity &= ~POWER_MASK;
}

bool Light::isOn() const {
    // isolate the power bit (bit 7)
    // if result != 0 then the light is ON
    // example: 11100100 & 10000000 -> 10000000 (ON)    
    return (luminosity & POWER_MASK) != 0; 
}

void Light::brighten() {
    setBrightness(getBrightness() + ADJUSTMENT);
}

void Light::dim() {
    setBrightness(getBrightness() - ADJUSTMENT);
}

int Light::getBrightness() const {
    // extracts brightness (bits 0–6) by masking out the power bit (bit 7)
    return luminosity & BRIGHTNESS_MASK;
}

/* sets the brightness of the luminosity value
- Only applies if the light is ON
- Resulting brightness is clamped to [1, 100]
- Internally updates only bits 0–6 (brightness)
*/
void Light::setBrightness(int value) {
    // can only adjust the brightness if the light is ON
    if (!isOn())
        return;

    value = clamp(value, MIN, MAX);
    uint8_t bValue = static_cast<uint8_t>(value); // value is clamped to [1, 100] which fits within bits 0-6
    
    // clears the brightness bits (0-6), preservess the power bit (clearing the brightness bits)
    luminosity &= POWER_MASK; 
    
    luminosity |= bValue; // sets the brightness bits (0-6) does not affect bit 7 since bValue < 128
}

#ifndef NDEBUG

#include <assert.h>

// tests the light class's private members
void Light::testLightClass() {
    turnOn();
    setBrightness(50);
    
    assert(getBrightness() == 50);
    
    // test internal brightness manipulation
    setBrightness(70);
    
    assert(getBrightness() == 70);
    
    // test clamping in setBrightness
    setBrightness(110);
    assert(getBrightness() == 100);
    
    setBrightness(-100);
    assert(getBrightness() == 1);

    // test clamp
    assert(clamp(-10, MIN, MAX) == MIN);
    assert(clamp(120, MIN, MAX) == MAX);
    assert(clamp(50, MIN, MAX) == 50);

    std::cout << "White-box tests passed.\n";
}

#endif

// Outputs "Light is on: <0|1>, luminosity <value>"
std::ostream& operator<<(std::ostream& os, const Light& lt) {
    os << "Light is on: " << lt.isOn() << ", luminosity " << lt.getBrightness();
    return os;
}

// Clamps the value to a specific range [min, max]
int Light::clamp(int value, int min, int max) {
    if (value > max) 
        return max;
    else if (value < min)
        return min;

    return value;
}
