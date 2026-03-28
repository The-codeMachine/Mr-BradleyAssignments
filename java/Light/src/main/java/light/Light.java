package light;

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
 *  A light is encoded as a byte value with a valid range between
 *  [1 -> 100] The the last bit inside the byte represents whether 
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
 *  Bit 7: represents whether or not the light is on
 *  Bit 0-6: represents the brightness level
 * 
 * Notes:
 *  Brightness is clamped between values 1 -> 100, anything else is invalid
 */

public class Light {

    // Private members
    private byte luminosity; // uses only 1 byte

    // Constants
    private static final int POWER_MASK = 0b10000000;
    private static final int BRIGHTNESS_MASK = 0b01111111;
    private static final int MIN = 1, MAX = 100, DIM = 50, ADJUSTMENT = 10;

    // Core API
    
    public Light() {
        setBrightness(50); // init brightness
        turnOn();
    }

    public void turnOn() {
        // forces bit 0 to 1, leaves other bits unchanged
        // example:         01100100 | 10000000 -> 11100100
        luminosity = (byte)(luminosity | POWER_MASK);
    }
    
    public void turnOff() {
        // forces bit 0 to 0, leaves other bits unchanged
        // example:         11100101 & 01111111 -> 01100101
        luminosity = (byte)(luminosity & ~POWER_MASK);
    }
    
    public boolean isOn() {
        // AND isolated bit 0
        // if result != 0 then bit is 1 (on)
        // example: 11100101 & 10000000 -> 10000000
        return (luminosity & POWER_MASK) != 0;
    }
    
    public int getBrightness() {
        // luminosity & 0xFF
        // promotes byte to int without sign extension (unsigned)
        return luminosity & BRIGHTNESS_MASK;
    }
    
    public boolean isDim() {
        return getBrightness() <= DIM;
    }
    
    public boolean isBright() {
        return getBrightness() > DIM;
    }
    
    public void brighten() {
        adjustBrightness(ADJUSTMENT);
    }
    
    public void dim() {
        adjustBrightness(-ADJUSTMENT); 
    }
    
    // Helper conversion function
    public String toString() {
        return String.format("Light is on: %b, luminonsity %d%%", isOn(), getBrightness());
    }
    
    // ensures the value is within the given range (does not exceed 100, or go below 1)
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
        
        // clear brightness bits
        luminosity = (byte)(luminosity & POWER_MASK); // keeps only bit 0
        
        // set new brightness
        luminosity = (byte)(luminosity | value); // shifts bits and then inserts new bits (not affecting bit 0)
    }
    
    /*
        it has been decided that the light can only be adjusted by the set 10% each time (so it is private)
        The adjustBrightness function changes the luminosity variable by the amount given as an argument (lumens).
        This function also checks to ensure that the luminosity does not exceed its range (1-100), and ensures
        the light is on.
    */
    private void adjustBrightness(int lumens) {
        if (!isOn())
            return;

        setBrightness(getBrightness() + lumens);
    }
}
