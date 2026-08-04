#include "Game.hpp"

#include <common/IO.hpp>

#include <algorithm>

Game::Game() : enterprise(3000, 0, 10, false) {
    constructGame();
}

// Constructs a game
void Game::constructGame() {
    initializeQuadrants();
    initializeKlingons();
    placeEnterprise();
}

// Initializes all Quadrants in the Game
void Game::initializeQuadrants() {
    for (int x = 0; x < MAP_SIZE; ++x) {
        for (int y = 0; y < MAP_SIZE; ++y) {
            map[x][y] = QuadrantMap(galaxy.getQuadrant(x, y));
        }
    }
}

// Initializes all klingons for the Game
void Game::initializeKlingons() {
    klingons.resize(MAP_SIZE);

    for (int x = 0; x < MAP_SIZE; ++x) {
        klingons[x].resize(MAP_SIZE);

        for (int y = 0; y < MAP_SIZE; ++y) {
            for (const auto& loc : map[x][y].klingons()) {
                klingons[x][y].emplace_back(common::Location(loc.sectorX, loc.sectorY, x, y));
            }
        }
    } 
}

// Places the Enterprise in its initial location
void Game::placeEnterprise() {
    const auto location = enterprise.getLocation();

    at(location).place(common::toBase1(location.sectorX), common::toBase1(location.sectorY), QuadrantMap::ENTERPRISE);
}

// Finds the movement destination based off the path
common::Location Game::findMovementDestination(const std::vector<common::Location>& path) {
    auto destination = enterprise.getLocation();

    for (const auto& location : path) {
        const auto& quadrant = at(location);

        if (!quadrant.empty(common::toBase1(location.sectorX), common::toBase1(location.sectorY)))
            break;

        destination = location;
    }

    return destination;
}

// Updates the Enterprise's map
void Game::updateEnterpriseMap(common::Location oldLocation, common::Location newLocation) {
    auto& oldQuadrant = at(oldLocation);
    auto& newQuadrant = at(newLocation);

    oldQuadrant.clearSector(common::toBase1(oldLocation.sectorX), common::toBase1(oldLocation.sectorY));
    newQuadrant.place(common::toBase1(newLocation.sectorX), common::toBase1(newLocation.sectorY), QuadrantMap::ENTERPRISE);
}


// Checks whether or not the Enterprise can dock, if it can it returns true
bool Game::canDock() const noexcept {
    const auto location = enterprise.getLocation();
    const auto& quadrant = at(location);

    const int centerX = common::toBase1(location.sectorX);
    const int centerY = common::toBase1(location.sectorY);
    
    for (int y = centerY - 1; y <= centerY + 1; ++y) {
        for (int x = centerX - 1; x <= centerX + 1; ++x) {
            if (x < MIN_SECTOR || x > MAX_SECTOR || y < MIN_SECTOR || y > MAX_SECTOR)
                continue;

            if (quadrant.at(x, y) == QuadrantMap::BASE) 
                return true;
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

// Gets the current Enterprise and returns a reference
Enterprise& Game::getEnterprise() {
    return enterprise;
}

// Gets the current Enterprise and returns a constant reference
const Enterprise& Game::getEnterprise() const {
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
    const auto path = enterprise.calculatePath(warpFactor, warpDirection);

    if (path.empty())
        return false;

    const auto oldLocation = enterprise.getLocation();
    const auto newLocation = findMovementDestination(path);

    updateEnterpriseMap(oldLocation, newLocation);
    
    enterprise.move(newLocation, warpFactor);
    enterprise.updateDocked(canDock());

    return newLocation == path.back();
}

// Handles firing the phasers at all klingons. If a klingon is
// destroyed, it will handle destroying the klingon as well
void Game::firePhasers(double phaserEnergy) {
    enterprise.adjustEnergy(-phaserEnergy);

    const auto location = enterprise.getLocation();
    auto& currentKlingons = klingons[location.quadrantX][location.quadrantY];

    for (auto it = currentKlingons.begin(); it != currentKlingons.end();) {
        auto& klingon = *it;

        klingon.adjustEnergy(-enterprise.firePhasers(phaserEnergy, klingon.getLocation().sectorX,
                            klingon.getLocation().sectorY, currentKlingons.size()));

        if (!klingon.isDestroyed()) {
            ++it;
            continue;
        }

        auto klingonLocation = it->getLocation();
        
        if (klingonLocation != klingonLocation) {
            ++it;
            continue;
        }
        
        galaxy.getQuadrant(klingonLocation.quadrantX, klingonLocation.quadrantY).reduceKlingons();
        at(klingonLocation).removeObject(common::toBase1(klingonLocation.sectorX), 
                            common::toBase1(klingonLocation.sectorY), QuadrantMap::KLINGON);

        it = currentKlingons.erase(it);
        common::IO::printf("Destroyed klingon at %s\n", klingonLocation.toString().c_str());
    }
}

// Fires a torpedo from the Enterprise based off a warpFactor and
// warpDircetion.
void Game::fireTorpedo(double warpDirection) {
    if (enterprise.getTorpedoes() <= 0)
        return;

    enterprise.reduceTorpedoes();
    // biggest warp factor possible so it won't stop
    const auto& path = enterprise.calculatePath(8.0, warpDirection);

    auto destination = findMovementDestination(path);
    auto currLocation = enterprise.getLocation();

    // checks that the torpedo is in the same quadrant
    if (destination.quadrantX != currLocation.quadrantX || destination.quadrantY != currLocation.quadrantY)
        return;

    auto it = std::find(path.begin(), path.end(), destination);
    if (it == path.end())
        return;

    // if the next object in the path is a klingon then destroy it
    ++it;
    if (it == path.end())
        return;

    if (at(*it).at(common::toBase1(it->sectorX), common::toBase1(it->sectorY)) == QuadrantMap::KLINGON)
        destroyKlingon(*it);
}

// Destroys the klingon at a position. Removes it from QuadrantMap,
// the klingons vector, and galaxy. 
void Game::destroyKlingon(common::Location position) {
    auto& currKlingons = klingons[position.quadrantX][position.quadrantY];

    for (auto it = currKlingons.begin(); it != currKlingons.end();) {
        auto klingonLocation = it->getLocation();
        
        if (klingonLocation != position) {
            ++it;
            continue;
        }
        
        galaxy.getQuadrant(position.quadrantX, position.quadrantY).reduceKlingons();
        at(position).removeObject(common::toBase1(klingonLocation.sectorX), 
                            common::toBase1(klingonLocation.sectorY), QuadrantMap::KLINGON);

        currKlingons.erase(it);
        common::IO::printf("Destroyed klingon at %s\n", klingonLocation.toString().c_str());

        return;
    }
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

    const auto& name = command.front();

    if (name == "NAV") {
        moveCommand(command);
    } else if (name == "SRS") {
        shortRangeCommand();
    } else if (name == "LRS") {
        longRangeCommand();
    } else if (name == "PHA") {
        phaserCommand(command);
    } else if (name == "TOR") {
        torpedoCommand(command);
    } else if (name == "SHE") {
        shieldCommand(command);
    } else if (name == "DAM") {
        damageReportCommand();
    } else if (name == "XXX") {
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
    const auto& location = enterprise.getLocation();
    
    common::IO::println(at(location).toString());
    common::IO::println(enterprise.toString());
}

 
// Does a long range scan around the Enterprise. Returns
// the quadrant's KBS value around the Enterprise. 
void Game::longRangeCommand() {
    const auto location = enterprise.getLocation();

    const int startY = std::max(0, location.quadrantY - 1);
    const int endY = std::min(7, location.quadrantY + 1);

    const int startX = std::max(0, location.quadrantX - 1);
    const int endX = std::min(7, location.quadrantX + 1);

    for (int y = startY; y <= endY; ++y) {
        for (int x = startX; x <= endX; ++x) {
            common::IO::print(galaxy.getQuadrant(x, y).toString() + " ");
        }

        common::IO::println("");
    }
}

// Fires the phasers based off the energy
void Game::phaserCommand(const std::vector<std::string>& command) {
    if (command.size() < 2) {
        common::IO::warning("PHA usage <phaser energy>");
        return;
    }
    
    try {
        double phaserEnergy = std::stod(command[1]);
        firePhasers(phaserEnergy);
        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// Fires a torpedo to a certain location based off the command
void Game::torpedoCommand(const std::vector<std::string>& command) {
    if (command.size() < 2) {
        common::IO::warning("TOR usage <warp direction>");
        return;
    }
    
    try {
        double warpDirection = std::stod(command[1]);
        fireTorpedo(warpDirection);
        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// Adjusts the shields based off the new shields
void Game::shieldCommand(const std::vector<std::string>& command) {
    if (command.size() < 2) {
        common::IO::warning("SHE usage <new shields>");
        return;
    }

    try {
        double newShields = std::stod(command[1]);
        enterprise.adjustShields(newShields);
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// Prints a damage report based off the Enterprise's devices
void Game::damageReportCommand() {
    enterprise.damageReport();
}