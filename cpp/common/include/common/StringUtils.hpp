#pragma once
#include <string>

/**
 * 
 * This holds string utilities. Operations include:
 *  - padding a message to the left
 *  - padding a message to the right
 *  - padding a message to the center
 * 
 *  - zero filling an integer (e.g. 12 (with size 3) becomes 012)
 *  - a test driver to test that all the functions are working
 * 
 */

namespace common {

    std::string padLeft(const std::string& message, int width);
    std::string padRight(const std::string& message, int width);
    std::string padCenter(const std::string& message, int width);

    std::string zeroFill(std::string in, size_t size);

    void stringUtilsTestDriver();

} // namespace common

/*

Sample Output

String utils test 
Message padded left:     something cool
Message padded right: something cool      
Message padded center:    something cool   
Zero filled (str): 0189
String utils test success

*/