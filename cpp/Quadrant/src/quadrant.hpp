#pragma once

#include <ostream>

/**
 * The Quadrant class consists of the following operations:
 * - Get klingons (gets the number of klingons in a quadrant)  √
 * - Get bases (gets the number of bases in a quadrant)        √
 * - Get stars (gets the number of stars in a quadrant)        √
 * - Set a new value for each type (set a new klingon number, base number, or stars number) √
 * 
 * The Quadrant class can be constructed from:
 * - Nothing, will use a RNG to make a new random quadrant √
 * - From klingons, bases, and stars, with the correct clamping  √
 * 
 * There can be between 0-3 klingons per quadrant, 0-1 bases per quadrant, and 1-8 stars per quadrant [1 -> 318]
 * NB: Stars cap out at 8. [0..318]
 * 
 */

class Quadrant {
public:
    Quadrant();
    Quadrant(int klingons, int bases, int stars);

    int klingons() const;
    int bases() const;
    int stars() const;

    void setKlingons(int newValue);
    void setBases(int newValue);
    void setStars(int newValue);

    #ifndef NDEBUG

    void whiteBoxTest();
    
    #endif

    friend std::ostream& operator<<(std::ostream& os, const Quadrant& qu);

private:
    void setContent(int klingons, int bases, int stars);

private:
    // Data
    int kbs;

    // Constants
    static inline constexpr int KLINGON_MAX = 3;
    static inline constexpr int BASE_MAX = 1;
    static inline constexpr int STAR_MAX = 8;
    static inline constexpr int KLINGON_MIN = 0;
    static inline constexpr int BASE_MIN = 0;
    static inline constexpr int STAR_MIN = 1;
};