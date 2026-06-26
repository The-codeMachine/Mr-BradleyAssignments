#pragma once

#include <quadrant.hpp>

#include <string>
#include <ostream>
#include <cstdint>

/**
 * QuadrantMap handles all of the movement and positional
 * status for Klingons, bases, stars, and the Enterprise
 * within a Quadrant. It allows you to remove a klingon,
 * and move the Enterprise. Operations include:
 *  - Construction (raw kbs, klingons bases stars, or a Quadrant)
 *  - Move the Enterprise
 *  - Remove a klingon
 *  - Check what the value of a sector is
 *  - Check if a sector is empty
 *  - Get the number of klingons/bases/stars in the Quadrant
 *  - Convert the map to a string
 *
 * Currently, there are 8 rows and 8 columns, with each
 * symbol being 3 big.
 *
 */
class QuadrantMap
{
public:
   QuadrantMap(Quadrant &q);

   void moveEnterprise(int x, int y, int newX, int newY);
   void removeKlingon(int x, int y);

   std::string at(int x, int y) const;
   bool empty(int x, int y) const;

   int klingons() const;
   int bases() const;
   int stars() const;

   std::string toString() const;

   friend std::ostream &operator<<(std::ostream &os, const QuadrantMap &m);

private:
   static int getIndexFrom(int x, int y);
   static void generateRandomPosition(int &x, int &y);
   static bool validPos(int x, int y);

   void clear(int x, int y);
   void insert(int x, int y, std::string value);
   void insertValues(int amount, const std::string &value);

private:
   std::string quadrantString;
   Quadrant &quadrant;

   static inline constexpr size_t ROWS = 8;
   static inline constexpr size_t COLS = 8;
   static inline constexpr size_t SYMBOL_SIZE = 3;

   static inline constexpr const char* KLINGON = "+K+";
   static inline constexpr const char* BASE = ">!<";
   static inline constexpr const char* STAR = " * ";
   static inline constexpr const char* ENTERPRISE = "<*>";
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