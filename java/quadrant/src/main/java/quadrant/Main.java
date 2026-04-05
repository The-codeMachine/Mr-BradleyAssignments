package quadrant;

public class Main {
    public static void main(String args[]) {
        Quadrant q = new Quadrant(365);

        System.out.printf("klingons: %d", q.klingons());
        System.out.printf("bases: %d", q.bases());
        System.out.printf("stars: %d", q.stars());
    } 
}
