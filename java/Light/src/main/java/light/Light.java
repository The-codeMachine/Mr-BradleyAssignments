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
 *  [0..100]  0 - OFF, 1 - MIN, 100 - MAX Any values outside this range
 *  are invalid.
 *  
 *  Operations include:
 *  
 *      o turning on / off the light
 *      o brighten / dim the light by 10% NB: luminosity changes can only occur if the light is ON
 *      o query the light for its state - on | off, and luminosity
 */

public class Light {

    // Core API
    
    public Light() {}

    public void turnOn() {
        luminosity = MIN;
    }

    public void turnOff() {
        luminosity = OFF;
    }

    public void dim() {
        if (!isOn())
            return;

        luminosity -= DIM_AMOUNT;

        if (luminosity < MIN) {
            luminosity = MIN;
        }
    }

    public void brighten() {
        if (!isOn())
            return;

        luminosity += BRIGHTEN_AMOUNT;
 
        if (luminosity > MAX)
            luminosity = MAX;
    }

    public boolean isOn() {
        return luminosity > OFF;
    }

    public int luminosity() {
        return luminosity;
    }

    public boolean isDim() {
        return luminosity <= DIM;
    }

    public boolean isBright() {
        return luminosity > DIM;
    }

    // Helper conversion function
    public String toString() {
        return String.format("Light is on: %b, luminonsity %d%%", isOn(), luminosity);
    }

    // Private members
    private int luminosity;

    // Constants
    private static final int OFF = 0, MIN = 1, MAX = 100, DIM = 50, DIM_AMOUNT = 10, BRIGHTEN_AMOUNT = 10;
}
