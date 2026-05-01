#include "quadrant.hpp"

#include <common/random.hpp>
#include <common/GameLib.hpp>

#include <cassert>
#include <iostream>
#include <iomanip>
#include <string>

// Constructs a quadrant using a random number generator
Quadrant::Quadrant() : Quadrant(common::genKlingons(), common::genBases(), common::genStars()) {}

// Constructs a new quadrant based off the number of klingons, bases, and stars
Quadrant::Quadrant(int klingons, int bases, int stars)
{
    common::totalQuadrants++;
    kbs = setContent(klingons, bases, stars);
}

// Gets the number of klingons inside the quadrant
int Quadrant::klingons() const
{
    return kbs / 100;
}

// Gets the number of bases inside the quadrant
int Quadrant::bases() const
{
    return (kbs / 10) % 10;
}

// Gets the number of stars inside the quadrant
int Quadrant::stars() const
{
    return kbs % 10;
}

// Removes one Klingon from this quadrant if one exists
void Quadrant::reduceKlingons()
{
    if (kbs >= 100) // removes overhead of klingons()
        kbs = setContent(klingons() - 1, bases(), stars());
}

#ifndef NDEBUG

// Tests the setContent function
void Quadrant::whiteBoxTest()
{
    std::cout << "Quadrant whitebox test\n";

    Quadrant q(3, 1, 8);

    assert(q.kbs == 318);
    std::cout << "Got " << q.kbs << ", expected 318\n";

    q.kbs = setContent(2, 1, 2);

    assert(q.kbs == 212);
    std::cout << "Got " << q.kbs << ", expected 212\n";

    std::cout << "Quadrant whitebox test success\n";

    // NOTE:
    // Invalid input cases are not programmatically tested here because setContent()
    // uses assertions. Assertion failures terminate the program and cannot be
    // caught or verified within the same execution flow.
    //
    // These cases were manually verified by running the program
    // and confirming that invalid inputs trigger assertion failures.
    //
    // This approach ensures correctness during development without introducing
    // exception handling, as per assignment constraints.

    // For example: setContent(-1, 0, 1); // triggers an Assertion error
}

#endif

// Encodes klingons, bases, and stars into a single integer (KBS format)
int Quadrant::setContent(int klingons, int bases, int stars)
{
    static constexpr int KLINGON_MAX = 3;
    static constexpr int KLINGON_MIN = 0;

    static constexpr int BASE_MAX = 1;
    static constexpr int BASE_MIN = 0;

    assert(klingons >= KLINGON_MIN && klingons <= KLINGON_MAX && "Klingon out of range");
    assert(bases >= BASE_MIN && bases <= BASE_MAX && "Base out of range");
    assert(stars >= STAR_MIN && stars <= STAR_MAX && "Star out of range");

    return klingons * 100 + bases * 10 + stars;
}

// returns a 3-digit string (with leading zeros if necessary) representing
// the quadrant contents in KBS format
std::ostream &operator<<(std::ostream &os, const Quadrant &qu)
{
    // could be changed to std::format("%03d", qu.kbs) but that requires C++20
    os << std::setw(3) << std::setfill('0') << qu.kbs;
    return os;
}

// Populates a Quadrant with klingons, bases, and stars
uint16_t populate() {
    /*
        Rules:
        
        - 20% chance that 1 klingon is present 
        - 5% chance that 2 klingons is present 
        - 2% chance that 3 klingons is present 
        - 73% chance that 0 klingons is present
      
        - 4% chance that 1 star base is present
        - Max of 1 per quadrant, and no more than 2 for each galaxy
    */

    return Quadrant::setContent(common::genKlingons(), common::genBases(), common::genStars());
}