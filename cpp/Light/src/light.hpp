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
 *  [1 -> 100] The highest (7) bit inside the byte represents whether 
 *  or not the light is on. Any values outside this range
 *  are invalid [1 -> 100]. Brightness is always stored regardless
 *  of the power state. However, it can only be changed if the light is ON
 *  
 *  Operations include:
 *  
 *      o turning on / off the light 
 *      o toggling the light on / off 
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
#include <ostream>
#include <cstdint>

class Light {
public:
    Light();
    Light(int initBrightness);

    void turnOn();
    void turnOff();
    void switchPower();
    bool isOn() const;

    void dim();
    void brighten();

    int getBrightness() const;

    #ifndef NDEBUG

    // tests the light class's private members
    void testLightClass();

    #endif

    friend std::ostream& operator<<(std::ostream& os, const Light& lt);

private:
    uint8_t luminosity;

    static inline constexpr int MIN = 1; // this is a ints because std::clamp requires ints
    static inline constexpr int MAX = 100; // this is a ints because std::clamp requires ints
    static inline constexpr uint8_t OFF = 0;
    static inline constexpr uint8_t DEFAULT = 50;
    static inline constexpr uint8_t ADJUSTMENT = 10;
    static inline constexpr uint8_t POWER_MASK = 0b10000000;
    static inline constexpr uint8_t BRIGHTNESS_MASK = 0b01111111;

private:
    void setBrightness(int value);

};

// Outputs "Light is on: <0|1>, luminosity <value>"
std::ostream& operator<<(std::ostream& os, const Light& lt);
