
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
 *  [-100..100]  0 is OFF, +/- 1 is MIN, +/- 100 is MAX Any values outside this range
 *  are invalid. 
 *  A luminosity value of < 0 indicates that the light is off, and that
 *  its previous 'on' value was the magnitude of its current value.
 *  For example: a light that is on at 45, is turned off; 
 *      then its luminosity value would now be -45.
 *  
 *  Operations include:
 *  
 *      o turning on / off the light
 *      o toggling the light between off/on
 *      o brighten / dim the light by 10%
 *      o query the light for its state - on | off, and luminosity
 */

public class Light
{
    /*** API - from design ***/
    public Light() { luminosity = -DEFAULT; }   // lights starts off at 50%
    
    public void turnOn()    { if( !isOn() ) toggle(); }
    public void turnOff()   { if(  isOn() ) toggle(); }
    
    public void brighten()  { adjustBrightness(+ADJUSTMENT); }
    public void dim()       { adjustBrightness(-ADJUSTMENT); }
    
    public boolean isOn()   { return luminosity > OFF; }
    public int luminosity() { return luminosity; }
    public void toggle()    { luminosity *= POWER_TOGGLE; }
    
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
        if( isOn() ) setBrightness( luminosity + lumens );;
        
                /*
                int value = luminosity + lumens;
                if      ( value < MIN ) luminosity = MIN;
                else if ( value > MAX ) luminosity = MAX;
                else                    luminosity = value;
                */
    }
    
    /**
     * constrains the value to be within [MIN..MAX]
     * NB: Does no checking about whether the light is on or off
     */
    private void setBrightness( int value ) {
        value = MathUtils.constrain(value, MIN, MAX);
        luminosity = value;
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
        MAX = 100,
        POWER_TOGGLE = -1;
        
}
