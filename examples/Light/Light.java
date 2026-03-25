
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
 *      o brighten / dim the light by 10%
 *      o query the light for its state - on | off, and luminosity
 */

public class Light
{
    /*** API - from design ***/
    public Light() { luminosity = OFF; }
    
    public void turnOn()    { luminosity = DEFAULT; }
    public void turnOff()   { luminosity = OFF; }
    
    public void brighten()  { adjustBrightness(+ADJUSTMENT); }
    public void dim()       { adjustBrightness(-ADJUSTMENT); }
    
    public boolean isOn()   { return luminosity > OFF; }
    public int luminosity() { return luminosity; }
    
    /*** Helper methods ***/   
    
    /**
     * this method implements all of the validation checks required
     * to ensure that a light value remains valid.
     * It calculates the adjusted value, then enforces the capacity /
     * boundary limits.
     * 
     * This method is a helper method (private) to locate all rules
     * in a single method - this makes maintenance and testing easier.
     * 
     * NOTE: that the check for isOn() is now also located in one spot
     */
    private void adjustBrightness( int lumens ) {
        if( !isOn() ) return;
        
        int value = luminosity + lumens;
        if      ( value < MIN ) luminosity = MIN;
        else if ( value > MAX ) luminosity = MAX;
        else                    luminosity = value;
        
    }
    
    public String toString() {
        return String.format("Light is on: %b. Luminosity: %d%%.", 
                                isOn(), luminosity );
    }
    
    /*** State - instance variables ***/
    private int luminosity;
    
    /*** Constants ***/
    private static final int
        DEFAULT     = 50,
        ADJUSTMENT  = 10,
        OFF = 0,
        MIN = 1,
        MAX = 100;
        
}
