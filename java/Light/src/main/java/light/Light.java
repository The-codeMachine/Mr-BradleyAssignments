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
 *  of the power state. 
 *  
 *  Operations include:
 *  
 *      o turning on / off the light through direct methods (turnOn(), or turnOff()), or by switching its state (switchPower())
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

package light;

import static common.MathUtils.clamp;

public class Light {
    
    // Private members
    private byte luminosity; // uses only 1 byte
    
    // Constants
    private static final byte POWER_MASK = (byte)0b10000000;
    private static final byte BRIGHTNESS_MASK = (byte)0b01111111;

    private static final int MIN = 1; // only used for clamping so they can be int (since the clamp function takes ints)
    private static final int MAX = 100;
    private static final byte ADJUSTMENT = 10;
    private static final byte OFF = 0;
    private static final byte DEFAULT = 50;
    
    // Core API
    
    /**
     * Constructs a new Light
     * 
     * A newly created light starts with:
     * - ON
     * - Brightness = 50%
     * 
     */
    public Light() {
        turnOn();
        setBrightness(DEFAULT);
    }

    /**
     * Turns the light ON
     * 
     * Sets the power bit (bit 7) while preserving brightness
     */
    public void turnOn() {
        // Sets the power bit (7) to 1, leaving the rest unchanged
        // example: 01100100 | 10000000 -> 11100100
        luminosity = (byte)(luminosity | POWER_MASK);
    }
    
    /**
     * Turns the light OFF
     * 
     * Clears the power bit while preserving brightness
     */
    public void turnOff() {
        // Sets the power bit (7) to 0, leaving the other bits unchanged
        // example: 11100100 & 01111111 -> 01100100
        luminosity = (byte)(luminosity & ~POWER_MASK);
    }
    
    /**
     * Turns the light OFF if it is ON, 
     * or it turns the light ON if it is OFF
     */
    public void switchPower() {
        // for example: 01100100 ^ 10000000 -> 11100100
        // another example 11100100 ^ 10000000 -> 01100100
        luminosity ^= POWER_MASK;
    }

    /**
     * Returns whether the light is currently ON
     * 
     * @return true if the power bit is set
     */
    public boolean isOn() {
        // isolate the power bit (bit 7)
        // if result != 0 then the light is ON
        // example: 11100100 & 10000000 -> 10000000 (ON) 0
        return (luminosity & POWER_MASK) != OFF;
    }

    /**
     * Returns the current brightness of the light.
     *
     * Brightness is stored in bits 0–6 and ranges from 1–100.
     *
     * @return brightness percentage
     */
    public int getBrightness() {
        // extracts brightness (bits 0–6) by masking out the power bit (bit 7)
        return luminosity & BRIGHTNESS_MASK;
    }
    
    /**
     * Increases brightness by 10%.
     *
     * Brightness changes only occur if the light is ON.
     * The value is clamped to the valid range [1,100].
     */
    public void brighten() {
        setBrightness((byte)(getBrightness() + ADJUSTMENT));
    }
    
    /**
     * Decreases brightness by 10%.
     *
     * Brightness changes only occur if the light is ON.
     * The value is clamped to the valid range [1,100].
     */
    public void dim() {
        setBrightness((byte)(getBrightness() - ADJUSTMENT)); 
    }
    
    /**
     * @return {@code true} if {@link #isOn()} is true, with brightness {@link #getBrightness()}.
    */
    @Override
    public String toString() {
        return String.format("Light is on: %b, luminosity %d%%", isOn(), getBrightness());
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
    private void setBrightness(byte value) {
        // can only adjust the brightness if the light is ON
        if (!isOn())
            return;

        byte bValue = (byte)clamp((int)value, MIN, MAX); // value is clamped to [1, 100] which fits within bits 0-6
        
        // clears the brightness bits (0-6), perserves the power bit (7)
        luminosity = (byte)(luminosity & ~BRIGHTNESS_MASK); 
        
        luminosity = (byte)(luminosity | bValue); // sets the brightness bits (0-6) does not affect bit 7 since bValue < 128
    }

    public static void main(String[] args) {
        Light l = new Light();

        // test internal brightness manipulation
        l.setBrightness((byte)70);
        assert l.getBrightness() == 70 : "The light was not set to 70";

        // test clamping through setBrightness
        l.setBrightness((byte)120);
        assert l.getBrightness() == 100 : "The light's brightness was not clamped to 100";

        l.setBrightness((byte)-20);
        assert l.getBrightness() == 1 : "The light's brightness was not clamped to 1";

        System.out.println("White-box tests passed.");
    }

    /***  SAMPLE OUTPUT ****
    White-box tests passed.
    */  
}
