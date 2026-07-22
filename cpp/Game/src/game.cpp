#include "Game.hpp"

#include <common/IO.hpp>

Game::Game() : enterprise(300.0, 1, 1, 1, 1, 3000, 10, false) {
    for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < 8; ++j) {
            map[i][j] = QuadrantMap(galaxy.getQuadrant(i, j));
        }
    }

    map[0][0].place(1, 1, QuadrantMap::ENTERPRISE);
}

// Gets the QuadrantMap at (x, y). This
// method takes base-1 coordinates.
QuadrantMap& Game::at(int x, int y) {
    return map[common::toBase0(x)][common::toBase0(y)];
}

// Gets the QuadrantMap at (x, y). This
// method takes base-0 location coordinates.
QuadrantMap& Game::at(common::Location location) {
    return map[location.quadrantX][location.quadrantY];
}

// Gets the current Enterprise. 
Enterprise Game::getEnterprise() const {
    return enterprise;
}

// Moves the Enterprise based off warpFactor
// and warpDirection. Moves the Enterprise
// withinQuadrants (moving it between or
// within a quadrant).
// 
// It does this by comparing the new and old
// coordinates. Based off this it retrieves
// the old and new QuadrantMap, adjusting values
// within there.
bool Game::move(double warpFactor, double warpDirection) {
    std::vector<common::Location> path = enterprise.calculatePath(warpFactor, warpDirection);

    if (path.empty())
        return false;

    common::Location last = enterprise.getLocation();
    
    for (common::Location location : path) {
        common::IO::println(location.toString());

        if (!map[location.quadrantX][location.quadrantY]
            .empty(common::toBase1(location.sectorX), common::toBase1(location.sectorY)))
            break;

        last = location;
    }

    common::Location oldEnterpriseLocation = enterprise.getLocation();

    // clears old enterprise location
    at(oldEnterpriseLocation)
            .clearSector(common::toBase1(oldEnterpriseLocation.sectorX), common::toBase1(oldEnterpriseLocation.sectorY));

    // sets new enterprise location
    at(common::toBase1(last.quadrantX), common::toBase1(last.quadrantY))
            .place(common::toBase1(last.sectorX), common::toBase1(last.sectorY), QuadrantMap::ENTERPRISE);

    enterprise.move(last);

    return last == path.back();
}