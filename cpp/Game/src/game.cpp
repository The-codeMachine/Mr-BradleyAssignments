#include "Game.hpp"

#include <common/IO.hpp>

Game::Game() : enterprise(3000, 0, 10, false) {
    constructGame();
}

// Constructs a game
void Game::constructGame() {
    klingons.resize(8);
    for (int i = 0; i < 8; ++i) {
        klingons[i].resize(8);
        for (int j = 0; j < 8; ++j) {
            map[i][j] = QuadrantMap(galaxy.getQuadrant(i, j));

            /*
            int numKlingons = galaxy.getQuadrant(i, j).klingons();
            klingons[i][j].resize(numKlingons);

            for (int k = 0; k < numKlingons; ++k) {
                // IDK how to do this. Find where the klingon is somehow, idk
                // maybe loop through the quadrantmap to find it, or maybe
                // quadrant map will do it somehow 
                klingons[i][j][k] = Klingon(map[i][j]);
            }
            */
        }
    }

    common::Location enterpriseLocation = enterprise.getLocation();
    map[enterpriseLocation.quadrantX][enterpriseLocation.quadrantY]
        .place(common::toBase1(enterpriseLocation.sectorX), common::toBase1(enterpriseLocation.sectorY), QuadrantMap::ENTERPRISE);
}

// Checks whether or not the Enterprise can dock, if it can it returns true
bool Game::canDock() const noexcept {
    common::Location loc = enterprise.getLocation();

    // done like this for base0 to base1 conversion
    for (int i = common::toBase1(loc.sectorY - 1); i < common::toBase1(loc.sectorY + 2); ++i) {
        for (int j = common::toBase1(loc.sectorX - 1); j < common::toBase1(loc.sectorX + 2); ++j) {
            if (i < 1 || i > 8 || j < 1 || j > 8)
                continue;

            if (at(loc).at(j, i) == QuadrantMap::BASE) {
                return true;
            }
        }
    }

    return false;
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

// Gets the QuadrantMap at (x, y). This
// method takes base-1 coordinates.
const QuadrantMap& Game::at(int x, int y) const {
    return map[common::toBase0(x)][common::toBase0(y)];
}

// Gets the QuadrantMap at (x, y). This
// method takes base-0 location coordinates.
const QuadrantMap& Game::at(common::Location location) const {
    return map[location.quadrantX][location.quadrantY];
}

// Gets the current Enterprise. 
Enterprise Game::getEnterprise() const {
    return enterprise;
}

// Runs the game (takes input from the user,
// and runs those commands in game).
void Game::run() {
    while (handleCommand()) {
        
    }
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
    enterprise.updateDocked(false);
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

    enterprise.move(last, warpFactor);

    if (canDock())
        enterprise.updateDocked(true);

    return last == path.back();
}

// Handles a command from the user. Takes the input
// and handles the command (gets the correct data, 
// and calls the functions). Returns a boolean 
// representing whether the game should continue or
// not (true if the game should continue).
bool Game::handleCommand() {
    // continue if empty because the readCommand logs an error
    std::vector<std::string> command = common::IO::readCommand();
    if (command.empty())
        return true;

    const std::string& cmd = command[0];

    if (cmd == "NAV") {
        moveCommand(command);
    } else if (cmd == "SRS") {
        shortRangeCommand();
    } else if (cmd == "LRS") {
        longRangeCommand();
    } else if (cmd == "SHE") {
        shieldCommand(command);
    } else if (cmd == "DAM") {
        damageReportCommand();
    } else if (cmd == "XXX") {
        return false;
    }

    return !enterprise.isDestroyed();
}

// Handles the move command (gets the correct data,
// then calls the move function). 
void Game::moveCommand(const std::vector<std::string>& command) {
    if (command.size() < 3) {
        common::IO::warning("NAV usage <warp direction> <warp factor>");
        return;
    }

    try {
        double warpDirection = std::stod(command[1]);
        double warpFactor = std::stod(command[2]);

        move(warpFactor, warpDirection);
        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// The Enterprise does a short range scan.
void Game::shortRangeCommand() {
    common::Location enterpriseLocation = enterprise.getLocation();
    common::IO::println(map[enterpriseLocation.quadrantX][enterpriseLocation.quadrantY].toString());

    common::IO::println(enterprise.toString());
}

 
// Does a long range scan around the Enterprise. Returns
// the quadrant's KBS value around the Enterprise. 
void Game::longRangeCommand() {
    common::Location loc = enterprise.getLocation();

    int startY = std::max(0, loc.quadrantY - 1);
    int endY = std::min(7, loc.quadrantY + 1);

    int startX = std::max(0, loc.quadrantX - 1);
    int endX = std::min(7, loc.quadrantX + 1);

    for (int y = startY; y <= endY; ++y) {
        for (int x = startX; x <= endX; ++x) {
            common::IO::print(galaxy.getQuadrant(x, y).toString() + " ");
        }

        common::IO::println("");
    }
}

void Game::shieldCommand(const std::vector<std::string>& command) {
    try {
        double newShields = std::stod(command[1]);

        enterprise.adjustShields(newShields);
    } catch (const std::exception& e) {
        common::IO::warning("Invalid usage of SHE");
        common::IO::println("SHE usage: SHE <new shields>");
    }
}

// Prints a damage report based off the Enterprise's devices
void Game::damageReportCommand() {
    enterprise.damageReport();
}