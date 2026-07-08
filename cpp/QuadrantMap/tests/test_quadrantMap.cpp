#include <QuadrantMap.hpp>

#include <common/random.hpp>

#include <iostream>
#include <cassert>

static void generateRandomPosition(int& x, int& y) {
   x = common::randomInt(1, 8);
   y = common::randomInt(1, 8);
}

int main()
{
   std::cout << "QuadrantMap test\n";

   Quadrant q;
   std::cout << "Klingons: " << q.klingons() << ", Bases: " << q.bases() <<
      ", Stars: " << q.stars() << "\n";

   int x, y;
   generateRandomPosition(x, y);
   std::cout << "Enterprise location: (" << x << ", " << y << ")\n";

   QuadrantMap m(q, x, y);

   assert(m.at(x, y) == QuadrantMap::ENTERPRISE);

   std::cout << m << "\n";

   int klingons = 0;
   int bases = 0;
   int stars = 0;
   for (int i = 1; i <= 8; ++i) {
      for (int j = 1; j <= 8; ++j) {
         std::string sector = m.at(i, j);

         if (sector == QuadrantMap::KLINGON)
            klingons++;
         else if (sector == QuadrantMap::BASE)
            bases++;
         else if (sector == QuadrantMap::STAR)
            stars++;
      }
   }

   assert(klingons == q.klingons());
   assert(bases == q.bases());
   assert(stars == q.stars());

   std::cout << "(5, 3): < " << m.at(5, 3) << " >\n";
   std::cout << "Is (5, 3) empty: " << m.empty(5, 3) << "\n\n";

   m.insert(7, 8, QuadrantMap::STAR);

   std::cout << "(7, 8): < " << m.at(7, 8) << " >\n";
   std::cout << "Is (7, 8) empty: " << m.empty(7, 8) << "\n\n";

   m.clearSector(7, 8);

   std::cout << "(7, 8): < " << m.at(7, 8) << " >\n";
   std::cout << "Is (7, 8) empty: " << m.empty(7, 8) << "\n\n";

   std::cout << "QuadrantMap test success\n";

   return 0;
}

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