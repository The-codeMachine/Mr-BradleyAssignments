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
    for (int x = 0; x < common::MAP_SIZE; ++x) {
        for (int y = 0; y < common::MAP_SIZE; ++y) {
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

    // checks that it entered a new quadrant
    if (!newLocation.sameQuadrant(oldLocation))
        common::IO::printf("\nNow entering %s quadrant ...\n\n", Galaxy::getGalaticRegionName(newLocation).c_str());
}

// Gets the QuadrantMap at (x, y). This
// method takes base-1 coordinates.
QuadrantMap& Game::at(int x, int y) {
    return map[common::toBase0(x)][common::toBase0(y)];
}

// Gets the QuadrantMap at (x, y). This
// method takes base-1 location coordinates.
QuadrantMap& Game::at(common::Location location) {
    return map[common::toBase0(location.getQuadrantY())][common::toBase0(location.getQuadrantX())];
}

// Gets the QuadrantMap at (x, y). This
// method takes base-1 coordinates.
const QuadrantMap& Game::at(int x, int y) const {
    return map[common::toBase0(x)][common::toBase0(y)];
}

// Gets the QuadrantMap at (x, y). This
// method takes base-1 location coordinates.
const QuadrantMap& Game::at(common::Location location) const {
    return map[common::toBase0(location.getQuadrantY())][common::toBase0(location.getQuadrantX())];
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
    common::IO::println("Your mission begins with your starship located");
    common::IO::printf("in the galatic quadrant %s\n", Galaxy::getGalaticRegionName(enterprise.getLocation()).c_str());
    
    // checks the current quadrant
    shortRangeCommand();

    // handles command parsing 
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
        common::IO::printf("It is stardate: %0.3f\n", currentStardate);
        common::IO::printf("There were %d Klingon warships left to destroy\n", galaxy.getKlingons());
        common::IO::println("before they could launch their attack against the");
        common::IO::println("Federation. They will destroy the Enterprise as well");
        common::IO::println("as the Federation. ");
        return;
    }
    
    // the enterprise got destroyed
    // if the shields are == -1 then that means a special death occurred
    // and that death message already printed, so skip a death message. 
    if (enterprise.isDestroyed() && enterprise.getShields() != -1) {
        common::IO::printf("It is stardate: %0.3f\n", currentStardate);
        common::IO::printf("There were %d Klingon warships left to destroy\n", galaxy.getKlingons());
        common::IO::println("The Enterprise has been destroyed and now there is ");
        common::IO::println("nothing stopping the Klingons from destroying the");
        common::IO::println("Federation. ");
    }

    common::IO::println("The Federation is in need of a new starship commander");
    common::IO::println("for a similar mission -- if there is a volunteer let");
    common::IO::println("him step forward and say \"Aye.\"");

    // restart the game if the player enters AYE
    if (common::IO::toUpper(common::IO::prompt("")) == "AYE") {
        enterprise = Enterprise(3000, 0.0, 10, false);
        constructGame();
        run();
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

// Destroys a star base within the Galaxy. Removes it from the quadrant
// and the galaxy
void Game::destroyStarbase(common::Location location) {
    common::IO::println("***Starbase Destroyed***");

    galaxy.reduceStarBases(location);
    at(location).removeObject(location, QuadrantMap::BASE);

    if (galaxy.starBases() > 0 || galaxy.getKlingons() > missionDuration + startingStardate - currentStardate) {
        common::IO::println("Starfleet command is reviewing your record to consider court martial");
    } else {
        common::IO::println("That does it, Captain! You are hereby relieved of command");
        common::IO::println("and sentenced to 99 stardates at hard labour on Cygnus 12!");
        enterprise.kill();
    }
}

// Handles firing the phasers at all klingons. If a klingon is
// destroyed, it will handle destroying the klingon as well
void Game::firePhasers(double phaserEnergy) {
    const auto location = enterprise.getLocation();
    auto& currentKlingons = at(location).getKlingons();
    
    if (currentKlingons.size() <= 0) {
        common::IO::println("Science officer Spock reports: ");
        common::IO::println("\"Sensors show no enemy ships in this quadrant\"");
        return;
    }
    
    enterprise.adjustEnergy(-phaserEnergy);

    if (enterprise.isDeviceBroken(std::string(Devices::COMPUTER_SYSTEMS))) {
        common::IO::println("Computer failure hampers accuracy");
    }

    common::IO::println("Phasers locked on target.");

    int klingonSize = currentKlingons.size();
    for (auto it = currentKlingons.begin(); it != currentKlingons.end();) {
        auto& klingon = *it;
        auto klingonLocation = klingon.getLocation();

        int damage = enterprise.firePhasers(phaserEnergy, klingonLocation.getSectorY(),
                            klingonLocation.getSectorX(), klingonSize);

        klingon.adjustEnergy(-damage);

        common::IO::printf("%d unit hit on Klingon at %s\n",
                            damage, klingonLocation.sectorString().c_str());


        if (!klingon.isDestroyed()) {
            common::IO::printf("(Sensors show %d units remaining on klingon: %s)\n", 
                                klingon.getEnergy(), klingonLocation.sectorString().c_str());
            
            ++it;
            continue;
        }

        destroyKlingon(common::Location(klingonLocation.getSectorY(), klingonLocation.getSectorX(), location.getQuadrantY(), location.getQuadrantX()));
        it = currentKlingons.begin();
    }
}

// Fires a torpedo from the Enterprise based off a warpFactor and
// warpDircetion.
void Game::fireTorpedo(double warpDirection) {
    if (enterprise.getTorpedoes() <= 0) {
        common::IO::println("All photon torpedoes expended");   
        return;
    }

    enterprise.reduceTorpedoes();
    // biggest warp factor possible so it won't stop
    const auto& path = enterprise.calculatePath(8.0, warpDirection);

    double nullValue = 0;
    auto destination = findMovementDestination(path, nullValue);
    auto currLocation = enterprise.getLocation();

    // finds where the torpedo stopped
    auto it = std::find(path.begin(), path.end(), destination);
    // if it is not found then the torpedo stopped at its first sector
    if (it == path.end()) {
        it = path.begin();
    } else {
        // if the next object is not within the path it went out of the galaxy
        ++it;
        if (it == path.end()) {
            common::IO::println("Torpedo missed");
            return;
        }
    }

    // prints the torpedo's track
    common::IO::println("Torpedo Track");
    for (auto iterator = path.begin(); iterator <= it; ++iterator) {
        common::IO::println(iterator->toString());
    }   

    // checks that the torpedo is in the same quadrant
    if (!destination.sameQuadrant(currLocation)) {
        common::IO::println("Torpedo missed");
        return;
    }

    common::Location loc = *it;
    std::string sector = at(loc).at(loc);
    if (sector == QuadrantMap::KLINGON) {
        destroyKlingon(loc);
    } else if (sector == QuadrantMap::BASE) {
        destroyStarbase(loc);
    } else if (sector == QuadrantMap::STAR) {
        common::IO::printf("Star at %s absorbed torpedo energy\n", loc.sectorString().c_str());
    }
}

// Destroys the klingon at a position. Removes it from QuadrantMap,
// the klingons vector, and galaxy. Returns an iterator pointing to the
// next klingon. 
bool Game::destroyKlingon(common::Location position) {
    auto& klingons = at(position).getKlingons();

    for (auto it = klingons.begin(); it != klingons.end(); ++it) {
        if (!it->getLocation().sameSector(position))
            continue;

        const auto klingonLocation = it->getLocation();

        at(position).removeObject(klingonLocation, QuadrantMap::KLINGON);
        galaxy.reduceKlingons(position);

        common::IO::printf(
            "***Klingon Destroyed***\nKlingon: %s\n",
            klingonLocation.sectorString().c_str()
        );

        klingons.erase(it);
        return true;
    }

    return false;
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
        bool moveSuccess = move(warpFactor, warpDirection);

        auto newLocation = enterprise.getLocation();
        if (oldPos != newLocation) {
            at(newLocation).klingonsMove();
            enterprise.takeDamage(at(newLocation).klingonsFire());
            if (enterprise.isDestroyed())
                return;
        }

        if (!moveSuccess) {
            common::IO::printf("Warp engines shut down at %s due to bad navigation\n", newLocation.sectorString().c_str());
        }

        shortRangeCommand();
    } catch (const std::exception& e) {
        common::IO::warning("Error occurred: " + std::string(e.what()));
        return;
    }
}

// The Enterprise does a short range scan.
void Game::shortRangeCommand() {
    if (enterprise.isDeviceBroken(std::string(Devices::SHORT_RANGE_SENSORS))) {
        common::IO::println("Short Range Sensors need repair, cannot do scan");
        return;
    }

    // Status conditions
    const auto& location = enterprise.getLocation();
    std::string condition = "Green";

    if (enterprise.getEnergy() < 300)
        condition = "Yellow";

    if (galaxy.getQuadrant(location).klingons() > 0)
        condition = "*Red*";

    if (enterprise.getDocked())
        condition = "Docked";

    common::IO::printf("Status condition: %s\n", condition.c_str());

    // Split map and status into individual lines
    std::istringstream mapStream(at(location).toString());
    std::istringstream statusStream(enterprise.toString());

    std::string mapLine;
    std::string statusLine;

    std::vector<std::string> statusLines;

    while (std::getline(statusStream, statusLine)) {
        statusLines.push_back(statusLine);
    }

    statusLines.push_back(std::format("Klingons left: {}", galaxy.getKlingons()));

    statusLines.push_back(std::format("Star date: {:.3f}", currentStardate));

    // Print map alongside status
    std::size_t statusIndex = 0;

    while (std::getline(mapStream, mapLine)) {
        if (statusIndex < statusLines.size()) {
            common::IO::printf("%-40s   %s\n",
                mapLine.c_str(), statusLines[statusIndex].c_str());
                
            ++statusIndex;
        } else {
            common::IO::printf("%s\n", mapLine.c_str());
        }
    }

    // Print any remaining status lines
    while (statusIndex < statusLines.size()) {
        common::IO::printf("%-40s   %s\n",
            "", statusLines[statusIndex].c_str());
            
        ++statusIndex;
    }

    common::IO::println("");
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
    } catch (const std::exception& e) {
        common::IO::warning("Error occurred: " + std::string(e.what()));
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
    } catch (const std::exception& e) {
        common::IO::warning("Error occurred: " + std::string(e.what()));
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
        common::IO::warning("Error occurred: " + std::string(e.what()));
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
    
    // repair the devices
    double repairTime = enterprise.estimateRepairDevices();

    // means no devices need repairing, so do not ask
    if (repairTime <= 0.0)  
        return;

    common::IO::println("Technicians standing by to effect repairs to your ship;");
    common::IO::printf("estimated time to repair %.3f stardates\n", repairTime);

    char ans = common::IO::prompt("will you authorize the repair order (Y/N): ")[0];
    if (std::toupper(ans) == 'Y')  {
        currentStardate += repairTime;
        enterprise.repairDevices();
    }
}

// Starts the library computer and forwards the user's request to 
// the specific library function. 
void Game::computerLibraryCommand(const std::vector<std::string>& command) {
    if (enterprise.isDeviceBroken(std::string(Devices::COMPUTER_SYSTEMS))) {
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
        common::IO::warning("Error occurred: " + std::string(e.what()));
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
void Game::computerLibraryCommandSR() {
    common::IO::println("Status Report:\n");
    common::IO::printf("Klingons left: %d\n", galaxy.getKlingons());
    common::IO::printf("Mission must be completed in %.3f stardates\n", missionDuration + (startingStardate - currentStardate));

    // checks that there are still starbases in the galaxy
    if (galaxy.starBases() < 1) {
        common::IO::println("Your stupidity has left you on your on in the galaxy -- you have no starbases left");
    } else {
        common::IO::printf("The Federation is maintaining %d starbases in the galaxy\n\n", galaxy.starBases());
    }
    
    damageReportCommand();
}

// Calculates the prints the precise firing directions and distances
// from the Enterprise to every Klingon inside the Enterprise's current Quadrant.
void Game::computerLibraryCommandPTD() const {
    common::Location startingLocation = enterprise.getLocation();
    const std::vector<Klingon>& currKlingons = at(startingLocation).getKlingons();
    
    if (currKlingons.size() <= 0) {
        common::IO::println("Science officer Spock reports: ");
        common::IO::println("\"Sensors show no enemy ships in this quadrant\"");
        return;
    }
    
    double direction;
    double factor;

    common::IO::println("From Enterprise to Klingon battle cruiser: ");
    for (const auto& k : currKlingons) {
        // direction and factor are set to 0 in calculateDD
        calculateDD(startingLocation, k.getLocation(), direction, factor);

        common::IO::printf("\nKlingon %s: \n", k.getLocation().sectorString().c_str());
        common::IO::printf("Direction: %.10f\n", direction);
        common::IO::printf("Factor: %.10f\n", factor);
    }
}

// Provides the user with exact warp direction and distance needed to reach a 
// starbase located inside the Enterprise's current Quadrant.
void Game::computerLibraryCommandSND() const {
    common::Location startingPosition = enterprise.getLocation();

    common::Location starbaseLocation = at(startingPosition).base();
    if (starbaseLocation == common::Location{common::Location::INVALID, common::Location::INVALID, 
                                            common::Location::INVALID, common::Location::INVALID}) {
        common::IO::println("Mr. Spock reports: ");
        common::IO::println("\"Sensors show no starbases in this quadrant\"");
        return;
    }

    // Determine which side of the starbase the Enterprise is on.
    const int dx = startingPosition.getSectorY() - starbaseLocation.getSectorY();
    const int dy = startingPosition.getSectorX() - starbaseLocation.getSectorX();

    // Select the starbase's neighboring sector closest to the Enterprise.
    //
    // If the Enterprise is east of the starbase, target the east neighbor.
    // If it is northeast, target the northeast neighbor, etc.
    common::Location targetLocation = starbaseLocation;

    if (dx > 0)
        targetLocation.setSectorY(targetLocation.getSectorY() + 1);
    else if (dx < 0)
        targetLocation.setSectorY(targetLocation.getSectorY() - 1);

    if (dy > 0)
        targetLocation.setSectorX(targetLocation.getSectorX() + 1);
    else if (dy < 0)
        targetLocation.setSectorX(targetLocation.getSectorX() - 1);

    double direction;
    double factor;

    common::IO::println("From Enterprise to Starbase: ");
    calculateDD(startingPosition, targetLocation, direction, factor);

    common::IO::printf("Direction: %.10f\nFactor: %.10f\n", direction, factor);
}

// Calculates the distance and direction the Enterprise must travel to reach a 
// specific sector in inside its current Quadrant. 
void Game::computerLibraryCommandDC() const {
    common::IO::println("Initial sector: ");
    common::Location startingPosition = common::IO::promptSector();

    common::IO::println("Final sector: ");
    common::Location endingPosition = common::IO::promptSector();

    double direction;
    double factor;

    calculateDD(startingPosition, endingPosition, direction, factor);

    common::IO::printf("Direction: %.10f\nFactor: %.10f\n", direction, factor);
}

// Prints the galatic region name map. There are 16 major galatic regions mapped
// across the game's world. 
void Game::computerLibraryCommandGRNM() const {
    Galaxy::printGalaticRegionMap();
}

// Calculates the distance and direction something must travel to reach another 
// position. Returns the direction and factor as references. 
void Game::calculateDD(common::Location startingLocation, 
        common::Location endingLocation, double& direction, double& factor)         
{
    direction = 0.0;
    factor = 0.0;
    
    // calculate distance
    const double dx = endingLocation.getSectorY() - startingLocation.getSectorY();
    const double dy = startingLocation.getSectorX() - endingLocation.getSectorX();

    double distance = std::hypot(dx, dy);

    if (distance == 0.0)
        return;

    factor = distance / common::MAP_SIZE;

    double degrees = common::degrees(std::atan2(dy, dx));

    if (degrees < 0.0)
        degrees += 360;

    direction = 1 + degrees / 45.0;
    if (direction >= 9.0)
        direction -= 8.0;
}