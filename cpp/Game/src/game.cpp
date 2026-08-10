#include "Game.hpp"

#include <common/IO.hpp>

#include <numbers>
#include <algorithm>

Game::Game() : enterprise(3000, 0, 10, false) {
    constructGame();
}

// Constructs a game
void Game::constructGame() {
    initializeQuadrants();
    initializeTime();
    placeEnterprise();

    common::IO::printf("Your orders are as follows: \n");
    common::IO::printf("destroy the %d Klingon warships which have invaded\n", galaxy.getKlingons());
    common::IO::printf("the galaxy before they can attack Federation headquarters\n");
    common::IO::printf("on stardate: %d, this gives you %d days\n", startingStardate + missionDuration, missionDuration);
}

// Initializes all Quadrants in the Game
void Game::initializeQuadrants() {
    for (int x = 0; x < MAP_SIZE; ++x) {
        for (int y = 0; y < MAP_SIZE; ++y) {
            map[x][y] = QuadrantMap(galaxy.getQuadrant(common::toBase1(x), common::toBase1(y)));
        }
    }
}

// Initializes the current time
void Game::initializeTime() {
    currentStardate = (int)(common::random() * 20 + 20) * 100;
    startingStardate = currentStardate;
    missionDuration = 25 + (int)(common::random() * 10);
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
    while ( handleCommand() && 
            galaxy.getKlingons() > 0 && 
    currentStardate <= missionDuration + startingStardate) {}

    // success
    if (galaxy.getKlingons() <= 0) {
        common::IO::println("Congratulations, captain! The last klingon battle cruiser");
        common::IO::println("menacing the Federation has been destroyed.");
        common::IO::printf("Your efficiency rating is: %0.6f", 1000 * std::pow(currentStardate - startingStardate, 2));
        return;
    }

    // lost because of time
    if (currentStardate > missionDuration + startingStardate) {
        common::IO::printf("It is stardate: %0.6f\n", currentStardate);
        common::IO::println("You have failed to destroy all the Klingon warships");
        common::IO::println("before they could launch their attack against the");
        common::IO::println("Federation. They will destroy the Enterprise as well");
        common::IO::println("as the Federation");
        return;
    }
    
    // they destroyed the Enterprise elsewise
    common::IO::printf("It is stardate: %0.6f\n", currentStardate);
    common::IO::println("You have failed to destroy all the Klingon warships");
    common::IO::println("The Enterprise has been destroyed and now there is ");
    common::IO::println("nothing stopping the Klingons from destroying the");
    common::IO::println("Federation. ");
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
    enterprise.updateDocked(at(newLocation).canDock());

    return newLocation == path.back();
}

// Handles firing the phasers at all klingons. If a klingon is
// destroyed, it will handle destroying the klingon as well
void Game::firePhasers(double phaserEnergy) {
    enterprise.adjustEnergy(-phaserEnergy);

    const auto location = enterprise.getLocation();
    auto& currentKlingons = at(location).getKlingons();

    for (auto it = currentKlingons.begin(); it != currentKlingons.end();) {
        auto& klingon = *it;

        klingon.adjustEnergy(-enterprise.firePhasers(phaserEnergy, klingon.getLocation().sectorX,
                            klingon.getLocation().sectorY, currentKlingons.size()));

        if (!klingon.isDestroyed()) {
            ++it;
            continue;
        }

        auto klingonLocation = it->getLocation();

        common::IO::println(klingonLocation.toString());

        galaxy.reduceKlingons(location);
        at(location).removeObject(klingonLocation, QuadrantMap::KLINGON);

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

        return;
    }

    // if the next object in the path is a klingon then destroy it
    ++it;
    if (it == path.end())
        return;

    if (at(*it).at(*it) == QuadrantMap::KLINGON)
        destroyKlingon(*it);
}

// Destroys the klingon at a position. Removes it from QuadrantMap,
// the klingons vector, and galaxy. 
void Game::destroyKlingon(common::Location position) {
    auto& currKlingons = at(position).getKlingons();

    for (auto it = currKlingons.begin(); it != currKlingons.end();) {
        auto klingonLocation = it->getLocation();
        
        if (klingonLocation != position) {
            ++it;
            continue;
        }

        common::IO::println(position.toString());

        at(enterprise.getLocation()).removeObject(klingonLocation, QuadrantMap::KLINGON);

        currKlingons.erase(it);
        galaxy.reduceKlingons(enterprise.getLocation());
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
    } else if (name == "COM") {
        computerLibraryCommand(command);
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

        auto newLocation = enterprise.getLocation();
        if (oldPos != newLocation) {
            at(newLocation).klingonsMove();
            enterprise.takeDamage(at(newLocation).klingonsFire());
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
    common::IO::printf("Klingons left: %d\n", galaxy.getKlingons());
    common::IO::printf("Star date: %.6f\n", currentStardate);
}

 
// Does a long range scan around the Enterprise. Returns
// the quadrant's KBS value around the Enterprise. 
void Game::longRangeCommand() {
    if (enterprise.isDeviceBroken(std::string(Devices::LONG_RANGE_SENSORS))) {
        common::IO::println("Long Range Sensors need repair, cannot do scan");
        return;
    }

    galaxy.longRangeScan(enterprise.getLocation());
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
        enterprise.takeDamage(at(enterprise.getLocation()).klingonsFire());
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
        enterprise.takeDamage(at(enterprise.getLocation()).klingonsFire());
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
void Game::damageReportCommand() const {
    if (enterprise.isDeviceBroken(std::string(Devices::DAMAGE_CONTROL))) {
        common::IO::println("Damage control needs repair. Cannot get damage control");
        return;
    }

    enterprise.damageReport();
}

// Starts the library computer and forwards the user's request to 
// the specific library function. 
void Game::computerLibraryCommand(const std::vector<std::string>& command) {
    if (enterprise.isDeviceBroken("COMPUTER SYSTEMS")) {
        common::IO::println("Computer systems needs repairs, cannot access library computer");
        return;
    }
    
    if (command.size() < 2) {
        common::IO::warning("COM usage <COMMAND INDEX>");
        return;
    }

    try {
        int cmd = std::stoi(command[1]);

        switch(cmd) {
        case 0:
            computerLibraryCommandCGR();
            break;
        case 1:
            computerLibraryCommandSR();
            break;
        case 2:
            computerLibraryCommandPTD();
            break;
        case 3:
            computerLibraryCommandSND();
            break;
        case 4:
            computerLibraryCommandDC();
            break;
        case 5:
            computerLibraryCommandGRNM();
            break;
        default:
            common::IO::println("Invalid command index");
            break;
        }
    } catch (const std::exception& e) {
        common::IO::warning("Please enter a valid int");
        return;
    }
}

// Prints the cumulative galactic record to the user. This displays
// a full memory grid of all historical short range and long range
// scans the user has done so far. 
void Game::computerLibraryCommandCGR() const {
    galaxy.printScannedGalaxy();
}

// Prints a status report. Shows remaining Klingon warships,
// stardates left, starbases available, and the ship's current condition.
void Game::computerLibraryCommandSR() const {
    common::IO::println("Status Report:\n");
    common::IO::printf("Klingons left: %d\n", galaxy.getKlingons());
    common::IO::printf("Mission must be completed in %.6f stardates\n", missionDuration + (startingStardate - currentStardate));
    common::IO::printf("The Federation is maintaining %d starbases in the galaxy\n\n", galaxy.starBases());
    
    damageReportCommand();
}

// Calculates the prints the precise firing directions and distances
// from the Enterprise to every Klingon inside the Enterprise's current Quadrant.
void Game::computerLibraryCommandPTD() {
    common::Location startingLocation = enterprise.getLocation();
    std::vector<Klingon>& currKlingons = at(startingLocation).getKlingons();
    
    if (currKlingons.size() <= 0) {
        common::IO::println("No klingons found in this quadrant");
        return;
    }
    
    double direction;
    double factor;

    for (const auto& k : currKlingons) {
        // direction and factor are set to 0 in calculateDD
        calculateDD(startingLocation, k.getLocation(), direction, factor);

        // no need for factor for a torpedo
        common::IO::printf("Direction: %.6f\n", direction);
    }
}

// Provides the user with exact warp direction and distance needed to reach a 
// starbase located inside the Enterprise's current Quadrant.
void Game::computerLibraryCommandSND() const {
    common::Location startingPosition = enterprise.getLocation();

    common::Location starbaseLocation = at(startingPosition).base();
    if (starbaseLocation == common::Location{-1, -1, -1, -1}) {
        common::IO::println("No starbases in this quadrant");
        return;
    }

    // Determine which side of the starbase the Enterprise is on.
    const int dx = startingPosition.sectorX - starbaseLocation.sectorX;
    const int dy = startingPosition.sectorY - starbaseLocation.sectorY;

    // Select the starbase's neighboring sector closest to the Enterprise.
    //
    // If the Enterprise is east of the starbase, target the east neighbor.
    // If it is northeast, target the northeast neighbor, etc.
    common::Location targetLocation = starbaseLocation;

    if (dx > 0)
        ++targetLocation.sectorX;
    else if (dx < 0)
        --targetLocation.sectorX;

    if (dy > 0)
        ++targetLocation.sectorY;
    else if (dy < 0)
        --targetLocation.sectorY;

    double direction;
    double factor;

    calculateDD(startingPosition, targetLocation, direction, factor);

    common::IO::printf("Direction: %.6f\nFactor: %.6f\n", direction, factor);
}

// Calculates the distance and direction the Enterprise must travel to reach a 
// specific sector in inside its current Quadrant. 
void Game::computerLibraryCommandDC() const {
    common::Location startingPosition = enterprise.getLocation();
    common::Location endingPosition = common::IO::promptSector();

    double direction;
    double factor;

    calculateDD(startingPosition, endingPosition, direction, factor);

    common::IO::printf("Direction: %.6f\nFactor: %.6f\n", direction, factor);
}

// Prints the galatic region name map. There are 16 major galatic regions mapped
// across the game's world. 
void Game::computerLibraryCommandGRNM() const {
    constexpr int COLUMN_WIDTH = 20;

    constexpr std::array<std::array<std::string_view, 2>, 8> regions = {{
        { "Antares",     "Sirius" },
        { "Rigel",       "Deneb" },
        { "Procyon",     "Capella" },
        { "Vega",        "Betelgeuse" },
        { "Canopus",     "Aldebaran" },
        { "Altair",      "Regulus" },
        { "Sagittarius", "Arcturus" },
        { "Pollux",      "Spica" }
    }};

    common::IO::println("");
    common::IO::println(common::padCenter("THE GALAXY", COLUMN_WIDTH * 2));

    for (const auto& row : regions) {
        common::IO::printf(
            "%s%s\n",
            common::padCenter(std::string(row[0]), COLUMN_WIDTH).c_str(),
            common::padCenter(std::string(row[1]), COLUMN_WIDTH).c_str()
        );
    }

    common::IO::println("");
}

// Calculates the distance and direction something must travel to reach another 
// position. Returns the direction and factor as references. 
void Game::calculateDD(common::Location startingLocation, 
        common::Location endingLocation, double& direction, double& factor)         
{
    direction = 0.0;
    factor = 0.0;
    
    // calculate distance
    const double dx = endingLocation.sectorX - startingLocation.sectorX;
    const double dy = startingLocation.sectorY - endingLocation.sectorY;

    double distance = std::hypot(dx, dy);

    if (distance == 0.0)
        return;

    factor = distance / MAP_SIZE;

    double radians = std::atan2(dy, dx);
    double degrees = radians * (180.0 / std::numbers::pi);

    if (degrees < 0.0)
        degrees += 360;

    direction = 1 + degrees / 45;
    if (direction >= 9.0)
        direction -= 8.0;
}