/**
 * Concept / Use 
 * =============
 * A Light represents a concept similar to a light in a house
 * with an on/off button and a brighten / dim switch or dial.
 * Pressing the on/off button turns the light on, after which
 * pressing the brighten / dim switches increase and decrease the 
 * luminosity of the light by 10%
 * 
 * Design
 * ======
 *  A light is encoded as a byte value with a valid range between
 *  [1 -> 100] The last bit inside the byte represents whether 
 *  or not the light is on. Any values outside this range
 *  are invalid [1 -> 100]. Brightness is always stored regardless
 *  of the power state. 
 *  
 *  Operations include:
 *  
 *      o turning on / off the light
 *      o brighten / dim the light by 10% NB: luminosity changes can only occur if the light is ON
 *      o query the light for its state - on | off, and luminosity
 */


/**
 * Internal representation (1 byte):
 * 
 * Layout:
 *  Bit 7: Power (1 = ON, 0 = OFF)
 *  Bit 0-6: brightness (range 1-100)
 * 
 * Example:
 *  10000001 -> ON, Brightness = 1
 *  11100100 -> ON, Brightness = 100
*/

#pragma once
#include <string>
#include <stdint.h>
#include <iostream>

constexpr int OFF = 0;
constexpr int MIN = 1;
constexpr int MAX = 100;
constexpr int ADJUSTMENT = 10;
constexpr uint8_t POWER_MASK = 0b10000000;
constexpr uint8_t BRIGHTNESS_MASK = 0b01111111;

class Light {
public:
    Light();

    void turnOn();
    void turnOff();
    bool isOn() const;

    void dim();
    void brighten();

    int getBrightness() const;

    friend std::ostream& operator<<(std::ostream& os, const Light& lt);

private:
    uint8_t luminosity;

private:
    void setBrightness(int value);

    // it was decided that the light could only be adjusted by 10% each step
    void adjustBrightness(int delta);
};

// Outputs "Light is on: <0|1>, luminosity <value>"
std::ostream& operator<<(std::ostream& os, const Light& lt);

// Clamps the value to a specific range [min, max]
int clamp(int value, int min, int max);