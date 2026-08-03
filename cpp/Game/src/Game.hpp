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

    Enterprise getEnterprise() const;

    void run();

    bool move(double warpFactor, double warpDirection);

private:
    void constructGame();

    bool canDock() const noexcept;

    void firePhasers(double phaserEnergy);
    void klingonsFire();

    bool handleCommand();
    
    void moveCommand(const std::vector<std::string>& command);
    void shortRangeCommand();
    void longRangeCommand();
    void phaserCommand(const std::vector<std::string>& command);
    void shieldCommand(const std::vector<std::string>& command);
    void damageReportCommand();

private:
    Enterprise enterprise;
    Galaxy galaxy;

    QuadrantMap map[8][8];
    
    std::vector<std::vector<std::vector<Klingon>>> klingons;
};