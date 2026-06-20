#include <QuadrantMap.hpp>

#include <common/random.hpp>

#include <cassert>

QuadrantMap::QuadrantMap(uint16_t kbs)
{
    uint8_t klingons = kbs / 100;
    uint8_t bases = kbs / 10 % 10;
    uint8_t stars = kbs % 10;

    QuadrantMap(klingons, bases, stars);
}

QuadrantMap::QuadrantMap(uint8_t klingons, uint8_t bases, uint8_t stars)
{
    quadrantString.resize(192, ' ');
    
    insertValues(klingons, "+K+");
    insertValues(bases, ">!<");
    insertValues(stars, " * ");
    insertValues(1, "<*>"); // enterprise
}

QuadrantMap::QuadrantMap(const Quadrant& q) {
    QuadrantMap(q.klingons(), q.bases(), q.stars());
}

// Generates a random x, and y position based off the quadrant map's size
void QuadrantMap::generateRandomPosition(int &x, int &y)
{
    x = common::randomInt(0, ROWS - 1);
    y = common::randomInt(0, COLS - 1);
}

// Inserts a value into a random location
void QuadrantMap::insertValues(int amount, const std::string &value)
{
    while (amount--)
    {
        int x;
        int y;

        generateRandomPosition(x, y);
        while (!empty(x, y))
        {
            generateRandomPosition(x, y);
        }

        insert(x, y, value);
    }
}

// Checks if the x, and y values are valid
bool QuadrantMap::validPos(int x, int y) {
    return x >= 0 && x < COLS && y >= 0 && y < ROWS;
}

// Moves the enterprise from one square to another
void QuadrantMap::moveEnterprise(int x, int y, int newX, int newY) {
    assert(at(x, y) == "<*>");
    
    if (empty(newX, newY)) {
        insert(newX, newY, "<*>");
        clear(x, y);
    }
}

// Removes the klingon from the quadrant
void QuadrantMap::removeKlingon(int x, int y) {
    if (at(x, y) == "+K+") {
        clear(x, y);
    }
}

// Gets the value at (x, y)
std::string QuadrantMap::at(int x, int y) const
{
    assert(validPos(x, y));

    std::string out;

    for (size_t i = 0; i < SYMBOL_SIZE; ++i)
    {
        out.push_back(quadrantString[x * SYMBOL_SIZE + y * ROWS * SYMBOL_SIZE + i]);
    }

    return out;
}

// Checks if (x, y) is empty ("   ")
bool QuadrantMap::empty(int x, int y) const
{
    assert(validPos(x, y));

    return at(x, y) == "   ";
}

// Clears (x, y)
void QuadrantMap::clear(int x, int y) {
    if (empty(x, y))
        return;

    insert(x, y, "   ");
}

// Inserts value at (x, y) as long as nothing is there right now
void QuadrantMap::insert(int x, int y, std::string value)
{
    assert(validPos(x, y));

    if (value.size() < SYMBOL_SIZE)
        return;

    if (empty(x, y))
    {
        for (size_t i = 0; i < SYMBOL_SIZE; ++i)
        {
            quadrantString[x * SYMBOL_SIZE + y * ROWS * SYMBOL_SIZE + i] = value[i];
        }
    }
}

// Converts the QuadrantMap into a string
std::string QuadrantMap::toString() const {
    std::string out;

    for (size_t i = 0; i < ROWS; ++i) {
        out += "----------------------------------\n";

        for (size_t j = 0; j < COLS; ++j) {
            out += at(j, i) + "|";
        }

        out += "\n";
    }

    return out;
}

std::ostream &operator<<(std::ostream &os, const QuadrantMap &m) {
    os << m.toString();
    return os;
}