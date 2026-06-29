#include <QuadrantMap.hpp>

#include <common/random.hpp>

#include <cassert>

QuadrantMap::QuadrantMap(Quadrant &q, int x, int y) : quadrant(q)
{
    quadrantString.resize(ROWS * COLS * SYMBOL_SIZE, ' ');

    insert(x - 1, y - 1, ENTERPRISE); 
    insertValues(q.klingons(), KLINGON);
    insertValues(q.bases(), BASE);
    insertValues(q.stars(), STAR);
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

// Removes whatever occupies the specified sector.
// Clearing is implemented by replacing the sector with
// the empty-space symbol.
void QuadrantMap::clear(int x, int y)
{
    if (empty(x + 1, y + 1))
        return;

    insert(x, y, EMPTY);
}

// Writes a fixed-width symbol into the specified sector.
// The backing String is copied into a StringBuilder so the
// three characters representing the sector can be replaced.
// The updated String then becomes the new map.
void QuadrantMap::insert(int x, int y, std::string value)
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

// Inserts a value into a random location. Uses base-0. 
void QuadrantMap::insertValues(int amount, const std::string &value)
{
    while (amount--)
    {
        int x;
        int y;

        generateRandomPosition(x, y);
        while (!empty(x + 1, y + 1))
        {
            generateRandomPosition(x, y);
        }

        insert(x, y, value);
    }
}

// Moves the Enterprise to a new sector. 
// The move succeeds only if the destination sector is empty.
// Internally, the destination is updated before the previous
// sector is cleared so that the map always contains exactly
// one Enterprise. 
void QuadrantMap::moveEnterprise(int x, int y, int newX, int newY)
{
    assert(at(x, y) == ENTERPRISE);

    if (empty(newX, newY))
    {
        insert(newX - 1, newY - 1, ENTERPRISE);
        clear(x - 1, y - 1);
    }
}

// Removes a klingon from (x, y) and from the Quadrant.
// X, and y both use base-1 positions. Removes a klingon
// by checking if a klingon is there, and then clears it.
void QuadrantMap::removeKlingon(int x, int y)
{
    if (klingons() <= 0)
        return;

    if (at(x, y) == KLINGON)
    {
        clear(x - 1, y - 1);
        quadrant.reduceKlingons();
    }
}

// Returns the symbol stored at the specified sector.
// The 2D coordinates are converted into a 1D index into
// the backing String, and the fixed-width symbol stored
// at that location is returned.
std::string QuadrantMap::at(int x, int y) const
{
    assert(validPos(x - 1, y - 1));

    std::string out;
    int index = getIndexFrom(x - 1, y - 1);
    for (size_t i = 0; i < SYMBOL_SIZE; ++i)
    {
        out.push_back(quadrantString[index + i]);
    }

    return out;
}

// Gets the number of klingons in the quadrant
int QuadrantMap::klingons() const
{
    return quadrant.klingons();
}

// Gets the number of bases in the quadrant
int QuadrantMap::bases() const
{
    return quadrant.bases();
}

// Gets the number of stars in the quadrant
int QuadrantMap::stars() const
{
    return quadrant.stars();
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

    out += "Klingons: " + std::to_string(klingons()) +
           ", Bases: " + std::to_string(bases()) +
           ", Stars: " + std::to_string(stars());

    return out;
}

std::ostream &operator<<(std::ostream &os, const QuadrantMap &m)
{
    os << m.toString();
    return os;
}