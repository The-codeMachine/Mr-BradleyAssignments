package Game;

import QuadrantMap.QuadrantMap;
import enterprise.Enterprise;
import galaxy.Galaxy;

public class Game {

    public Game() {
        enterprise = new Enterprise(300.0, 1, 1, 1, 1, 3000, 10, false);
        galaxy = new Galaxy();
        map = new QuadrantMap[8][8];
    }

    public void run() {

    }

    /**
     * 
     * Moves the Enterprise based off warpFactor
     * and warpDirection. Moves the Enterprise
     * withinQuadrants (moving it between or
     * within a quadrant).
     * 
     * It does this by comparing the new and old
     * coordinates. Based off this it retrieves
     * the old and new QuadrantMap, adjusting values
     * within there.
     * 
     * TODO:
     * Make it handle if there is already an object
     * there more gracefully. Currently it will just
     * not complete the move. Maybe make .move function
     * inside the Enterprise a boolean returning whether
     * it was a success or not. 
     * 
     * @param warpFactor
     * @param warpDirection
     */
    public void move(double warpFactor, double warpDirection) {
        int[] oldGlobalLocation = enterprise.getGlobalLocation();
        int[] oldLocalLocation = enterprise.getLocalLocation();

        enterprise.move(warpFactor, warpDirection);

        int[] newGlobalLocation = enterprise.getGlobalLocation();
        int[] newLocalLocation = enterprise.getLocalLocation();

        // clears old location
        map[oldGlobalLocation[X]][oldGlobalLocation[Y]]
                .clearSector(oldLocalLocation[X], oldLocalLocation[Y]);

        // puts new location
        map[newGlobalLocation[X]][newGlobalLocation[Y]]
                .place(newLocalLocation[X], newLocalLocation[Y], QuadrantMap.ENTERPRISE);
    }
    
    private Enterprise enterprise;
    private Galaxy galaxy;

    private QuadrantMap[][] map;

    private static final int X = 0;
    private static final int Y = 1;
}
