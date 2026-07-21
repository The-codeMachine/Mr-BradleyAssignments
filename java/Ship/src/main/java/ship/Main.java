package ship;

import common.GameLib.Location;

import common.IO;

public class Main {
    public static void main(String args[]) {
        IO.println("Ship test");

        Ship s = new Ship(200, 4, 4, 2, 2);

        // N
        s.calculatePath(2, 1);

        Location location = s.getLocation();

        assert location.sectorX == 4 && location.sectorY == 4 &&
                location.quadrantX == 2 && location.quadrantY == 4
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)\n", location.sectorX, location.sectorY,
                location.quadrantX, location.quadrantY);

        // NE
        s.calculatePath(3, 2);
        location = s.getLocation();

        assert location.sectorX == 4 && location.sectorY == 4 &&
                location.quadrantX == 4 && location.quadrantY == 6
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)\n", location.sectorX, location.sectorY,
                location.quadrantX, location.quadrantY);

        s = new Ship(200, 7, 7, 8, 8);
        
        // NE                
        s.calculatePath(8, 2);
        location = s.getLocation();

        assert location.sectorX == 8 && location.sectorY == 8 &&
                location.quadrantX == 8 && location.quadrantY == 8
                : "Ship did not move to correct location";
        IO.printf("The ship's new position is: (%d, %d) in (%d, %d)\n", location.sectorX, location.sectorY,
                location.quadrantX, location.quadrantY);

        IO.println("Ship test success");
    }
}
