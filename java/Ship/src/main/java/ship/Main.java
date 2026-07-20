package ship;

import common.IO;

public class Main {
    public static void main(String args[]) {
        IO.println("Ship test");

        Ship s = new Ship(200, 4, 4, 2, 2);

        // N
        s.move(2, 1);

        int[] localLocation = s.getLocalLocation();
        int[] globalLocation = s.getGlobalLocation();

        assert localLocation[0] == 4 && localLocation[1] == 4 &&
                globalLocation[0] == 2 && globalLocation[1] == 4
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)", localLocation[0], localLocation[1],
                globalLocation[0], globalLocation[1]);

        // NE
        s.move(3, 2);
        localLocation = s.getLocalLocation();
        globalLocation = s.getGlobalLocation();

        assert localLocation[0] == 4 && localLocation[1] == 4 &&
                globalLocation[0] == 4 && globalLocation[1] == 6
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)", localLocation[0], localLocation[1],
                globalLocation[0], globalLocation[1]);

        IO.println("Ship test success");
    }
}
