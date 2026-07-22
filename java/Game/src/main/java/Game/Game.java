package Game;

import common.GameLib;
import common.IO;
import common.GameLib.Location;

import java.util.ArrayList;

import QuadrantMap.QuadrantMap;
import enterprise.Enterprise;
import galaxy.Galaxy;

public class Game {

    public Game() {
        enterprise = new Enterprise(300.0, 1, 1, 1, 1, 3000, 10, false);
        galaxy = new Galaxy();
        map = new QuadrantMap[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                map[i][j] = new QuadrantMap(galaxy.getQuadrant(i, j));
            }
        }

        map[0][0].place(1, 1, QuadrantMap.ENTERPRISE);
    }

    /**
     * 
     * Gets the QuadrantMap at (x, y). This
     * method takes base-1 coordinates.
     * 
     * @param x
     * @param y
     * @return the QuadrantMap at (x, y)
     */
    public QuadrantMap at(int x, int y) {
        return map[GameLib.toBase0(x)][GameLib.toBase0(y)];
    }

    /**
     * 
     * Gets the QuadrantMap at (x, y). This
     * method takes base-0 location coordinates.
     * 
     * @param location
     * @return the QuadrantMap at (x, y)
     */
    public QuadrantMap at(Location location) {
        return map[location.quadrantX][location.quadrantY];
    }

    /**
     * 
     * Gets the curernt Enterprise. 
     * 
     * @return the enterprise of the game
     */
    public Enterprise getEnterprise() {
        return enterprise;
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
     * @param warpFactor
     * @param warpDirection
     * @return true if the move was successful
     */
    public boolean move(double warpFactor, double warpDirection) {
        ArrayList<Location> path = enterprise.calculatePath(warpFactor, warpDirection);

        if (path.isEmpty())
            return false;

        Location last = enterprise.getLocation();

        for (Location location : path) {
            IO.println(location.toString());

            if (!map[location.quadrantX][location.quadrantY]
                    .empty(GameLib.toBase1(location.sectorX), GameLib.toBase1(location.sectorY))) {
                    break;
            }

            last = location;
        }

        Location oldEnterpriseLocation = enterprise.getLocation();

        // clears old enterprise location
        at(oldEnterpriseLocation)
                .clearSector(GameLib.toBase1(oldEnterpriseLocation.sectorX), GameLib.toBase1(oldEnterpriseLocation.sectorY));

        // sets new enterprise location
        at(GameLib.toBase1(last.quadrantX), GameLib.toBase1(last.quadrantY))
                .place(GameLib.toBase1(last.sectorX), GameLib.toBase1(last.sectorY), QuadrantMap.ENTERPRISE);

        enterprise.move(last);

        return last == path.getLast();
    }

    private Enterprise enterprise;
    private Galaxy galaxy;

    private QuadrantMap[][] map;
}
