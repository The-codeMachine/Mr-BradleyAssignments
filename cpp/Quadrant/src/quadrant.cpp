#include "quadrant.hpp"

#include <random>
#include <cassert>
#include <stdexcept>
#include <iostream>

// Constructs a quadrant using a random number generator
Quadrant::Quadrant() {
    static std::random_device rd;
    static std::mt19937 gen(rd());

    static std::uniform_int_distribution<> klingons(KLINGON_MIN, KLINGON_MAX);
    static std::uniform_int_distribution<> bases(BASE_MIN, BASE_MAX);
    static std::uniform_int_distribution<> stars(STAR_MIN, STAR_MAX);


    setContent(klingons(gen), bases(gen), stars(gen));
}

// Constructs a new quadrant based off the number of klingons, bases, and stars
Quadrant::Quadrant(int klingons, int bases, int stars) {
    setContent(klingons, bases, stars);
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

// Sets a new klingon value (does not affect the other)
void Quadrant::setKlingons(int newValue) {
    setContent(newValue, bases(), stars());
}

// Sets a new base value (does not affect the other)
void Quadrant::setBases(int newValue) {
    setContent(klingons(), newValue, stars());
}

// Sets a new star value (does not affect the other)
void Quadrant::setStars(int newValue) {
    setContent(klingons(), bases(), newValue);
}

#ifndef NDEBUG

    // Tests the setContent function
    void Quadrant::whiteBoxTest() {
        setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

        assert(kbs == 318);

        setContent(2, 1, 2);
        
        assert(kbs == 212);

        try {
            setContent(0, 129, 1233);
        } catch (const std::exception& e) {
            std::cout << "There was a runtime exception, success\n";
        }
    }
    
#endif

// Sets the raw kbs value (ensures it is in a valid range)
void Quadrant::setContent(int klingons, int bases, int stars) {
    if (klingons > KLINGON_MAX || klingons < KLINGON_MIN)
        throw std::runtime_error("Klingon exceed MAX, or dropped under MIN");

    if (bases > BASE_MAX || bases < BASE_MIN)
        throw std::runtime_error("Base exceed MAX, or dropped under MIN");

    if (stars > STAR_MAX || stars < STAR_MIN)
        throw std::runtime_error("Star exceed MAX, or dropped under MIN");

    kbs = klingons * 100 + bases * 10 + stars;
}

std::ostream& operator<<(std::ostream& os, const Quadrant& qu) {
    os << "Klingons: " << qu.klingons() << ", Bases: " << qu.bases() << ", Stars: " << qu.stars();
    return os;
}