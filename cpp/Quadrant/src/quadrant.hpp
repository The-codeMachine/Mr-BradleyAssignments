#pragma once

#include <ostream>

/**
 * A Quadrant holds the contents of the quadrant in terms
 * of the number of each item present. Each Quadrant may
 * hold Klingons [0..3], Bases [0..1], and Stars [1..8].
 *
 * The contents are packed into a 16-bit data type (uint16_t)
 * whose valid range is [1..318].
 *
 * The Klingons  value is stored in the 100's position
 * The StarBases value is stored in the  10's position
 * and the Stars value is stored in the   1's position
 *
 * The number of Klingons in the Quadrant is the only
 * value that may change, at this time.
 * 
 * There can only be 2 bases for the entire galaxy,
 * and there must be at least 1 base for the galaxy. 
 * There is a 4% chance for a base to generate inside a quadrant. 
 *
 * There is a 20% chance for there to be 1 klingon in a quadrant,
 * 5% chance for 2 klingons, and 2% chance for 3 klingons in the quadrant. 
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
    friend uint16_t populate();

private:
    static int setContent(int klingons, int bases, int stars);

private:
    // Data
    uint16_t kbs;

    // Constants
    static inline constexpr int STAR_MAX = 8;
    static inline constexpr int STAR_MIN = 1;
};

/* Sample Output

Testing Quadrant getters
Got 3 klingons, expected 3
Got 1 bases, expected 1
Got 8 stars, expected 8
Quadrant getters success
Testing Quadrant constructors
Got 0 klingons, expected between 0-3
Got 0 bases, expected between 0-1
Got 8 stars, expected between 1-8
Got Quadrant: Klingons(3), Bases(1), Stars(2), expected Quadrant: Klingons(3), Bases(1), Stars(2)
Quadrant constructor success
Testing Quadrant << operator
Got 318, expected "318"
Got 001, expected "001"
Quadrant << operator success
Testing Quadrant reduceKlingons
Got 2 klingons, expected 2
Got 1 klingons, expected 1
Got 0 klingons, expected 0
Got 0 klingons, expected 0
Quadrant reduceKlingons success
Quadrant galaxy construction test
Number of quadrants with 1 klingon: 20% <- these may change due to statistical noise
Number of quadrants with 2 klingon: 5% <- these may change due to statistical noise
Number of quadrants with 3 klingon: 2% <- these may change due to statistical noise
Number of quadrants with bases: 0% <- these may change due to statistical noise
Time taken: 49 ms <- this one may change based off your system's hardware
Quadrant galaxy construction success
Quadrant whitebox test
Got 318, expected 318
Got 212, expected 212
Quadrant whitebox test success

 */