#include "light.hpp"
#include <iostream>
#include <algorithm>

/**
 * Constructs a new Light
 * 
 * A newly created light starts with:
 * - OFF
 * - Brightness = 50%
 * 
 */
Light::Light() : luminosity(0) {
    // sets the brightness to be 50, and the light to be OFF
    luminosity |= (static_cast<uint8_t>(DEFAULT) & BRIGHTNESS_MASK);
}

/**
 * Constructs a new Light
 * 
 * A newly created light starts with:
 * - OFF
 * - Brightness = initBrightness
 * 
 */
Light::Light(int initBrightness) : luminosity(0) {
    // sets the brightness to be the init brightness
    luminosity |= (static_cast<uint8_t>(initBrightness) & BRIGHTNESS_MASK);
}

/**
 * Turns the light ON
 * 
 * Sets the power bit (bit 7) while preserving brightness
 */
void Light::turnOn() {
    // forces bit 7 to 1, leaves other bits unchanged
    // example: 01100100 | 10000000 -> 11100100
    luminosity |= POWER_MASK;
}

/**
 * Turns the light OFF
 * 
 * Clears the power bit while preserving brightness
 */
void Light::turnOff() {
    // forces bit 7 to 0, leaves other bits unchanged
    // example: 11100100 & 01111111 -> 01100100
    luminosity &= ~POWER_MASK;
}

/**
 * Turns the light OFF if it is ON, 
 * or it turns the light ON if it is OFF
 */
void Light::switchPower() {
    // for example: 01100100 ^ 10000000 -> 11100100
    // another example 11100100 ^ 10000000 -> 01100100
    luminosity ^= POWER_MASK;
}

/**
 * Returns whether the light is currently ON
 * 
 * @return true if the power bit is set
 */
bool Light::isOn() const {
    // isolate the power bit (bit 7)
    // if result != 0 then the light is ON
    // example: 11100100 & 10000000 -> 10000000 (ON)    
    return (luminosity & POWER_MASK) != OFF; 
}

/**
 * Increases brightness by 10%.
 *
 * Brightness changes only occur if the light is ON.
 * The value is clamped to the valid range [1,100].
 */
void Light::brighten() {
    setBrightness((luminosity & BRIGHTNESS_MASK) + ADJUSTMENT);
}

/**
 * Decreases brightness by 10%.
 *
 * Brightness changes only occur if the light is ON.
 * The value is clamped to the valid range [1,100].
*/
void Light::dim() {
    setBrightness((int)(luminosity & BRIGHTNESS_MASK) - ADJUSTMENT);
}

/**
 * Returns the current brightness of the light.
 *
 * Brightness is stored in bits 0–6 and ranges from 1–100.
 *
 * @return brightness percentage
 */
int Light::getBrightness() const {
    // extracts brightness (bits 0–6) by masking out the power bit (bit 7)
    return luminosity & BRIGHTNESS_MASK;
}

/**
 * Sets the brightness of the luminosity value
 * 
 * @apiNote Only applies if the light is ON
 * @apiNote Resulting brightness is clamped to [1, 100]
 * @apiNote Internally updates only bits 0-6
 * 
 * @param value
 */
void Light::setBrightness(int value) {
    // can only adjust the brightness if the light is ON
    if (!isOn())
        return;

    value = std::clamp(value, MIN, MAX); // changed this to the standard clamp function 

    // clears the brightness bits (0-6), preserves the power bit (clearing the brightness bits)
    luminosity &= ~BRIGHTNESS_MASK; 

    uint8_t bValue = static_cast<uint8_t>(value);
    
    luminosity |= (bValue & BRIGHTNESS_MASK); // sets the brightness bits (0-6) does not affect bit 7 since bValue < 128
}

#ifndef NDEBUG

#include <cassert>

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

    std::cout << "White-box tests passed.\n";
}

#endif

/**
 * @return true if {@link #Light::isOn()} is true, with brightness {@link #Light::getBrightness()}.
*/
std::ostream& operator<<(std::ostream& os, const Light& lt) {
    std::string status = lt.isOn() ? "ON" : "OFF";
    os << "Light is on: " << status << ", luminosity " << lt.getBrightness();
    return os;
}