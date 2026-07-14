#include "QuadrantString.hpp"

#include <cassert>

QuadrantString::QuadrantString()
{
    quadrantString.resize(ARRAY_SIZE * CELL_SIZE, ' ');
}

// Checks whether the inputted index is a valid
// position.
bool QuadrantString::isValidIndex(int index)
{
    return index >= 0 && index < ARRAY_SIZE * CELL_SIZE;
}

// Formats the user-inputted index (0-63) to
// a string format (e.g. 63 would become 189)
int QuadrantString::formatIndex(int index)
{
    return index * CELL_SIZE;
}

// Gets what the cell at index is. Returns
// what string is held there,
std::string QuadrantString::at(int index) const
{
    index = formatIndex(index);
    assert(isValidIndex(index));

    return quadrantString.substr(index, CELL_SIZE);
}

// Checks whether the cell contains value.
bool QuadrantString::contains(int index, const std::string &value) const
{
    // checks are done within other functions
    return at(index) == value;
}

// Checks whether the cell at index is empty or
// not
bool QuadrantString::isEmpty(int index) const
{
    // checks are done within other functions
    return contains(index, EMPTY);
}

// Places the value at index, overrides whatever was there already
void QuadrantString::place(int index, const std::string &value)
{
    index = formatIndex(index);
    assert(isValidIndex(index));
    assert(value.length() == CELL_SIZE);

    for (int i = 0; i < CELL_SIZE; ++i) {
        quadrantString[index + i] = value[i];
    }
}

// Clears a cell at index (replaces it with empty)
void QuadrantString::clear(int index)
{
    // checks ar edone within place
    place(index, EMPTY);
}

// Gets the size if the 1D array
int QuadrantString::size() const
{
    return ARRAY_SIZE;
}

// Gets the length of the quadrantString
int QuadrantString::length() const
{
    return quadrantString.length();
}

std::string QuadrantString::toString() const
{
    return quadrantString;
}