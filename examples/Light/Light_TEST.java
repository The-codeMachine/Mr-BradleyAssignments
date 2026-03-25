
/**
 * Test the design and operation of the Light class here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Light_TEST
{
    
    public static void main( String[] args ) {
        System.out.println(" Testing the Light class...");
        
        Light l = new Light();
        
        System.out.println("\t A new instance of a light: " + l);
        
        l.brighten();
        System.out.println("\t light (brighten): " + l);
        
        l.dim();
        System.out.println("\t light (dim): " + l);
        
        l.turnOff();
        System.out.println("\t light (off): " + l);

        TEST_Ranges();
        
        System.out.println(" END Run ");
    }
    
    private static void TEST_Ranges() {
        Light l = new Light();
        System.out.println("\t A new instance of a light: " + l);
        
        l.turnOn();
        System.out.println("\t light (ON): " + l);
        
        for( int i = 0; i < 12; i ++ ) {
            l.brighten();
            System.out.println("\t (Brighten):  light: " + l);
        }
        
        for( int i = 0; i < 12; i ++ ) {
            l.dim();
            System.out.println("\t (Dim):       light: " + l);
        } 
        
        for( int i = 0; i < 12; i ++ ) {
            l.brighten();
            System.out.println("\t (Brighten):  light: " + l);
        }
        
        l.turnOff();
        System.out.println("\t light (OFF): " + l);
        
    }
}

/***  SAMPLE OUTPUT ****

 Testing the Light class...
	 A new instance of a light: Light is on: false. Luminosity: 0%.
	 light (brighten): Light is on: false. Luminosity: 0%.
	 light (dim): Light is on: false. Luminosity: 0%.
	 light (off): Light is on: false. Luminosity: 0%.
	 A new instance of a light: Light is on: false. Luminosity: 0%.
	 light (ON): Light is on: true. Luminosity: 50%.
	 (Brighten):  light: Light is on: true. Luminosity: 60%.
	 (Brighten):  light: Light is on: true. Luminosity: 70%.
	 (Brighten):  light: Light is on: true. Luminosity: 80%.
	 (Brighten):  light: Light is on: true. Luminosity: 90%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Dim):       light: Light is on: true. Luminosity: 90%.
	 (Dim):       light: Light is on: true. Luminosity: 80%.
	 (Dim):       light: Light is on: true. Luminosity: 70%.
	 (Dim):       light: Light is on: true. Luminosity: 60%.
	 (Dim):       light: Light is on: true. Luminosity: 50%.
	 (Dim):       light: Light is on: true. Luminosity: 40%.
	 (Dim):       light: Light is on: true. Luminosity: 30%.
	 (Dim):       light: Light is on: true. Luminosity: 20%.
	 (Dim):       light: Light is on: true. Luminosity: 10%.
	 (Dim):       light: Light is on: true. Luminosity: 1%.
	 (Dim):       light: Light is on: true. Luminosity: 1%.
	 (Dim):       light: Light is on: true. Luminosity: 1%.
	 (Brighten):  light: Light is on: true. Luminosity: 11%.
	 (Brighten):  light: Light is on: true. Luminosity: 21%.
	 (Brighten):  light: Light is on: true. Luminosity: 31%.
	 (Brighten):  light: Light is on: true. Luminosity: 41%.
	 (Brighten):  light: Light is on: true. Luminosity: 51%.
	 (Brighten):  light: Light is on: true. Luminosity: 61%.
	 (Brighten):  light: Light is on: true. Luminosity: 71%.
	 (Brighten):  light: Light is on: true. Luminosity: 81%.
	 (Brighten):  light: Light is on: true. Luminosity: 91%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 (Brighten):  light: Light is on: true. Luminosity: 100%.
	 light (OFF): Light is on: false. Luminosity: 0%.
 END Run 
 
 */
