#include "quadrant.hpp"

#include <common/random.hpp>

#include <random>
#include <cassert>
#include <iostream>
#include <string>

// Constructs a quadrant using a random number generator
Quadrant::Quadrant()
{
    uint32_t r = generateRandom32();

    // Use bitwise AND to extract the first 2 bits (mask 0b11) to get a value from 0–3.
    int k = r & 3;

    // Right-shift by 2 bits and extract the next single bit (mask 0b1) for a 0–1 toggle.
    int b = (r >> 2) & 1;

    // Right-shift by 3 bits and extract 3 bits (mask 0b111), then offset by 1 for a 1–8 range.
    int s = ((r >> 3) & 7) + 1;

    kbs = setContent(k, b, s);
}

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
        kbs -= 100;
}

#ifndef NDEBUG

// Tests the setContent function
void Quadrant::whiteBoxTest()
{
    std::cout << "Quadrant whitebox test\n";

    int test_kbs = 0;
    test_kbs = setContent(KLINGON_MAX, BASE_MAX, STAR_MAX);

    assert(test_kbs == 318);

    test_kbs = setContent(2, 1, 2);

    assert(test_kbs == 212);

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
    os << qu.klingons() << qu.bases() << qu.stars();
    return os;
}