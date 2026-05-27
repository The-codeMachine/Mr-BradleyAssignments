#pragma once

#include <Device.hpp>
#include <Ship.hpp>

#include <vector>

/**
 *
 * The Enterprise is the player. It can move,
 * attack, defend, and dock at space stations.
 * The player may contorl the Enterprise. The
 * Enterprise inherits some functionality from
 * the ship super class (which klingons also
 * inherit from). The Enterprise holds 8
 * devices, each responsible for a specific
 * ability:
 *  - Warp Engines (1, "Navigate and Move")
 *  - Short Range Sensors (2, "Quadrant scan to see what is there")
 *  - Long Range Sensors (3, "Galactic scan to see what is nearby")
 *  - Phaser Control (4, "Shoot energy phasers at enemy ships")
 *  - Torpedo Control (5, "Fire 1-shot torpedoes at enemy ship")
 *  - Shield Control (6, "Power a shield against phaser fire")
 *  - Damage Control (7, "Status and repair devices")
 *  - Computer Systems (8, "Computer aid in navigation/weapons")
 *
 */

class Enterprise : Ship
{
    Enterprise(double shields, double health, int x, int y);

    void takeFire(double phaserEnergy, double distance);

    void dock();

    void event();

private:
    void warpRepair(double warpFactor);

private:
    int energy;
    int torpedoes;

    std::vector<Device> devices;

};