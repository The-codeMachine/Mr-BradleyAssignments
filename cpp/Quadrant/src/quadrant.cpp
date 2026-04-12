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

    kbs = setContent(klingons(gen), bases(gen), stars(gen));
}

// Constructs a new quadrant based off the number of klingons, bases, and stars
Quadrant::Quadrant(int klingons, int bases, int stars) {
    kbs = setContent(klingons, bases, stars);
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

// Reduces the klingons by 1
void Quadrant::reduceKlingons() {
    if (klingons() >= 1)
        kbs -= 100;
}

#ifndef NDEBUG

    // Tests the setContent function
    void Quadrant::whiteBoxTest() {
        kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

        assert(kbs == 318);

        kbs = setContent(2, 1, 2);
        
        assert(kbs == 212);

        try {
            setContent(0, 129, 1233);
        } catch (const std::exception& e) {
            std::cout << "There was a runtime exception, success\n";
        }
        
        
        try {
            kbs = setContent(-42, -432, -123);
        } catch (const std::exception& e) {
            std::cout << "There was a runtime exception, success: " << e.what() << "\n";
        }

        kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

        std::cout << *this << "\n";
    }
    
#endif

// Sets the raw kbs value (ensures it is in a valid range)
int Quadrant::setContent(int klingons, int bases, int stars) {
    if (klingons > KLINGON_MAX || klingons < KLINGON_MIN)
        throw std::runtime_error("Klingon exceed MAX, or dropped under MIN");

    if (bases > BASE_MAX || bases < BASE_MIN)
        throw std::runtime_error("Base exceed MAX, or dropped under MIN");

    if (stars > STAR_MAX || stars < STAR_MIN)
        throw std::runtime_error("Star exceed MAX, or dropped under MIN");

    return klingons * 100 + bases * 10 + stars;
}

std::ostream& operator<<(std::ostream& os, const Quadrant& qu) {
    os << qu.klingons() << qu.bases() << qu.stars();
    return os;
}