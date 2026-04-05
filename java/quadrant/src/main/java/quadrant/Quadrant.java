package quadrant;

public class Quadrant {
    
    public Quadrant(int initValue) {
        data = initValue;
    }

    public int klingons() {
        return data / 100;
    }

    public int bases() {
        return (data % 100) / 10;
    }

    public int stars() {
        return (data % 100) % 10;
    }

    private int data;
}
