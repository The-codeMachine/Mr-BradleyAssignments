#include <QuadrantMap.hpp>
#include <QuadrantString.hpp>

#include <common/random.hpp>

#include <iostream>
#include <cassert>

static void generateRandomPosition(int &x, int &y)
{
   x = common::randomInt(1, 8);
   y = common::randomInt(1, 8);
}

int main()
{
   std::cout << "QuadrantString test" << std::endl;
   QuadrantString qs;

   std::cout << "Checking initial state" << std::endl;
   for (int i = 0; i < 64; i++)
   {
      assert(qs.isEmpty(i) && ("Location " + std::to_string(i) + " should initially be empty").c_str());
      assert(qs.at(i) == QuadrantMap::EMPTY && ("Location " + std::to_string(i) + " should contain EMPTY").c_str());
   }

   std::cout << "Testing place()" << std::endl;
   qs.place(0, QuadrantMap::ENTERPRISE);
   qs.place(10, QuadrantMap::KLINGON);
   qs.place(20, QuadrantMap::BASE);
   qs.place(63, QuadrantMap::STAR);

   assert(qs.at(0) == QuadrantMap::ENTERPRISE);
   assert(qs.at(10) == QuadrantMap::KLINGON);
   assert(qs.at(20) == QuadrantMap::BASE);
   assert(qs.at(63) == QuadrantMap::STAR);

   std::cout << "Testing isEmpty()" << std::endl;
   assert(!qs.isEmpty(0));
   assert(!qs.isEmpty(10));
   assert(!qs.isEmpty(20));
   assert(!qs.isEmpty(63));
   
   assert(qs.isEmpty(1));
   assert(qs.isEmpty(15));
   assert(qs.isEmpty(40));

   std::cout << "Testing overwrite\n";
   qs.place(10, QuadrantMap::STAR);
   assert((qs.at(10) == QuadrantMap::STAR) && "place() should overwrite an existing symbol");

   std::cout << "Testing clear()\n";
   qs.clear(10);
   assert(qs.isEmpty(10));
   assert(qs.at(10) == QuadrantMap::EMPTY);

   std::cout << "Testing edge positions\n";
   qs.place(0, QuadrantMap::BASE);
   qs.place(63, QuadrantMap::KLINGON);
   assert(qs.at(0) == QuadrantMap::BASE);
   assert(qs.at(63) == QuadrantMap::KLINGON);

   std::cout << "\nRaw Quadrant String:\n";
   std::cout << qs.toString() << std::endl;

   std::cout << "\nQuadrantString test success\n";

   std::cout << "QuadrantMap test\n";

   Quadrant q;
   std::cout << "Klingons: " << q.klingons() << ", Bases: " << q.bases() << ", Stars: " << q.stars() << "\n";

   int x, y;
   generateRandomPosition(x, y);
   std::cout << "Enterprise location: (" << x << ", " << y << ")\n";

   QuadrantMap m(q, x, y);

   assert(m.at(x, y) == QuadrantMap::ENTERPRISE);

   std::cout << m << "\n";

   int klingons = 0;
   int bases = 0;
   int stars = 0;
   for (int i = 1; i <= 8; ++i)
   {
      for (int j = 1; j <= 8; ++j)
      {
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

   m.place(7, 8, QuadrantMap::STAR);

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

QuadrantString test
Checking initial state
Testing place()
Testing isEmpty()
Testing overwrite
Testing clear()
Testing edge positions

Raw Quadrant String:
>!<                                                         >!<                                                                                                                              +K+

QuadrantString test success
QuadrantMap test
Klingons: 0, Bases: 0, Stars: 8
Enterprise location: (1, 1)
--------------------------------
<*>|   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   | * |   |   |
--------------------------------
   | * |   |   | * | * |   |   |
--------------------------------
   |   |   |   | * |   |   |   |
--------------------------------
   |   |   |   |   |   | * |   |
--------------------------------
   | * |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   | * |

(5, 3): <  *  >
Is (5, 3) empty: 0

(7, 8): <  *  >
Is (7, 8) empty: 0

(7, 8): <     >
Is (7, 8) empty: 1

QuadrantMap test success
*/