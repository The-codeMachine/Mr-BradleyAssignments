// I used Gradle, Groovy to build this.

package light;

import static common.MathUtils.clamp;

public class Main {
    // These functions consist of tests for all of the light class, as
    // well as for a global clamp function

    // tests that the constructor initialize the object properly
    private static void testInitialState() {
        Light l = new Light();

        assert l.getBrightness() == 50 : "Light is not 50 (when initialized)";
        assert l.isOn() : "Light is not ON (when initialized)";
    }

    // tests that the brightness does not exceed its bounds
    private static void testBrightnessBounds() {
        Light l = new Light();

        for (int i = 0; i < 15; ++i) 
            l.brighten();


        assert l.getBrightness() == 100 : "The light did not reach a maximum of 100";

        for (int i = 0; i < 15; ++i) 
            l.dim();

        assert l.getBrightness() == 1 : "The light did not reach a minimum of 1";
    }

    // tests that the light's power behaviour is correct
    private static void testPowerBehaviour() {
        Light l = new Light();

        assert l.isOn() : "The light does not initialize as ON";

        l.turnOff();

        assert !l.isOn() : "The light fails to turn OFF";

        l.turnOff();

        assert !l.isOn() : "The light still fails to turn OFF";

        l.turnOn();

        assert l.isOn() : "The light fails to turn ON";

        l.turnOn();

        assert l.isOn() : "The light still fails to turn ON";
    }

    // tests that the brightness only changes when ON
    private static void testAdjustWhileOff() {
        Light l = new Light();

        l.turnOff();

        int before = l.getBrightness();
        l.brighten();

        assert l.getBrightness() == before : "The light's brightness changed when it was OFF";
    }

    // tests that the brightness is perserved even when the light is off
    private static void testBrightnessPreservation() {
        Light l = new Light();

        l.turnOff();

        assert l.getBrightness() == 50 : "The light's brightness fails to stay when it is OFF";
    }

    // tests that the switchPower function works
    private static void testSwitchPower() {
        Light l = new Light();

        l.switchPower();

        assert !l.isOn() : "Light did not turn OFF";

        l.switchPower();

        assert l.isOn() : "Light did not turn ON";
    }

    // tests that the clamp function actually works
    private static void testClamp() {
        assert clamp(-10, 1, 100) == 1 : "The clamp function did not clamp the value to MIN";
        assert clamp(120, 1, 100) == 100 : "The clamp function did not return the MAX";
        assert clamp(50, 1, 100) == 50 : "The clamp function did not return the value when it was valid";
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
        testSwitchPower();
        testClamp();
        
        System.out.println("END Run ");
    }
}

/***  SAMPLE OUTPUT ****
Testing light class ... 


END Run
*/