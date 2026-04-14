#pragma once

#include <ostream>

/**
 * A Quadrant holds the contents of the quadrant in terms
 * of the number of each item present. Each Quadrant may
 * hold Klingons [0..3], Bases [0..1], and Stars [1..8].
 *
 * The contents are packed into a 32-bit data type (int)
 * whose valid range is [1..318].
 *
 * The Klingons  value is stored in the 100's position
 * The StarBases value is stored in the  10's position
 * and the Stars value is stored in the   1's position
 *
 * The number of Klingons in the Quadrant is the only
 * value that may change, at this time.
 *
 * Operations
 *
 *  o Construct a Quadrant( k, b, s )
 *  o Construct a Quadrant() // uses an RNG to generate a random quadrant
 *  o return the current number of Klingons
 *  o return the number of Star Bases
 *  o return the number of Stars
 *  o provide a textual presentation of the Quadrant for
 *      use with displaying the Galactic Map eg, "318", or "001"
 *
 *  o provides the ability to decrement the number of Klingons
 *
 * @author Mr. Bradley
 * @version SPRING 2026
 */

class Quadrant
{
public:
    Quadrant();
    Quadrant(int klingons, int bases, int stars);

    int klingons() const;
    int bases() const;
    int stars() const;

    void reduceKlingons();

#ifndef NDEBUG

    static void whiteBoxTest();

#endif

    friend std::ostream &operator<<(std::ostream &os, const Quadrant &qu);

private:
    static int setContent(int klingons, int bases, int stars);

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