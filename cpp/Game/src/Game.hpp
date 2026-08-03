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

    Enterprise getEnterprise() const;

    void run();

    bool move(double warpFactor, double warpDirection);

private:
    bool handleCommand();
    
    void moveCommand(const std::vector<std::string>& command);
    void shortRangeCommand();
    void longRangeCommand();
    void shieldCommand(const std::vector<std::string>& command);
    void damageReportCommand();

    static int convertTo1D(common::Location loc);

private:
    Enterprise enterprise;
    Galaxy galaxy;

    QuadrantMap map[8][8];
    
    std::vector<Klingon> klingons;

    /*
    I'm going soon, so I wanted to write this down.
    We can make Klingon access by adding a reference
    table to Game. Something like:

    row             col         actual vector of klingons
    std::vector<std::vector<std::vector<Klingon>>> klingons
    
    then access is simply klingon[y][x][klingon id number]

    this will be initialized at the creation of Game
    */
};