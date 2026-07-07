#include <QuadrantMap.hpp>

#include <common/random.hpp>

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

// Converts the 2D index (x, y) into a 1D index
// for the quadrantString. X, and y use base-0
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

    return x * SYMBOL_SIZE + y * COLS * SYMBOL_SIZE;
}

// Generates two random ints, one the x (0), and the other
// the y value (1). Returns an array. X, and y are returned
// as base-0 positions. Based off the COLS and ROWS.
void QuadrantMap::generateRandomPosition(int &x, int &y)
{
    x = common::randomInt(0, COLS - 1);
    y = common::randomInt(0, ROWS - 1);
}

// Checks whether the supplied 0-based coordinates lie within
// the bounds of the quadrant.
bool QuadrantMap::validPos(int x, int y)
{
    return x >= 0 && x < COLS && y >= 0 && y < ROWS;
}

// Inserts a value into a random location. Uses base-0.
void QuadrantMap::insertValues(int amount, const std::string &value)
{
    assert(amount <= ROWS * COLS);

    while (amount--)
    {
        int x;
        int y;

        generateRandomPosition(x, y);
        while (!empty(x + 1, y + 1))
        {
            generateRandomPosition(x, y);
        }

        insert(x + 1, y + 1, value);
    }
}

// Initializes the Quadrant by placing the Enterprise at (x, y), 
// and uses the Quadrant information to place the rest of the
// objects. 
void QuadrantMap::initializeQuadrant(Quadrant q, int x, int y)
{
    quadrantString.resize(ROWS * COLS * SYMBOL_SIZE, ' ');

    insert(x, y, ENTERPRISE);
    insertValues(q.klingons(), KLINGON);
    insertValues(q.bases(), BASE);
    insertValues(q.stars(), STAR);
}

// Writes a fixed-width symbol into the specified sector.
// The backing String is copied into a StringBuilder so the
// three characters representing the sector can be replaced.
// The updated String then becomes the new map.
void QuadrantMap::insert(int x, int y, const std::string &value)
{
    x--;
    y--;

    assert(validPos(x, y));

    if (value.size() != SYMBOL_SIZE)
        return;

    int index = getIndexFrom(x, y);
    for (size_t i = 0; i < SYMBOL_SIZE; ++i)
    {
        quadrantString[index + i] = value[i];
    }
}

// Removes whatever occupies the specified sector.
// Clearing is implemented by replacing the sector with
// the empty-space symbol.
void QuadrantMap::clearSector(int x, int y)
{
    insert(x, y, EMPTY);
}

// Moves a value from (x, y) to (newX, newY). It does
// this by checking if (x, y) is actually the value, and
// then clearing it, and inserting it into (newX, newY) after
// verifying that (newX, newY) is empty. Can be used to move
// Enterprise or Klingons.
void QuadrantMap::move(int x, int y, int newX, int newY, const std::string &value)
{
    assert(validPos(x - 1, y - 1));
    assert(validPos(newX - 1, newY - 1));

    assert(at(x, y) == value);

    if (empty(newX, newY))
    {
        insert(newX, newY, value);
        clearSector(x, y);
    }
}

// Clears a sector only if it has value as its object. If it does then
// it is cleared.
void QuadrantMap::removeObject(int x, int y, const std::string &object)
{
    assert(validPos(x - 1, y - 1));

    if (at(x, y) == object)
    {
        clearSector(x, y);
    }
}

// Returns the symbol stored at the specified sector.
// The 2D coordinates are converted into a 1D index into
// the backing String, and the fixed-width symbol stored
// at that location is returned.
std::string QuadrantMap::at(int x, int y) const
{
    x--;
    y--;
    assert(validPos(x, y));

    int index = getIndexFrom(x, y);
    return quadrantString.substr(index, SYMBOL_SIZE);
}

// Checks if sector (x, y) is empty.
// X, and y both use base-1 positions.
// Checks if at(x, y) == "   ".
bool QuadrantMap::empty(int x, int y) const
{
    return at(x, y) == EMPTY;
}

// Converts the QuadrantMap into a string
std::string QuadrantMap::toString() const
{
    std::string out;

    for (size_t i = 0; i < ROWS; ++i)
    {
        for (size_t j = 0; j < COLS * (SYMBOL_SIZE + 1); ++j)
        {
            out += '-';
        }
        out += "\n";

        for (size_t j = 0; j < COLS; ++j)
        {
            out += at(j + 1, i + 1) + "|";
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