#include "quadrant.hpp"

#include <random>
#include <algorithm>

// Constructs a quadrant using a random number generator
Quadrant::Quadrant() {
    static std::random_device rd;
    static std::mt19937 gen(rd());
    static std::uniform_int_distribution<> dist(1, 319);

    // ensures that there is the correct amount of klingons/bases/stars
    kbs = clampKBS(dist(gen));   
}

// Constructs a quadrant using an initial value
Quadrant::Quadrant(int initValue) {
    kbs = clampKBS(initValue);
}

// Constructs a new quadrant based off the number of klingons, bases, and stars
Quadrant::Quadrant(int klingons, int bases, int stars) {
    kbs = clampKBS(klingons * 100 + bases * 10 + stars);
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

// Clamps the KBS value to their max/mins
int Quadrant::clampKBS(int kbs) {
    int k = kbs / 100;
    int b = (kbs / 10) % 10;
    int s = kbs % 10;

    k = std::clamp(k, KLINGON_MIN, KLINGON_MAX);
    b = std::clamp(b, BASE_MIN, BASE_MAX);
    s = std::clamp(s, STAR_MIN, STAR_MAX);

    // for example: 581 becomes 311
    // another example: 000 becomes 001

    return k * 100 + b * 10 + s;
}