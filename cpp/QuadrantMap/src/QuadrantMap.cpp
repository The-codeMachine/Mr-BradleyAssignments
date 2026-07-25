#include <QuadrantMap.hpp>

#include <common/random.hpp>
#include <common/GameLib.hpp>

#include <cassert>
#include <iostream>

// Constructs a QuadrantMap from quadrant Q,
// setting Enterprise's coordinates to (x, y).
//
// Design Note:
// The Enterprise position is supplied separately because
// a QuadrantMap is intended to represent the visible state
// of a quadrant after the Enterprise has entered it. The
// Quadrant stores the klingons, bases, and stars, while the
// Enterprise is considered part of the game state.
QuadrantMap::QuadrantMap(Quadrant q, int x, int y)
{
    initializeQuadrant(q, x, y);
}

// Constructs a QuadrantMap from quadrant Q.
QuadrantMap::QuadrantMap(Quadrant q) {
    initializeQuadrant(q);
}

QuadrantMap::QuadrantMap() : quadrantString() {}

// Converts the 2D index (x, y) into a 1D index
// for the quadrantString. X, and y use base-1
// positions. This uses the formula:
//
// y * AMOUNT_OF_COLUMNS (COLS) * SYMBOL_SIZE +
// x * SYMBOL_SIZE = the start index of the column
//
// Where y = amount of rows, and x = amount of columns.
// The calculation works because each row occupies
// COLS * SYMBOL_SIZE characters in the backing String.
// Multiplying y by this value skips entire rows,
// while x * SYMBOL_SIZE moves to the correct sector
// within that row.
int QuadrantMap::getIndexFrom(int x, int y)
{
    assert(validPos(x, y));

    return common::toBase0(x) + common::toBase0(y) * COLS;
}

// Generates two random ints, one the x (0), and the other
// the y value (1). Returns an array. X, and y are returned
// as base-1 positions. Based off the COLS and ROWS.
void QuadrantMap::generateRandomPosition(int &x, int &y)
{
    x = common::randomInt(1, COLS);
    y = common::randomInt(1, ROWS);
}

// Checks whether the supplied 1-based coordinates lie within
// the bounds of the quadrant.
bool QuadrantMap::validPos(int x, int y)
{
    return x > 0 && x <= COLS && y > 0 && y <= ROWS;
}

// Inserts a value into a random location. Uses base-1.
void QuadrantMap::placeValues(int amount, const std::string &value)
{
    assert(amount <= ROWS * COLS);

    while (amount--)
    {
        int x, y;

        generateRandomPosition(x, y);

        while (!empty(x, y))
        {
            generateRandomPosition(x, y);
        }

        place(x, y, value);
    }
}

// Initializes the Quadrant by placing the Enterprise at (x, y),
// and uses the Quadrant information to place the rest of the
// objects.
void QuadrantMap::initializeQuadrant(Quadrant q, int x, int y)
{
    place(x, y, ENTERPRISE);
    placeValues(q.klingons(), KLINGON);
    placeValues(q.bases(), BASE);
    placeValues(q.stars(), STAR);
}

// Initializes the Quadrant without placing the Enterprise at (x, y),
// and uses the Quadrant information to place the rest of the
// objects.
void QuadrantMap::initializeQuadrant(Quadrant q)
{
    placeValues(q.klingons(), KLINGON);
    placeValues(q.bases(), BASE);
    placeValues(q.stars(), STAR);
}

// Writes a fixed-width symbol into the specified sector.
// The backing String is copied into a StringBuilder so the
// three characters representing the sector can be replaced.
// The updated String then becomes the new map.
// Uses base-1 coordinates
void QuadrantMap::place(int x, int y, const std::string &value)
{
    assert(validPos(x, y));

    // checks are done within place
    int index = getIndexFrom(x, y);
    quadrantString.place(index, value);
}

// Removes whatever occupies the specified sector.
// Clearing is implemented by replacing the sector with
// the empty-space symbol.
// Uses base-1 coordinates.
void QuadrantMap::clearSector(int x, int y)
{
    // checks like validPos are done within place
    place(x, y, EMPTY);
}

// Moves a value from (x, y) to (newX, newY). It does
// this by checking if (x, y) is actually the value, and
// then clearing it, and inserting it into (newX, newY) after
// verifying that (newX, newY) is empty. Can be used to move
// Enterprise or Klingons.
// Uses base-1 coordinates
void QuadrantMap::move(int x, int y, int newX, int newY, const std::string &value)
{  
    assert(validPos(x, y));
    assert(validPos(newX, newY));

    assert(at(x, y) == value);

    if (empty(newX, newY))
    {
        clearSector(x, y);
        place(newX, newY, value);
    }
}

// Clears a sector only if it has value as its object. If it does then
// it is cleared.
// Uses base-1 coordinates. 
void QuadrantMap::removeObject(int x, int y, const std::string &object)
{
    assert(validPos(x, y));

    if (at(x, y) == object)
    {
        clearSector(x, y);
    }
}

// Returns the symbol stored at the specified sector.
// The 2D coordinates are converted into a 1D index into
// the backing String, and the fixed-width symbol stored
// at that location is returned.
// Uses base-1 coordinates.
std::string QuadrantMap::at(int x, int y) const
{
    assert(validPos(x, y));

    // getIndexFrom converts (x, y) to a valid 0-based index for QuadrantString
    int index = getIndexFrom(x, y);
    return quadrantString.at(index);
}

// Checks if sector (x, y) is empty.
// X, and y both use base-1 positions.
// Checks if at(x, y) == "   ".
// Uses base-1 coordinates
bool QuadrantMap::empty(int x, int y) const
{
    // getIndexFrom converts it to a base-0 1D index for quadrantString
    int index = getIndexFrom(x, y);
    return quadrantString.isEmpty(index);
}

// Converts the QuadrantMap into a string
std::string QuadrantMap::toString() const
{
    std::string out;

    for (size_t i = 1; i <= ROWS; ++i)
    {
        out += "---+---+---+---+---+---+---+---+\n";

        for (size_t j = 1; j <= COLS; ++j)
        {
            out += at(j, i) + "|";
        }

        out += "\n";
    }

    return out;
}

std::ostream &operator<<(std::ostream &os, const QuadrantMap &m)
{
    os << m.toString();
    return os;
}