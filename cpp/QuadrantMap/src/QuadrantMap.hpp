#pragma once

#include <quadrant.hpp>

#include <string>
#include <ostream>
#include <cstdint>

/**
 * QuadrantMap handles all of the movement and positional
 * status for all objects within a Quadrant. Currently this
 * includes: klingons, stars, bases, and the Enterprise. 
 * Operations include:
 *  - Construction (through Quadrant)
 *  - Insert an object
 *  - Clear a sector
 *  - Move an object
 *  - Remove an object
 *  - Check what object is at a certain sector
 *  - Check if a sector is empty
 *  - Convert map to string
 *
 * Currently, there are 8 rows and 8 columns, with each
 * symbol being 3 big.
 * 
 * All public methods use 1-based coordinates because they represent
 * the quadrant from the player's perspective. Players naturally
 * think of the first sector as (1,1), rather than (0,0).
 *
 * Private helper methods use 0-based coordinates because the internal
 * String representation uses Java's natural 0-based indexing. This
 * simplifies conversion between 2D sector coordinates and the 1D
 * String representation.
 *
 * Conversion between the two coordinate systems occurs only at the
 * public API boundary. Public methods convert to 0-based coordinates
 * before calling private helper methods.
 *
 */
class QuadrantMap
{
public:
   QuadrantMap(Quadrant q, int x, int y);

   void insert(int x, int y, const std::string& value);
   void clearSector(int x, int y);

   void move(int x, int y, int newX, int newY, const std::string& value);
   void removeObject(int x, int y, const std::string& object);

   std::string at(int x, int y) const;
   bool empty(int x, int y) const;

   std::string toString() const;

   friend std::ostream &operator<<(std::ostream &os, const QuadrantMap &m);

public:
   static inline constexpr const char* KLINGON = "+K+";
   static inline constexpr const char* BASE = ">!<";
   static inline constexpr const char* STAR = " * ";
   static inline constexpr const char* ENTERPRISE = "<*>";
   static inline constexpr const char* EMPTY = "   ";

private:
   static int getIndexFrom(int x, int y);
   static void generateRandomPosition(int &x, int &y);
   static bool validPos(int x, int y);

   void insertValues(int amount, const std::string &value);

private:
   std::string quadrantString;

   static inline constexpr size_t ROWS = 8;
   static inline constexpr size_t COLS = 8;
   static inline constexpr size_t SYMBOL_SIZE = 3;
};

/*
Sample Output

QuadrantMap test
--------------------------------
   |   |   |   |   | * |   |   |
--------------------------------
   |   |+K+|   |   |   |   |   |
--------------------------------
   |   |   |   | * |   |   |   |
--------------------------------
   | * |   |   |   | * |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   | * |   |   |   |
--------------------------------
<*>|   |+K+| * |   |   |   |   |
Klingons: 2, Bases: 0, Stars: 6
(5, 3): <  *  >
Is (5, 3) empty: 0

Klingons: 2
Bases: 0
Stars: 6
Enter (x, y) to remove a klingon: 3 2
--------------------------------
   |   |   |   |   | * |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   | * |   |   |   |
--------------------------------
   | * |   |   |   | * |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   | * |   |   |   |
--------------------------------
<*>|   |+K+| * |   |   |   |   |
Klingons: 1, Bases: 0, Stars: 6
QuadrantMap test success
*/