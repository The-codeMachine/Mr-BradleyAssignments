#include "quadrant.hpp"

#include <common/random.hpp>
#include <common/GameLib.hpp>

#include <cassert>
#include <iostream>
#include <iomanip>
#include <string>

// Constructs a quadrant using a random number generator
Quadrant::Quadrant() : Quadrant(genKlingons(), genBases(), genStars()) {}

// Constructs a new quadrant based off the number of klingons, bases, and stars
Quadrant::Quadrant(int klingons, int bases, int stars)
{
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

/*
Could not put the bases functions inside Galaxy
because they cannot effect the Quadrant's private
values. I cannot put it as default visibility because
Galaxy and Quadrant are in different packages.
*/

// Puts a base into the quadrant (to fix if there are no bases)
void Quadrant::putBase()
{
    int base = bases();

    if (base < 1)
    {
        kbs = setContent(klingons(), base + 1, stars());
    }
}

// Returns true if the quadrant has a base
bool Quadrant::hasBase() const
{
    return bases() >= 1;
}

// Removes all bases from a quadrant
void Quadrant::removeBase()
{
    kbs = setContent(klingons(), 0, stars());
}

// Generates the number of klingons in a quadrant
int Quadrant::genKlingons()
{
    // klingons: 0 1 2 3
    // 73% 20% 5% 2%
    int r = common::weightChoice({0.73, 0.2, 0.05, 0.02});
    return r;
}

// Generates the number of bases in a quadrant
int Quadrant::genBases()
{
    // 4% chance of a quadrant having a base
    if (common::chanceOf(4))
    {
        return 1;
    }

    return 0;
}

// Randomly generates a random number of stars between 1-8
int Quadrant::genStars()
{
    return common::randomInt(1, 8);
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

// Turns the KBS value into a string (zero padded)
std::string Quadrant::toString() const {
    return common::zeroFill(kbs, 3);
}

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
    os << qu.toString();
    return os;
}

// Populates a Quadrant with klingons, bases, and stars
uint16_t populate()
{
    /*
        Rules:

        - 20% chance that 1 klingon is present
        - 5% chance that 2 klingons is present
        - 2% chance that 3 klingons is present
        - 73% chance that 0 klingons is present

        - 4% chance that 1 star base is present
        - Max of 1 per quadrant, and no more than 2 for each galaxy
    */

    return Quadrant::setContent(Quadrant::genKlingons(), Quadrant::genBases(), Quadrant::genStars());
}
