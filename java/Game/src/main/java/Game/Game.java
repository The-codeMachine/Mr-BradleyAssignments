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
     * not complete the move. Might even kill the 
     * program. But I am not sure how to handle it yet. 
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

        QuadrantMap currQ = map[toBase0(oldGlobalLocation[X])][toBase0(oldGlobalLocation[Y])];

        // moves the Enterprise within a quadrant
        if (oldGlobalLocation == newGlobalLocation) {
            currQ.move(oldLocalLocation[X], oldLocalLocation[Y],
                    newLocalLocation[X], newLocalLocation[Y],
                    QuadrantMap.ENTERPRISE);

            // exit function, no more functionality is necessary
            return;
        }

        // moves the Enterprise through different quadrants
        QuadrantMap newQ = map[toBase0(newGlobalLocation[X])][toBase0(newGlobalLocation[Y])];

        currQ.clearSector(oldLocalLocation[X], oldLocalLocation[Y]);
        newQ.place(newLocalLocation[X], newLocalLocation[Y], QuadrantMap.ENTERPRISE);
    }

    /**
     * 
     * Converts the value from base-1 to base-0.
     * 
     * @param c
     * @return the value as base-0 from base-1
     */
    private static int toBase0(int c) {
        return c - 1;
    }

    /**
     * 
     * Converts a value from base-0 to base-1.
     * 
     * @param c
     * @return the value as base-1 from base-0
     */
    private static int toBase1(int c) {
        return c + 1;
    }

    private Enterprise enterprise;
    private Galaxy galaxy;

    private QuadrantMap[][] map;

    private static final int X = 0;
    private static final int Y = 1;
}
