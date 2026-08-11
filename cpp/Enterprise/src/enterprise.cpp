#include "Enterprise.hpp"

#include <common/IO.hpp>

Enterprise::Enterprise(int energy, common::Location location, double shields, 
                int torpedoes, bool docked) :
                Ship(energy, location),
                shields(shields), torpedoes(torpedoes), docked(docked) {}

Enterprise::Enterprise(int energy, double shields, 
                int torpedoes, bool docked) :
                Ship(energy),
                shields(shields), torpedoes(torpedoes), docked(docked) {}

// Docks the Enterprise. Replenishes energy, torpedoes
// and repairs all devices
void Enterprise::dock() {
    if (shields > 0) {
        common::IO::println("Shields lowered for docking");
        adjustShields(0);
    }

    adjustEnergy(3000 - getEnergy());
    torpedoes = 10;

    // repairs all devices fully
    devices.repairAll(10000);
}

// Returns if the Enterprise is destroyed.
// Will return true if the Enterprise is destoryed.
bool Enterprise::isDestroyed() const noexcept {
    return shields < 0;
}

// Makes the Enterprise move based off warpFactor
// and warpDirection, but double checks that the
// warp engines are still capable. This still allows
// the user to use impulse engines if the warp
// engines are offline. 
std::vector<common::Location> Enterprise::calculatePath(double warpFactor, double warpDirection) {
    if (devices.isDamaged(Devices::WARP_ENGINES) && warpFactor > 0.2) {
        common::IO::printf("Chief engineer Scott reports \"The engines won't take warp %.3f!\"\n", warpFactor);
        common::IO::println("Warp engines are damaged. Maximum speed is warp 0.2");
        return {};
    }

    return Ship::calculatePath(warpFactor, warpDirection);
}

// Moves the Enterprise and repairs its devices. It also makes a
// random event occur to the devices
void Enterprise::move(common::Location loc, double warpFactor) {
    int energyUsed = (int) (warpFactor * 8 + 0.5);
    if (getEnergy() < energyUsed) {
        common::IO::println("Engineering reports: ");
        common::IO::printf("\"Insufficient energy available for manuvering at warp %.3f!\"", warpFactor);
        
        if (shields < energyUsed - getEnergy() || devices.isDamaged("SHIELD CONTROL")) {
            common::IO::println("** Fatal Error **");
            common::IO::println("You have just stranded your ship in space;");
            common::IO::println("You have insufficient maneuvering energy,");
            common::IO::println("and shield control is presently incapable of");
            common::IO::println("cross-circutting to the engine room!");
            shields = -1; // kills the enterprise
            return;
        }

        common::IO::printf("Deflector control room acknowledges %d units of energy are presently deployed to the shields\n", shields);

        return;
    }

    adjustEnergy(-energyUsed);
    
    devices.repairOverTime(warpFactor);
    devices.randomEvent();
    Ship::move(loc, warpFactor);
}

// Makes the Enterprise take damage based off
// the effective phaser energy.
bool Enterprise::takeDamage(double phaserEnergy) {
    if (docked)
        return isDestroyed();

    devices.hitDamage(phaserEnergy, this->getEnergy());

    shields -= phaserEnergy;
    return isDestroyed();
}

// Calculates the amount of damage the phaser does to a klingon
int Enterprise::firePhasers(double phaserEnergy, int x, int y, int numKlingons) {
    double distance = std::sqrt(std::pow(getLocation().sectorX - x, 2) + std::pow(getLocation().sectorY - y, 2));
    double h = phaserEnergy / numKlingons;

    return (int) ((h / distance) * (common::random() + 2));
}

// Gets the number of torpedoes the enterprise has
int Enterprise::getTorpedoes() const noexcept {
    return torpedoes;
}

// Reduces the torpedoes by 1
void Enterprise::reduceTorpedoes() {
    if (torpedoes <= 0)
        return;
    
    torpedoes--;
}

// Adjusts the shields of the enterprise. Sets the shields
// to this exact value replenishes the energy
void Enterprise::adjustShields(double shields) {
    if (shields < 0) {
        return;
    }
    
    double diffShields = this->shields - shields;
    if (-diffShields > getEnergy()) {
        common::IO::println("Not enough energy to adjust shields to: " + std::to_string(shields));
        return;
    }

    adjustEnergy((int)(diffShields));
    this->shields = shields;
}

// gets the status of a specific device
bool Enterprise::isDeviceBroken(const std::string& str) const {
    return devices.isDamaged(str);
}

// Returns whether the Enterprise is docked or not
bool Enterprise::getDocked() const noexcept {
    return docked;
}

// Updates the docked status to this and does all repairs
void Enterprise::updateDocked(bool value) {
    docked = value;

    if (docked)
        dock();
}

// Prints a damage report for all the device's state of
// repair. 
void Enterprise::damageReport() const {
    common::IO::println(devices.damageReport());
}

std::string Enterprise::toString() const {
    return "Energy: " + std::to_string(getEnergy()) +
                "\nLocation: " + getLocation().toString() +
                "\nTorpedoes: " + std::to_string(torpedoes) +
                "\nShields: " + std::to_string(shields) +
                "\nDocked: " + std::to_string(docked); 
}