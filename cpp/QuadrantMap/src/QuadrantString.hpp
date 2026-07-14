#pragma once

#include <string>

/**
 * 
 * QuadrantString encapsulates the fixed-width 
 * string used to represent the content of a Quadrant
 * Rather than exposing raw String manipulation 
 * methods, this class treats every 3 characters as a
 * cell, and manipulates those cells. These cells can
 * store any one of these:
 *  - An Enterprise
 *  - A Klingon
 *  - A star
 *  - A star base
 *  - Nothing
 * 
 * Internal storage of this object is a 192 character 
 * long String. Externally, this represents a 64 cell
 * grid. 
 * 
 * All functions use base-0 indices. 
 * 
 */
class QuadrantString
{
public:
    QuadrantString();
    
    std::string at(int index) const;
    bool contains(int index, const std::string& value) const;
    bool isEmpty(int index) const;

    void place(int index, const std::string& value);
    void clear(int index);

    int size() const;
    int length() const;

    std::string toString() const;

private:
    static bool isValidIndex(int index);
    static int formatIndex(int index); 

private:
    std::string quadrantString;

    static inline constexpr const char* EMPTY = "   ";
    static inline constexpr int ARRAY_SIZE = 64;
    static inline constexpr int CELL_SIZE = 3;
};

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

*/