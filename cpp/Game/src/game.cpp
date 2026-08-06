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
    initializeTime();
    placeEnterprise();
}

// Initializes all Quadrants in the Game
void Game::initializeQuadrants() {
    for (int x = 0; x < MAP_SIZE; ++x) {
        for (int y = 0; y < MAP_SIZE; ++y) {
            map[x][y] = QuadrantMap(galaxy.getQuadrant(common::toBase1(x), common::toBase1(y)));
        }
    }
}

// Initializes all klingons for the Game
void Game::initializeKlingons() {
    klingons.resize(MAP_SIZE);
    int numKlingons = 0;

    for (int x = 0; x < MAP_SIZE; ++x) {
        klingons[x].resize(MAP_SIZE);

        for (int y = 0; y < MAP_SIZE; ++y) {
            for (const auto& loc : map[x][y].klingons()) {
                numKlingons++;
                klingons[x][y].emplace_back(common::Location(loc.sectorX, loc.sectorY, x, y));
            }
        }
    } 

    common::IO::printf("Your orders are as follows: \n");
    common::IO::printf("destroy the %d Klingon warships which have invaded\n", numKlingons);
    common::IO::printf("the galaxy before they can attack Federation headquarters\n");
}

// Initializes the current time
void Game::initializeTime() {
    currentStardate = (int)(common::random() * 20 + 20) * 100;
    startingStardate = currentStardate;
    missionDuration = 25 + (int)(common::random() * 10);

    common::IO::printf("on stardate: %d, this gives you %d days\n", startingStardate + missionDuration, missionDuration);
}

// Places the Enterprise in its initial location
void Game::placeEnterprise() {
    const auto location = enterprise.getLocation();

    at(location).place(location, QuadrantMap::ENTERPRISE);
}

// Finds the movement destination based off the path
// Calculates the movement cost in time as well
common::Location Game::findMovementDestination(const std::vector<common::Location>& path, double& starDateChange) {
    const auto startingLocation = enterprise.getLocation();
    auto destination = startingLocation;

    starDateChange = 0.0;

    auto previousLocation = startingLocation;

    for (const auto& location : path) {
        const auto& quadrant = at(location);

        if (!quadrant.empty(location)) {
            break;
        }

        if (!location.sameQuadrant(previousLocation)) {
            starDateChange += 1.0;
        }

        destination = location;
        previousLocation = location;
    }

    return destination;
}

// Updates the Enterprise's map
void Game::updateEnterpriseMap(common::Location oldLocation, common::Location newLocation) {
    auto& oldQuadrant = at(oldLocation);
    auto& newQuadrant = at(newLocation);

    oldQuadrant.clearSector(oldLocation);
    newQuadrant.place(newLocation, QuadrantMap::ENTERPRISE);
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

    // currently we only have a lose condition, once we
    // have a win condition too we will adjust this
    common::IO::println("YOU LOST");
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
    double starDateChange = 0;
    const auto newLocation = findMovementDestination(path, starDateChange);

    // updates time
    
    // If no quadrant boundary was crossed, normal movement time applies.
    if (starDateChange == 0.0 && oldLocation != newLocation) {
        starDateChange = warpFactor < 1.0
            ? 0.1 * std::floor(10.0 * warpFactor)
            : 1.0;
    }
    
    currentStardate += starDateChange;

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
    auto& currentKlingons = getKlingons(location);

    for (auto it = currentKlingons.begin(); it != currentKlingons.end();) {
        auto& klingon = *it;

        klingon.adjustEnergy(-enterprise.firePhasers(phaserEnergy, klingon.getLocation().sectorX,
                            klingon.getLocation().sectorY, currentKlingons.size()));

        if (!klingon.isDestroyed()) {
            ++it;
            continue;
        }

        auto klingonLocation = it->getLocation();
        
        galaxy.getQuadrant(klingonLocation).reduceKlingons();
        at(klingonLocation).removeObject(klingonLocation, QuadrantMap::KLINGON);

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

    double nullValue = 0;
    auto destination = findMovementDestination(path, nullValue);
    auto currLocation = enterprise.getLocation();

    // checks that the torpedo is in the same quadrant
    if (!destination.sameQuadrant(currLocation))
        return;

    // if destination is not in path it is equal to the enterprise meaning the first one in the path
    auto it = std::find(path.begin(), path.end(), destination);
    if (it == path.end()) {
        common::Location loc = *path.begin();
        if (at(loc).at(loc) == QuadrantMap::KLINGON) {
            destroyKlingon(loc);
            return;
        }
    }

    // if the next object in the path is a klingon then destroy it
    ++it;
    if (it == path.end())
        return;

    if (at(*it).at(*it) == QuadrantMap::KLINGON)
        destroyKlingon(*it);
}

// Makes all the klingons within the Enterprise's current Quadrant fire at it
void Game::klingonsFire() {
    if (enterprise.getDocked()) {
        common::IO::println("Starbase shields protect you from incoming klingon attacks");
        return;
    }
    
    auto position = enterprise.getLocation();
    auto& currKlingons = getKlingons(position);

    for (auto& k : currKlingons) {
        int damage = k.firePhasers(position.sectorX, position.sectorY);

        common::IO::printf("Klingon (%d, %d) has fired their phasers dealing: %d damage\n",
                            common::toBase1(k.getLocation().sectorY), 
                            common::toBase1(k.getLocation().sectorX), damage
                        );

        enterprise.takeDamage(damage);
    }
}

// Moves all the Klingons within a Quadrant to a random unoccupied location 
// in its current Quadrant. Essentially teleports the Klingon (does not check
// the paths towards the destination), similar to the original.
void Game::klingonsMove() {
    auto position = enterprise.getLocation();
    auto& currKlingons = getKlingons(position);

    for (Klingon& k : currKlingons) {
        auto location = k.calculateDestination();
        while (!at(location).empty(location))
        {
            location = k.calculateDestination();
        }

        auto& quadrant = at(location);
        quadrant.move(k.getLocation(), location, QuadrantMap::KLINGON);
        k.move(location);
    }
}

// Destroys the klingon at a position. Removes it from QuadrantMap,
// the klingons vector, and galaxy. 
void Game::destroyKlingon(common::Location position) {
    auto& currKlingons = getKlingons(position);

    for (auto it = currKlingons.begin(); it != currKlingons.end();) {
        auto klingonLocation = it->getLocation();
        
        if (klingonLocation != position) {
            ++it;
            continue;
        }
        
        galaxy.getQuadrant(position).reduceKlingons();
        at(position).removeObject(klingonLocation, QuadrantMap::KLINGON);

        currKlingons.erase(it);
        common::IO::printf("Destroyed klingon at %s\n", klingonLocation.toString().c_str());

        return;
    }
}

// Gets the Klingons at (x, y). Returns a reference to that klingon
// vector. Takes base-1 coordinates
std::vector<Klingon>& Game::getKlingons(int x, int y) {
    return klingons[common::toBase0(x)][common::toBase0(y)];
}

// Gets the Klingons at (x, y). Returns a reference to that klingon
// vector. Takes base-0 coordinates through location
std::vector<Klingon>& Game::getKlingons(common::Location loc) {
    return klingons[loc.quadrantX][loc.quadrantY];
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
// then calls the move function). Calls klingons to fire and 
// move after. 
void Game::moveCommand(const std::vector<std::string>& command) {
    if (command.size() < 3) {
        common::IO::warning("NAV usage <warp direction> <warp factor>");
        return;
    }

    try {
        double warpDirection = std::stod(command[1]);
        double warpFactor = std::stod(command[2]);

        auto oldPos = enterprise.getLocation();
        move(warpFactor, warpDirection);

        if (oldPos != enterprise.getLocation()) {
            klingonsMove();
            klingonsFire();
        }

        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// The Enterprise does a short range scan.
void Game::shortRangeCommand() {
    if (enterprise.isDeviceBroken(std::string(Devices::SHORT_RANGE_SENSORS))) {
        common::IO::println("Short Range Sensors need repair, cannot do scan");
        return;
    }

    const auto& location = enterprise.getLocation();
    
    common::IO::println(at(location).toString());
    common::IO::println(enterprise.toString());
    common::IO::printf("Star date: %.6f\n", currentStardate);
}

 
// Does a long range scan around the Enterprise. Returns
// the quadrant's KBS value around the Enterprise. 
void Game::longRangeCommand() {
    if (enterprise.isDeviceBroken(std::string(Devices::LONG_RANGE_SENSORS))) {
        common::IO::println("Long Range Sensors need repair, cannot do scan");
        return;
    }

    const auto location = enterprise.getLocation();

    const int startY = std::max(0, location.quadrantY - 1);
    const int endY = std::min(7, location.quadrantY + 1);

    const int startX = std::max(0, location.quadrantX - 1);
    const int endX = std::min(7, location.quadrantX + 1);

    for (int y = startY; y <= endY; ++y) {
        for (int x = startX; x <= endX; ++x) {
            common::IO::print(galaxy.getQuadrant(common::toBase1(x), common::toBase1(y)).toString() + " ");
        }

        common::IO::println("");
    }
}

// Fires the phasers based off the energy. Makes the klingons fire after
// and then does a short range scan
void Game::phaserCommand(const std::vector<std::string>& command) {
    if (enterprise.isDeviceBroken(std::string(Devices::PHASER_CONTROL))) {
        common::IO::println("Phaser control needs repair, cannot fire phasers");
        return;
    }

    if (command.size() < 2) {
        common::IO::warning("PHA usage <phaser energy>");
        return;
    }
    
    try {
        double phaserEnergy = std::stod(command[1]);
        firePhasers(phaserEnergy);
        klingonsFire();
        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// Fires a torpedo to a certain location based off the command. Makes the klingons fire after
// and then does a short range scan
void Game::torpedoCommand(const std::vector<std::string>& command) {
    if (enterprise.isDeviceBroken(std::string(Devices::TORPEDO_CONTROL))) {
        common::IO::println("Torpedo control needs repair. Cannot fire a torpedo");
        return;
    }

    if (command.size() < 2) {
        common::IO::warning("TOR usage <warp direction>");
        return;
    }
    
    try {
        double warpDirection = std::stod(command[1]);
        fireTorpedo(warpDirection);
        klingonsFire();
        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// Adjusts the shields based off the new shields
void Game::shieldCommand(const std::vector<std::string>& command) {
    if (enterprise.isDeviceBroken(std::string(Devices::SHIELD_CONTROL))) {
        common::IO::println("Shield control needs repair. Cannot raise/lower shields");
        return;
    }

    if (command.size() < 2) {
        common::IO::warning("SHE usage <new shields>");
        return;
    }

    try {
        double newShields = std::stod(command[1]);
        if (newShields < 0.0) {
            common::IO::println("Invalid shields value; must be 0 or greater");
            return;
        }

        if (enterprise.getDocked()) {
            common::IO::println("Cannot raise shields while docked");
            return;
        }

        enterprise.adjustShields(newShields);
    } catch (const std::exception& e) {
        common::IO::warning("Please enter valid doubles");
        return;
    }
}

// Prints a damage report based off the Enterprise's devices
void Game::damageReportCommand() {
    if (enterprise.isDeviceBroken(std::string(Devices::DAMAGE_CONTROL))) {
        common::IO::println("Damage control needs repair. Cannot get damage control");
        return;
    }

    enterprise.damageReport();
}