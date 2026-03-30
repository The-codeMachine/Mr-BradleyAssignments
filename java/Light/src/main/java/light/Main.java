// I used Gradle, Groovy to build this.

package light;

public class Main {
    // These functions consist of tests for all of the light class, as
    // well as for a global clamp function

    // tests that the constructor initialize the object properly
    private static void testInitialState() {
        Light l = new Light();

        assert(l.getBrightness() == 50);
        assert(l.isOn());
    }

    // tests that the brightness does not exceed its bounds
    private static void testBrightnessBounds() {
        Light l = new Light();

        for (int i = 0; i < 15; ++i) 
            l.brighten();

        assert(l.getBrightness() == 100);

        for (int i = 0; i < 15; ++i) 
            l.dim();

        assert(l.getBrightness() == 1);
    }

    // tests that the light's power behaviour is correct
    private static void testPowerBehaviour() {
        Light l = new Light();

        assert(l.isOn());

        l.turnOff();

        assert(!l.isOn());

        l.turnOff();

        assert(!l.isOn());

        l.turnOn();

        assert(l.isOn());

        l.turnOn();

        assert(l.isOn());
    }

    // tests that the brightness only changes when ON
    private static void testAdjustWhileOff() {
        Light l = new Light();

        l.turnOff();

        int before = l.getBrightness();
        l.brighten();

        assert(l.getBrightness() == before);
    }

    // tests that the brightness is perserved even when the light is off
    private static void testBrightnessPreservation() {
        Light l = new Light();

        l.turnOff();

        assert(l.getBrightness() == 50);
    }

    // tests that the clamp function works as expected
    private static void testClamp() {
        assert(Light.clamp(-12, 1, 100) == 1);

        assert(Light.clamp(1234, 1, 199) == 199);

        assert(Light.clamp(50, 1, 100) == 50);
    }

    public static void main(String[] arg) {
        System.out.println("Testing light class ... \n\n");

        // the tests now consist of asserts instead of logging 
        // to more easily tell if the tests failed

        testInitialState();
        testBrightnessBounds();
        testPowerBehaviour();
        testAdjustWhileOff();
        testBrightnessPreservation();
        testClamp();
        
        System.out.println("END Run ");
    }
}

/***  SAMPLE OUTPUT ****
Testing light class ... 


END Run
*/