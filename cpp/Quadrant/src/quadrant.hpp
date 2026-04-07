#pragma once

/**
 * The Quadrant class consists of the following operations:
 * - Get klingons (gets the number of klingons in a quadrant)
 * - Get bases (gets the number of bases in a quadrant)
 * - Get stars (gets the number of stars in a quadrant)
 * - Get the raw content value
 * - Set a new content value
 * - Set a new of each type (set a new klingon number, base number, or stars number)
 * 
 * The Quadrant class can be constructed from:
 * - Nothing, will use a RNG to make a new random quadrant
 * - From an initial value, which content is set to
 * - From klingons, bases, and stars, with the correct clamping
 * 
 * There can be between 0-3 klingons per quadrant, 0-1 bases per quadrant, and 1-9 stars per quadrant
 * 
 */

class Quadrant {
public:
    Quadrant();
    Quadrant(int initValue);
    Quadrant(int klingons, int bases, int stars);

    int klingons() const;
    int bases() const;
    int stars() const;

    void setContent(int newValue); 
    int raw() const;

    void setKlingons(int newValue);
    void setBases(int newValue);
    void setStars(int newValue);

private:
    int clampKBS(int kbs);

private:
    // Data
    int kbs;

    // Constants
    static inline constexpr int KLINGON_MAX = 3;
    static inline constexpr int BASE_MAX = 1;
    static inline constexpr int STAR_MAX = 9;
    static inline constexpr int KLINGON_MIN = 0;
    static inline constexpr int BASE_MIN = 0;
    static inline constexpr int STAR_MIN = 1;
};