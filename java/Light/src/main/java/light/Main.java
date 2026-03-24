/*
    I used Gradle, Groovy to build this again. 
    Technically not necessary (you could just put the two classes in the same file)
    but I did for organization's sake.

    I hope you find this file less bug prone.
    I believe this does everything it needs to, based off the design.

    Thank you again for all of your feedback,
    Rey
*/

package light;

public class Main {
    public static void main(String[] arg) {
        System.out.println("Testing the Light class...");
        
        Light l = new Light();
        
        System.out.println("\tA new instance of a light: " + l);
        
        l.turnOn();

        l.brighten();
        System.out.println("\tlight (brighten): " + l);
        
        l.dim();
        System.out.println("\tlight (dim): " + l);
        
        l.turnOff();
        System.out.println("\tlight (off): " + l);

        l.brighten();
        System.out.println("\tlight: " + l);

        l.turnOn();

        for (int i = 0; i < 15; ++i)
            l.brighten();

        System.out.println("\tlight: " + l);
    
        for (int i = 0; i < 15; ++i)
            l.dim();
    
        System.out.println("\tlight: " + l);

        l.turnOff();
        
        System.out.println("END Run ");
    }
}

/***  SAMPLE OUTPUT ****
Testing the Light class...
        A new instance of a light: Light is on: true, luminonsity 50%
        light (brighten): Light is on: true, luminonsity 60%
        light (dim): Light is on: true, luminonsity 50%
        light (off): Light is on: false, luminonsity 0%
        light: Light is on: false, luminonsity 0%
        light: Light is on: true, luminonsity 100%
        light: Light is on: true, luminonsity 1%
END Run
*/