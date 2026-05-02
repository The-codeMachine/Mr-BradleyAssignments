#include "../src/Galaxy.hpp"
#include <quadrant.hpp>
#include <iostream>

void testIndex() {
    std::cout << "Index test\n";

    Galaxy g;
    
    Quadrant q = g.getQuadrant(1, 3);

    std::cout << q << "\n";
    g.printMap();

    std::cout << "Index test success\n";
}

int main() {
    testIndex();
    
    #ifndef NDEBUG

    Galaxy::whiteBoxTest();

    #endif

    return 0;
}

/*
Sample output

Index test
107 <- index will  vary
008 008 002 006 007 007 006 208 <- maps may also vary
004 001 105 107 001 108 101 002 
008 001 208 002 005 012 002 005 
001 003 003 001 005 107 003 005 
005 102 102 002 103 001 115 004 
006 103 004 305 104 002 002 201 
008 007 012 003 006 103 006 003 
006 004 005 006 007 004 002 101 

Index test success
white box test
106 004 006 006 205 005 006 001 
104 007 104 106 007 101 005 201 
105 204 004 006 102 208 004 005 
005 003 104 006 002 204 004 007 
003 102 003 005 007 006 101 004 
007 002 008 003 005 006 014 004 
001 004 012 104 206 006 005 003 
003 007 005 107 013 003 004 002 



106 004 006 006 205 005 006 001 
104 007 104 106 007 101 005 201 
105 204 004 006 102 208 004 005 
005 003 104 006 002 204 004 007 
003 102 003 005 007 006 101 004 
007 002 008 003 005 006 014 004 
001 004 012 104 206 006 005 003 
003 007 005 107 013 003 004 002 
Percent of 1 klingons: 18.75 <- percents will vary
Percent of 2 klingons: 9.375
Percent of 3 klingons: 0
Percent of bases: 1.5625
White box test success

*/