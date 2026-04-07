#include "quadrant.hpp"

#include <random>
#include <cassert>
#include <algorithm>

// Constructs a quadrant using a random number generator
Quadrant::Quadrant() {
    static std::random_device rd;
    static std::mt19937 gen(rd());

    static std::uniform_int_distribution<> klingons(0,3);
    static std::uniform_int_distribution<> bases(0,1);
    static std::uniform_int_distribution<> stars(1,9);

    kbs = klingons(gen) * 100 + bases(gen) * 10 + stars(gen);
}

// Constructs a quadrant using an initial value
Quadrant::Quadrant(int initValue) {
    kbs = clampKBS(initValue); // clamps the value to be within the given range
}

// Constructs a new quadrant based off the number of klingons, bases, and stars
Quadrant::Quadrant(int klingons, int bases, int stars) {
    kbs = clampKBS(klingons * 100 + bases * 10 + stars); // clamps the value to be within the given range
}

// Gets the number of klingons inside the quadrant
int Quadrant::klingons() const {
    return kbs / 100;
}

// Gets the number of bases inside the quadrant
int Quadrant::bases() const {
    return (kbs / 10) % 10;
}

// Gets the number of stars inside the quadrant
int Quadrant::stars() const {
    return kbs % 10;
}

// Returns the Quadrant's raw kbs value
int Quadrant::raw() const {
    return kbs;
}

// Sets the raw kbs value to a new value
void Quadrant::setContent(int newValue) {
    kbs = clampKBS(newValue);
}

// Sets a new klingon value (does not affect the other)
void Quadrant::setKlingons(int newValue) {
    newValue = std::clamp(newValue, KLINGON_MIN, KLINGON_MAX);
    
    kbs = newValue * 100 + bases() * 10 + stars();
}

// Sets a new base value (does not affect the other)
void Quadrant::setBases(int newValue) {
    newValue = std::clamp(newValue, BASE_MIN, BASE_MAX);
    
    kbs = klingons() * 100 + newValue * 10 + stars();
}

// Sets a new star value (does not affect the other)
void Quadrant::setStars(int newValue) {
    newValue = std::clamp(newValue, STAR_MIN, STAR_MAX);
    
    kbs = klingons() * 100 + bases() * 10 + newValue;
}

#ifndef NDEBUG

    void Quadrant::whiteBoxTest() {
        int value = clampKBS(319);
        assert(value == 319);
    
        value = clampKBS(500);
        assert(value == 301);

        value = clampKBS(257);
        assert(value == 217);

        value = clampKBS(233);
        assert(value == 213);
    }
    
#endif

// Clamps the KBS value to their max/mins
int Quadrant::clampKBS(int kbs) {
    int k = kbs / 100; // gets the number of klingons
    int b = (kbs / 10) % 10; // gets the number of bases
    int s = kbs % 10; // gets the number of stars

    k = std::clamp(k, KLINGON_MIN, KLINGON_MAX);
    b = std::clamp(b, BASE_MIN, BASE_MAX);
    s = std::clamp(s, STAR_MIN, STAR_MAX);

    // for example: 581 becomes 311
    // another example: 000 becomes 001

    return k * 100 + b * 10 + s;
}

std::ostream& operator<<(std::ostream& os, const Quadrant& qu) {
    os << "Klingons: " << qu.klingons() << ", Bases: " << qu.bases() << ", Stars: " << qu.stars();
    return os;
}