#pragma once

#include <QuadrantMap.hpp>
#include <Enterprise.hpp>
#include <Galaxy.hpp>
#include <Ship.hpp>
#include <Klingon.hpp>

/**
 * 
 * The Game class is the largest in Super Star Trek. It
 * is the entry point of the entire system. It owns 
 * everything from the Enterprise to Klingons, and the
 * Galaxy. As such, there are few public functions, these
 * include:
 *  - Access a quadrant map at (x, y) 
 *  - Access to the Enterprise
 *  - Running the game
 *  - Moving the Enterprise (probably will be moved to private but
 *      it was public for tests).
 * 
 * All functions either take base-1 coordinates through raw values
 * (x, y), or base-0 coordinates through the Location class. 
 * 
 * Game handles the time as well. 
 * 
 * Game forwards the user's commands to their specific functions. It
 * tells the Klingons to fire, and move. Game handles win and lose 
 * conditions. It also handles all library computer functions. 
 * 
 */
class Game {
public:
    Game();

    QuadrantMap& at(int x, int y);
    QuadrantMap& at(common::Location location);

    const QuadrantMap& at(int x, int y) const;
    const QuadrantMap& at(common::Location location) const;

    Enterprise& getEnterprise();
    const Enterprise& getEnterprise() const;

    void run();

    bool move(double warpFactor, double warpDirection);

private:
    void constructGame();

    void initializeQuadrants();
    void initializeTime();
    void placeEnterprise();

    common::Location findMovementDestination(const std::vector<common::Location>& path, double& starDateChange);
    void updateEnterpriseMap(common::Location oldLocation, common::Location newLocation);

    void firePhasers(double phaserEnergy);
    void fireTorpedo(double warpDirection);

    void destroyKlingon(common::Location position);

    bool handleCommand();
    
    void moveCommand(const std::vector<std::string>& command);
    void shortRangeCommand();
    void longRangeCommand();
    void phaserCommand(const std::vector<std::string>& command);
    void torpedoCommand(const std::vector<std::string>& command);
    void shieldCommand(const std::vector<std::string>& command);
    void damageReportCommand() const;
    void computerLibraryCommand(const std::vector<std::string>& command);

    void computerLibraryCommandCGR() const;
    void computerLibraryCommandSR() const;
    void computerLibraryCommandPTD() const;
    void computerLibraryCommandSND() const;
    void computerLibraryCommandDC() const;
    void computerLibraryCommandGRNM() const;

    static void calculateDD(common::Location startingLocation, 
        common::Location endingLocation, double& direction, double& factor);

private:
    Enterprise enterprise;
    
    Galaxy galaxy;
    QuadrantMap map[8][8];

    double currentStardate;
    int startingStardate;
    int missionDuration;

private:
    static inline constexpr int MAP_SIZE = 8;

    static inline constexpr int MIN_SECTOR = 1;
    static inline constexpr int MAX_SECTOR = 8;

};