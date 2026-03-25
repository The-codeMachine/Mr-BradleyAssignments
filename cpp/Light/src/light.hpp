/**
 * Concept / Use 
 * =============
 * A Light represents a the concept similar to a light in a house
 * with an on/off button and a brighten / dim switch or dial.
 * Pressing the on/off button turns the light on, after which
 * pressing the brighten / dim switches increase and decrease the 
 * luminosity of the light by 10%
 * 
 * Design
 * ======
 *  A light is encoded as an int value with a valid range between
 *  [1 -> 100] The the first bit inside the int represents whether 
 *  or not the light is on. Any values outside this range
 *  are invalid [1 -> 100].
 *  
 *  Operations include:
 *  
 *      o turning on / off the light
 *      o brighten / dim the light by 10% NB: luminosity changes can only occur if the light is ON
 *      o query the light for its state - on | off, and luminosity
 */
 
/**
 * A bit-packed light representation using a single byte (8 bits)
 * 
 * Layout:
 *  Bit 0: represents whether or not the light is on
 *  Bit 1-7: represents the brightness level
 * 
 * Notes:
 *  Brightness is clamped between values 1 -> 100, anything else is invalid
 *  All operations preserve bit integrity
 */

#pragma once
#include <string>
#include <stdint.h>
#include <iostream>

constexpr int OFF = 0;
constexpr int MIN = 1;
constexpr int MAX = 100;
constexpr int DIM = 50;
constexpr int ADJUSTMENT = 10;
constexpr uint8_t POWER_MASK = 0b0000001;

class Light {
public:
    Light();

    void turnOn();
    void turnOff();
    bool isOn() const;

    void dim();
    void brighten();

    int getBrightness() const;

    bool isDim() const;
    bool isBright() const;

    friend std::ostream& operator<<(std::ostream& os, Light& lt);

private:
    uint8_t luminosity;

private:
    void setBrightness(int value);
    // it was decided that the light could only be adjusted by 10% each step
    void adjustBrightness(int lumens);

    int clamp(int value);
};

// Uses a friend function to overload the insertion operator to write lt
std::ostream& operator<<(std::ostream& os, Light& lt);