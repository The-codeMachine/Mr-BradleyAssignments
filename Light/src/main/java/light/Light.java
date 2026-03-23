package light;

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
