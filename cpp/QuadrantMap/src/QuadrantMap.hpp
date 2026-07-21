#pragma once

#include <QuadrantString.hpp>
#include <quadrant.hpp>

#include <string>
#include <ostream>
#include <cstdint>

/*
   TODO:
   Assertions are currently used to document preconditions during
   development, and handle exceptions. Error handling with exceptions
   return values, etc. will be visited later as the design evolves.  
*/

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
 * symbol being 3 characters long.
 * 
 * All methods use 1-based coordinates because they represent
 * the quadrant from the player's perspective. Players naturally
 * think of the first sector as (1,1), rather than (0,0).
 *
 * Conversion between the two coordinate systems occurs only at the
 * getIndexFrom. 
 *
 */
class QuadrantMap
{
public:
   QuadrantMap(Quadrant q, int x, int y);

   void place(int x, int y, const std::string& value);
   void clearSector(int x, int y);
   
   void move(int x, int y, int newX, int newY, const std::string& value);
   void removeObject(int x, int y, const std::string& object);

   std::string at(int x, int y) const;
   bool empty(int x, int y) const;

   std::string toString() const;

   friend std::ostream &operator<<(std::ostream &os, const QuadrantMap &m);

public:
   /*
      Design Note:
      The object symbols are currently represented as String constants.
      An enum may provide better type safety and group the symbols
      into a single abstraction.  
   */
   static inline constexpr const char* KLINGON = "+K+";
   static inline constexpr const char* BASE = ">!<";
   static inline constexpr const char* STAR = " * ";
   static inline constexpr const char* ENTERPRISE = "<*>";
   static inline constexpr const char* EMPTY = "   ";

private:
   static int getIndexFrom(int x, int y);
   static void generateRandomPosition(int &x, int &y);
   static bool validPos(int x, int y);

   void placeValues(int amount, const std::string &value);
   void initializeQuadrant(Quadrant q, int x, int y);

private:
   QuadrantString quadrantString;

   static inline constexpr size_t ROWS = 8;
   static inline constexpr size_t COLS = 8;
   static inline constexpr size_t SYMBOL_SIZE = 3;
};

/*
Sample Output

QuadrantMap test
Klingons: 0, Bases: 0, Stars: 2
Enterprise location: (3, 5)
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   | * |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |<*>|   |   |   |   | * |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |

(5, 3): <     >
Is (5, 3) empty: 1

(7, 8): <  *  >
Is (7, 8) empty: 0

(7, 8): <     >
Is (7, 8) empty: 1

QuadrantMap test success
*/