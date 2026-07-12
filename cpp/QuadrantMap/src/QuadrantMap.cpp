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
    initializeQuadrant(q, toBase0(x), toBase0(y));
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

// Converts c to base-0, expects a base-1 input.
int QuadrantMap::toBase0(int c)
{
    return c - 1;
}

// Converts c to base-1, expects a base-0 input.
int QuadrantMap::toBase1(int c)
{
    return c + 1;
}

// Writes a fixed-width symbol into the specified sector.
// The backing String is copied into a StringBuilder so the
// three characters representing the sector can be replaced.
// The updated String then becomes the new map.
// Uses base-0 coordinates
void QuadrantMap::insertP(int x, int y, const std::string &value)
{
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
// Uses base-0 coordinates.
void QuadrantMap::clearSectorP(int x, int y)
{
    insertP(x, y, EMPTY);
}

// Moves a value from (x, y) to (newX, newY). It does
// this by checking if (x, y) is actually the value, and
// then clearing it, and inserting it into (newX, newY) after
// verifying that (newX, newY) is empty. Can be used to move
// Enterprise or Klingons.
// Uses base-0 coordinates
void QuadrantMap::moveP(int x, int y, int newX, int newY, const std::string &value)
{
    assert(validPos(x, y));
    assert(validPos(newX, newY));

    assert(atP(x, y) == value);

    if (emptyP(newX, newY))
    {
        insertP(newX, newY, value);
        clearSectorP(x, y);
    }
}

// Clears a sector only if it has value as its object. If it does then
// it is cleared.
// Uses base-0 coordinates. 
void QuadrantMap::removeObjectP(int x, int y, const std::string &object)
{
    assert(validPos(x, y));

    if (atP(x, y) == object)
    {
        clearSectorP(x, y);
    }
}

// Returns the symbol stored at the specified sector.
// The 2D coordinates are converted into a 1D index into
// the backing String, and the fixed-width symbol stored
// at that location is returned.
// Uses base-0 coordinates.
std::string QuadrantMap::atP(int x, int y) const
{
    assert(validPos(x, y));

    int index = getIndexFrom(x, y);
    return quadrantString.substr(index, SYMBOL_SIZE);
}

// Checks if sector (x, y) is empty.
// X, and y both use base-1 positions.
// Checks if at(x, y) == "   ".
// Uses base-1 coordinates
bool QuadrantMap::emptyP(int x, int y) const
{
    return atP(x, y) == EMPTY;
}

// Inserts a value into a random location. Uses base-0.
void QuadrantMap::insertValues(int amount, const std::string &value)
{
    assert(amount <= ROWS * COLS);

    while (amount--)
    {
        int x, y;

        generateRandomPosition(x, y);
        x = toBase1(x);
        y = toBase1(y);

        while (!empty(x, y))
        {
            generateRandomPosition(x, y);

            x = toBase1(x);
            y = toBase1(y);
        }

        insert(x, y, value);
    }
}

// Initializes the Quadrant by placing the Enterprise at (x, y),
// and uses the Quadrant information to place the rest of the
// objects.
void QuadrantMap::initializeQuadrant(Quadrant q, int x, int y)
{
    quadrantString.resize(ROWS * COLS * SYMBOL_SIZE, ' ');

    insertP(x, y, ENTERPRISE);
    insertValues(q.klingons(), KLINGON);
    insertValues(q.bases(), BASE);
    insertValues(q.stars(), STAR);
}

// Writes a fixed-width symbol into the specified sector.
// The backing String is copied into a StringBuilder so the
// three characters representing the sector can be replaced.
// The updated String then becomes the new map.
// Uses base-1 coordinates
void QuadrantMap::insert(int x, int y, const std::string &value)
{
    insertP(toBase0(x), toBase0(y), value);
}

// Removes whatever occupies the specified sector.
// Clearing is implemented by replacing the sector with
// the empty-space symbol.
// Uses base-1 coordinates.
void QuadrantMap::clearSector(int x, int y)
{
    clearSectorP(toBase0(x), toBase0(y));
}

// Moves a value from (x, y) to (newX, newY). It does
// this by checking if (x, y) is actually the value, and
// then clearing it, and inserting it into (newX, newY) after
// verifying that (newX, newY) is empty. Can be used to move
// Enterprise or Klingons.
// Uses base-1 coordinates
void QuadrantMap::move(int x, int y, int newX, int newY, const std::string &value)
{  
    moveP(toBase0(x), toBase0(y), toBase0(newX), toBase0(newY), value);
}

// Clears a sector only if it has value as its object. If it does then
// it is cleared.
// Uses base-1 coordinates. 
void QuadrantMap::removeObject(int x, int y, const std::string &object)
{
    removeObjectP(toBase0(x), toBase0(y), object);
}

// Returns the symbol stored at the specified sector.
// The 2D coordinates are converted into a 1D index into
// the backing String, and the fixed-width symbol stored
// at that location is returned.
// Uses base-1 coordinates.
std::string QuadrantMap::at(int x, int y) const
{
    return atP(toBase0(x), toBase0(y));
}

// Checks if sector (x, y) is empty.
// X, and y both use base-1 positions.
// Checks if at(x, y) == "   ".
// Uses base-1 coordinates
bool QuadrantMap::empty(int x, int y) const
{
    return emptyP(toBase0(x), toBase0(y));
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
            out += at(toBase1(j), toBase1(i)) + "|";
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