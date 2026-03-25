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

public class Light {

    // Core API
    
    public Light() {
        adjustBrightness(50); // init brightness
        turnOn();
    }

    public void turnOn() {
        // forces bit 0 to 1, leaves other bits unchanged
        // example:         01100100 | 00000001 -> 01100101
        luminosity = (byte)(luminosity | POWER_MASK);
    }
    
    public void turnOff() {
        // forces bit 0 to 0, leaves other bits unchanged
        // example:         01100101 & 11111110 -> 01100100
        luminosity = (byte)(luminosity & ~POWER_MASK);
    }
    
    public boolean isOn() {
        // AND isolated bit 0
        // if result != 0 then bit is 1 (on)
        // example: 01100101 & 00000001 -> 00000001
        return (luminosity & POWER_MASK) != 0;
    }
    
    public boolean isDim() {
        return getBrightness() <= DIM;
    }
    
    public boolean isBright() {
        return getBrightness() > DIM;
    }
    
    public void dim() {
        adjustBrightness(-ADJUSTMENT); 
    }
    
    public void brighten() {
        adjustBrightness(ADJUSTMENT);
    }
    
    public int getBrightness() {
        // luminosity & 0xFF
        // promotes byte to int without sign extension (unsigned)
        
        // >> 1
        // shifts all bits right by 1
        // moves bits 1-7 into 0-6
        // example: 01100101 -> 00110010 (50)
        return (luminosity & 0xFF) >> 1;
    }
    
    // Helper conversion function
    public String toString() {
        return String.format("Light is on: %b, luminonsity %d%%", isOn(), getBrightness());
    }
    
    private void setBrightness(int value) {
        value = clamp(value);
        
        // clear brightness bits
        luminosity = (byte)(luminosity & POWER_MASK); // keeps only bit 0
        
        // set new brightness
        luminosity = (byte)(luminosity | (value << 1)); // shifts bits and then inserts new bits (not affecting bit 0)
    }
    
    // it has been decided that the light can only be adjusted by the set 10% each time (so it is private)
    /*
        The adjustBrightness function changes the luminosity variable by the amount given as an argument (lumens).
        This function also checks to ensure that the luminosity does not exceed its range (1-100), and ensures
        the light is on.
    */
    private void adjustBrightness(int lumens) {
        setBrightness(getBrightness() + lumens);
    }
    
    private int clamp(int value) {
        if (value < MIN)
            return MIN;
        else if (value > MAX)
            return MAX;
        
        return value;
    }

    // Private members
    private byte luminosity; // uses only 1 byte

    // Constants
    private static final int POWER_MASK = 0b00000001;
    private static final int MIN = 1, MAX = 100, DIM = 50, ADJUSTMENT = 10;
}
