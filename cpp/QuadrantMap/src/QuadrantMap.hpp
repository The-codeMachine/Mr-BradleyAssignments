#pragma once

#include <QuadrantString.hpp>
#include <quadrant.hpp>
#include <Klingon.hpp>
#include <common/GameLib.hpp>

#include <string>
#include <ostream>
#include <vector>
#include <cstdint>

/**
 * 
 * QuadrantMap encapsulates all the positional values of objects
 * within a Quadrant. This includes:
 *  - Klingons
 *  - Bases
 *  - Stars
 *  - The Enterprise
 * 
 * A QuadrantMap can be constructed from a quadrant and/or the Enterprise's
 * initial position. 
 * 
 * A QuadrantMap owns the Klingons within its quadrant as well. Not just
 * their positional value, but the actual Klingon object. These can be
 * access through the getKlingons function. 
 * 
 * QuadrantMap can also check whether the Enterprise can dock or not. This
 * checks if the Enterprise is beside a base within the Quadrant. 
 * 
 * All functions which include:
 *  - Placing a new value
 *  - Clearing a sector
 *  - Moving an object from (x, y) to (newX, newY)
 *  - Removing an object from (x, y)
 *  - Getting the string representation of an object at (x, y)
 *  - Checking whether (x, y) is empty
 * 
 * take either base-1 coordinates or base-0 through Location. We recommend 
 * using the Location functions, but both are possible. QuadrantMap
 * takes (column, row) notation. 
 * 
 */
class QuadrantMap {
public:
   QuadrantMap(Quadrant q, int x, int y);
   QuadrantMap(Quadrant q);
   QuadrantMap();

   void place(int x, int y, const std::string& value);
   void place(common::Location loc, const std::string& value);

   void clearSector(int x, int y);
   void clearSector(common::Location loc);
   
   void move(int x, int y, int newX, int newY, const std::string& value);
   void move(common::Location oldLocation, common::Location newLocation, const std::string& value);
   
   void removeObject(int x, int y, const std::string& object);
   void removeObject(common::Location loc, const std::string& object);

   std::vector<Klingon>& getKlingons();
   const std::vector<Klingon>& getKlingons() const;

   int klingonsFire();
   void klingonsMove();

   common::Location& base();
   const common::Location& base() const; 

   bool canDock() const noexcept;

   std::string at(int x, int y) const;
   std::string at(common::Location loc) const;

   bool empty(int x, int y) const;
   bool empty(common::Location loc) const;

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
   void placeKlingons(int amount);
   void placeBase(int amount);
   void initializeQuadrant(Quadrant q, int x, int y);
   void initializeQuadrant(Quadrant q);

private:
   QuadrantString quadrantString;

   std::vector<Klingon> klingons;
   common::Location baseLocation;
   common::Location enterprise;
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