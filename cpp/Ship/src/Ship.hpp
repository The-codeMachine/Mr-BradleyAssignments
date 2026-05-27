#pragma once

/**
 * 
 * This is the temporary super class of the Enterprise.
 * Currently, it has no functionality, and simply holds
 * the health, and position of the ship. I will add
 * functionality to it when it is necessary, and when
 * I get the instructions to. 
 * 
 */

class Ship
{
public:
    Ship(double shields, double health, int x, int y) 
    : shields(shields), health(health), x(x), y(y) {}

protected:

    double shields;
    double health;

    // position in the galaxy (which quadrant it is in)
    int x, y;

};