#pragma once

#include <string>

namespace common {

    std::string padLeft(const std::string& message);
    std::string padRight(const std::string& message);
    std::string padCenter(const std::string& message);

    std::string zeroFill(std::string in, size_t size);

    void stringUtilsTestDriver();

} // namespace common

/*

Sample Output

String utils test 
Message padded left:    something cool
Message padded right: something cool
Message padded center:  something cool
Zero filled (str): 0189
String utils test success

*/