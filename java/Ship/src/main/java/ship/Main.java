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
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)\n", localLocation[0], localLocation[1],
                globalLocation[0], globalLocation[1]);

        // NE
        s.move(3, 2);
        localLocation = s.getLocalLocation();
        globalLocation = s.getGlobalLocation();

        assert localLocation[0] == 4 && localLocation[1] == 4 &&
                globalLocation[0] == 4 && globalLocation[1] == 6
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)\n", localLocation[0], localLocation[1],
                globalLocation[0], globalLocation[1]);

        s = new Ship(200, 7, 7, 8, 8);
        
        // NE                
        s.move(8, 2);
        localLocation = s.getLocalLocation();
        globalLocation = s.getGlobalLocation();

        assert localLocation[0] == 8 && localLocation[1] == 8 &&
                globalLocation[0] == 8 && globalLocation[1] == 8
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)\n", localLocation[0], localLocation[1],
                globalLocation[0], globalLocation[1]);

        IO.println("Ship test success");
    }
}
