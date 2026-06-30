#include <QuadrantMap.hpp>

#include <iostream>

int main()
{
   std::cout << "QuadrantMap test\n";

   Quadrant q;
   QuadrantMap m(q, 4, 3);

   std::cout << m << "\n";

   std::cout << "(5, 3): < " << m.at(5, 3) << " >\n";
   std::cout << "Is (5, 3) empty: " << m.empty(5, 3) << "\n\n";

   m.insert(7, 8, QuadrantMap::STAR);

   std::cout << "(7, 8): < " << m.at(7, 8) << " >\n";
   std::cout << "Is (7, 8) empty: " << m.empty(7, 8) << "\n\n";

   m.clearSector(7, 8);

   std::cout << "(7, 8): < " << m.at(7, 8) << " >\n";
   std::cout << "Is (7, 8) empty: " << m.empty(7, 8) << "\n\n";

   int x;
   int y;

   std::cout << "Enter (x, y) to remove an object: ";
   std::cin >> x >> y;

   m.clearSector(x, y);

   std::cout << m << "\n";

   std::cout << "QuadrantMap test success\n";

   return 0;
}

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