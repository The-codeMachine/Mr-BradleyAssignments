#include <QuadrantMap.hpp>

#include <iostream>

int main()
{
   std::cout << "QuadrantMap test\n";

   Quadrant q;
   QuadrantMap m(q);

   std::cout << m << "\n";

   std::cout << "(4, 2): < " << m.at(4, 2) << " >\n";
   std::cout << "Is (4, 2) empty: " << m.empty(4, 2) << "\n\n";

   std::cout << "Klingons: " << m.klingons() << "\n";
   std::cout << "Bases: " << m.bases() << "\n";
   std::cout << "Stars: " << m.stars() << "\n";

   int x;
   int y;

   std::cout << "Enter (x, y) to remove a klingon: ";
   std::cin >> x >> y;

   m.removeKlingon(x, y);

   std::cout << m << "\n";

   std::cout << "QuadrantMap test success\n";

   return 0;
}

/*
Sample Output

QuadrantMap test
--------------------------------
   |   |   |   |+K+|   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   | * |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |<*>|   | * |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
Klingons: 1, Bases: 0, Stars: 2
(4, 2): <     >
Is (4, 2) empty: 1

Klingons: 1
Bases: 0
Stars: 2
Enter (x, y) for a klingon: 4 0
--------------------------------
   |   |   |   |+K+|   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   | * |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
--------------------------------
   |   |<*>|   | * |   |   |   |
--------------------------------
   |   |   |   |   |   |   |   |
Klingons: 0, Bases: 0, Stars: 2
QuadrantMap test success

*/