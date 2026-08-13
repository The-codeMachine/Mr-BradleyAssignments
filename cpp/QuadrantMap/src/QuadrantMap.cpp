#include <QuadrantMap.hpp>

#include <common/random.hpp>
#include <common/GameLib.hpp>
#include <common/IO.hpp>

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

    return common::toBase0(x) + common::toBase0(y) * common::COLS;
}

// Generates two random ints, one the x (0), and the other
// the y value (1). Returns an array. X, and y are returned
// as base-1 positions. Based off the COLS and ROWS.
void QuadrantMap::generateRandomPosition(int &x, int &y)
{
    x = common::randomInt(1, common::COLS);
    y = common::randomInt(1, common::ROWS);
}

// Checks whether the supplied 1-based coordinates lie within
// the bounds of the quadrant.
bool QuadrantMap::validPos(int x, int y)
{
    return x > 0 && x <= common::COLS && y > 0 && y <= common::ROWS;
}

// Inserts a value into a random location. Uses base-1.
void QuadrantMap::placeValues(int amount, const std::string &value)
{
    assert(amount <= common::ROWS * common::COLS);

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

// Places klingons and records their positions
void QuadrantMap::placeKlingons(int amount) {
    assert(amount <= common::ROWS * common::COLS);

    while (amount--)
    {
        int x, y;

        generateRandomPosition(x, y);

        while (!empty(x, y))
        {
            generateRandomPosition(x, y);
        }

        place(x, y, KLINGON);
        klingons.emplace_back(common::Location(common::toBase0(x), common::toBase0(y), 
            common::Location::INVALID, common::Location::INVALID));
    }
}

// Places a starbase inside the QuadrantMap. Records the position at baseLocation.
void QuadrantMap::placeBase(int amount) {
    baseLocation = {common::Location::INVALID, common::Location::INVALID, 
                    common::Location::INVALID, common::Location::INVALID};
    
    assert(amount <= common::ROWS * common::COLS);

    while (amount--)
    {
        int x, y;

        generateRandomPosition(x, y);

        while (!empty(x, y))
        {
            generateRandomPosition(x, y);
        }

        place(x, y, BASE);
        baseLocation = common::Location(common::toBase0(x), common::toBase0(y), 
            common::Location::INVALID, common::Location::INVALID);
    }
}

// Initializes the Quadrant by placing the Enterprise at (x, y),
// and uses the Quadrant information to place the rest of the
// objects.
void QuadrantMap::initializeQuadrant(Quadrant q, int x, int y)
{
    place(x, y, ENTERPRISE);
    placeKlingons(q.klingons());
    placeBase(q.bases());
    placeValues(q.stars(), STAR);
}

// Initializes the Quadrant without placing the Enterprise at (x, y),
// and uses the Quadrant information to place the rest of the
// objects.
void QuadrantMap::initializeQuadrant(Quadrant q)
{
    placeKlingons(q.klingons());
    placeBase(q.bases());
    placeValues(q.stars(), STAR);
}

// Writes a fixed-width symbol into the specified sector.
// The updated String then becomes the new map.
// Uses base-1 coordinates
void QuadrantMap::place(int x, int y, const std::string &value)
{
    assert(validPos(x, y));

    // checks are done within place
    int index = getIndexFrom(x, y);
    quadrantString.place(index, value);

    if (value == ENTERPRISE) {
        enterprise.sectorX = common::toBase0(x);
        enterprise.sectorY = common::toBase0(y);
    }
}

// Writes a fixed-width symbol into the specified sector.
// The updated String then becomes the new map.
// Uses base-0 coordinates through Location
void QuadrantMap::place(common::Location loc, const std::string& value) {
    place(common::toBase1(loc.sectorX), common::toBase1(loc.sectorY), value);
}

// Removes whatever occupies the specified sector.
// Clearing is implemented by replacing the sector with
// the empty-space symbol.
// Uses base-1 coordinates.
void QuadrantMap::clearSector(int x, int y)
{
    if (at(x, y) == ENTERPRISE) {
        enterprise.sectorX = common::Location::INVALID;
        enterprise.sectorY = common::Location::INVALID;
    }
    
    // checks like validPos are done within place
    place(x, y, EMPTY);
}

// Removes whatever occupies the specified sector.
// Clearing is implemented by replacing the sector with
// the empty-space symbol.
// Uses base-0 coordinates through Location.
void QuadrantMap::clearSector(common::Location loc) {
    clearSector(common::toBase1(loc.sectorX), common::toBase1(loc.sectorY));
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

// Moves a value from (x, y) to (newX, newY). It does
// this by checking if (x, y) is actually the value, and
// then clearing it, and inserting it into (newX, newY) after
// verifying that (newX, newY) is empty. Can be used to move
// Enterprise or Klingons.
// Uses base-0 coordinates through location
void QuadrantMap::move(common::Location oldLocation, common::Location newLocation, const std::string& value) {
    move(common::toBase1(oldLocation.sectorX), common::toBase1(oldLocation.sectorY),
        common::toBase1(newLocation.sectorX), common::toBase1(newLocation.sectorY), value);
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

// Clears a sector only if it has value as its object. If it does then
// it is cleared.
// Uses base-0 coordinates through Location. 
void QuadrantMap::removeObject(common::Location loc, const std::string& object) {
    removeObject(common::toBase1(loc.sectorX), common::toBase1(loc.sectorY), object);
}

// Gets the locations of all the klingons within this QuadrantMap (reference)
std::vector<Klingon>& QuadrantMap::getKlingons() {
    return klingons;
}

// Gets the locations of all the klingons within this QuadrantMap (const refernce)
const std::vector<Klingon>& QuadrantMap::getKlingons() const {
    return klingons;
}

// Returns the amount of damage klingons used to damage the Enterprise. Calculates
// damage based off distance, and reduces the Klingon's energy reserves
int QuadrantMap::klingonsFire() {
    if (klingons.size() <= 0 || canDock())
        return 0;
    
    int out = 0;

    for (Klingon& klingon : klingons) {
        int damage = klingon.firePhasers(enterprise.sectorX, enterprise.sectorY);

        common::IO::printf("Klingon %s has fired their phasers dealing: %d damage\n",
                            klingon.getLocation().sectorString().c_str(),
                            damage
                        );

        out += damage;
    }

    return out;
}

// Moves the klingons in the Quadrant to a random sector. Checks that it is a valid 
// sector and that the klingon can move there. 
void QuadrantMap::klingonsMove() {
    if (klingons.size() <= 0)
        return;

    for (Klingon& klingon : klingons) {
        auto location = klingon.calculateDestination();
        while (!empty(location)) {
            location = klingon.calculateDestination();
        }

        move(klingon.getLocation(), location, KLINGON);
        klingon.move(location);
    }
}

// Gets the location of the starbase within the QuadrantMap (reference)
common::Location& QuadrantMap::base() {
    return baseLocation;
}

// Gets the location of the starbase within the QuadrantMap (const reference)
const common::Location& QuadrantMap::base() const {
    return baseLocation;
}

// Checks whether the Enterprise can dock or not based off its current position.
// Returns true if the Enterprise can dock, and false elsewise. 
bool QuadrantMap::canDock() const noexcept {
    const int centerX = common::toBase1(enterprise.sectorX);
    const int centerY = common::toBase1(enterprise.sectorY);

    for (int y = centerY - 1; y <= centerY + 1; ++y) {
        for (int x = centerX - 1; x <= centerX + 1; ++x) {
            if (x < common::MIN_INDEX_1 || x > common::MAX_INDEX_1 || 
                y < common::MIN_INDEX_1 || y > common::MAX_INDEX_1)
                continue;

            if (at(x, y) == QuadrantMap::BASE)
                return true;
        }
    }

    return false;
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

// Returns the symbol stored at the specified sector.
// The 2D coordinates are converted into a 1D index into
// the backing String, and the fixed-width symbol stored
// at that location is returned.
// Uses base-0 coordinates through Location.
std::string QuadrantMap::at(common::Location loc) const {
    return at(common::toBase1(loc.sectorX), common::toBase1(loc.sectorY));
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

// Checks if sector (x, y) is empty.
// X, and y both use base-1 positions.
// Checks if at(x, y) == "   ".
// Uses base-0 coordinates through location
bool QuadrantMap::empty(common::Location loc) const {
    return empty(common::toBase1(loc.sectorX), common::toBase1(loc.sectorY));
}

// Converts the QuadrantMap into a string
std::string QuadrantMap::toString() const
{
    std::string out;

    for (size_t i = 1; i <= common::ROWS; ++i)
    {
        out += "---+---+---+---+---+---+---+---+\n";

        for (size_t j = 1; j <= common::COLS; ++j)
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