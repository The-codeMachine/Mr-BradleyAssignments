#pragma once

#include <QuadrantMap.hpp>
#include <Enterprise.hpp>
#include <Galaxy.hpp>
#include <Ship.hpp>
#include <Klingon.hpp>

/**
 * 
 * The Game class is the main class of the
 * entire Super Star Trek recreation. It 
 * owns everything. It is responsible for 
 * the game loop, forwarding commands to the
 * Enterprise, handling game quiting, lost
 * and winning. A full list of its operations
 * include:
 *  - Construction (initializes everything)
 *  - Getting a QuadrantMap at a certain coordinate 
 *      (either by location, or global quadrant coordinates)
 *  - Running the game loop
 *  - Moving the Enterprise (and later other ships)
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
    void computerLibraryCommandPTD();
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