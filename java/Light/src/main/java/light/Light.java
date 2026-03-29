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
 *  [1 -> 100] The the last bit inside the byte represents whether 
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
 * Internal representation (1 bytes):
 * 
 * Layout:
 *  Bit 7: Power (1 = ON, 0 = OFF)
 *  Bit 0-6: brightness (range 1-100)
 * 
 * Example:
 *  10000001 -> ON, Brightness = 1
 *  11100100 -> ON, Brightness = 100
*/

package light;

public class Light {
    
    // Private members
    private byte luminosity; // uses only 1 byte
    
    // Constants
    private static final int POWER_MASK = 0b10000000;
    private static final int BRIGHTNESS_MASK = 0b01111111;
    private static final int MIN = 1, MAX = 100, ADJUSTMENT = 10;
    
    // Core API
    
    public Light() {
        setBrightness(50); // init brightness
        turnOn();
    }

    public void turnOn() {
        // forces bit 7 to 1, leaves other bits unchanged
        // example: 01100100 | 10000000 -> 11100100
        luminosity = (byte)(luminosity | POWER_MASK);
    }
    
    public void turnOff() {
        // forces bit 7 to 0, leaves other bits unchanged
        // example: 11100100 & 01111111 -> 01100100
        luminosity = (byte)(luminosity & ~POWER_MASK);
    }
    
    public boolean isOn() {
        // isolate the power bit (bit 7)
        // if result != 0 then the light is ON
        // example: 11100100 & 10000000 -> 10000000 (ON) 0
        return (luminosity & POWER_MASK) != 0;
    }
    
    public int getBrightness() {
        // extracts brightness (bits 0–6) by masking out the power bit (bit 7)
        return luminosity & BRIGHTNESS_MASK;
    }
    
    public void brighten() {
        adjustBrightness(ADJUSTMENT);
    }
    
    public void dim() {
        adjustBrightness(-ADJUSTMENT); 
    }
    
    // Outputs "Light is on: <false|true>, luminosity <value>"
    public String toString() {
        return String.format("Light is on: %b, luminosity %d%%", isOn(), getBrightness());
    }
    
    // Clamps a value to a certain range [min, max]
    static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        else if (value > max)
            return max;
        
        return value;
    }
    
    // sets the brightness of the luminosity value 
    private void setBrightness(int value) {
        value = clamp(value, MIN, MAX);
        byte bValue = (byte)(value); // value is clamped to [1, 100] which fits within bits 0-6
        
        // clears the power bits (0-6), perserves the power bit (7)
        luminosity = (byte)(luminosity & POWER_MASK); 
        
        luminosity = (byte)(luminosity | bValue); // sets the brightness bits (0-6) does not affect bit 7 since bValue < 128
    }
    
    /*
        Adjusts brightness by a signed amount (positive = brighten, negative = dim).

        - Only applies if the light is ON
        - Resulting brightness is clamped to [1, 100]
        - Internally updates only bits 0–6 (brightness)
    */
    private void adjustBrightness(int lumens) {
        // can only adjust the brightness if the light is ON
        if (!isOn())
            return;

        setBrightness(getBrightness() + lumens);
    }
}
