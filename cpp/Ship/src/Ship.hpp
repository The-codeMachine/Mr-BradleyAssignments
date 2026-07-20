#pragma once

/**
 * TODO:
 * Find a way to handle ship destruction 
 * within the shipl Maybe return a boolean
 * returning whether or not it was destroyed, 
 * and then the QuadrantMap will remove it
 * if it was destroyed. Or if the Enterprise
 * gets destroyed end the game. But we will
 * simply override that. 
 * 
 */

/**
 * TODO:
 * Add a phaser firing functionality. Make
 * sure the phaser calculation works. Currently,
 * though we don't do this until Mr. Bradley 
 * tells us to. We currently only need the 
 * movement functionality. 
 * 
 */

/**
 * 
 * This is the base Ship class. The ship class
 * consists of shield, and position information.
 * It handles movement calculation, damage reduction,
 * and phaser firing for all base ships. Other ships
 * like the Enterprise might use this as a super 
 * class and work upon the current functions 
 * (e.g. adding checks for devices).
 * Current list of operations consist of:
 *  - Move (move the ship based off warp factor, and direction)
 *  - Make the ship take damage
 *  - Fire the ship's phasers
 * 
 */
class Ship {
public:
    Ship(double shields, int sectorX, int sectorY, int quadrantX, int quadrantY);
    
    void getLocalLocation(int& x, int& y) const noexcept;
    void getGlobalLocation(int& x, int& y) const noexcept;
    double getShields() const noexcept;

    virtual void move(double warpFactor, double warpDirection);

    virtual void takeDamage(double phaserEnergy);
    virtual void firePhasers(double phaserEnergy, int x, int y);

private:
    double shields;

    int sectorX;
    int sectorY;

    int quadrantX;
    int quadrantY;

    static constexpr int GRID_SIZE = 8;
};