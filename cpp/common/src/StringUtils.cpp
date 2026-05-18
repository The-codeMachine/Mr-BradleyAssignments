#include "../include/common/StringUtils.hpp"

#include <iostream>
#include <cmath>
#include <cassert>

namespace common {

    // pads the message to the left (e.g. "something" becomes "   something", based off the width)
    std::string padLeft(const std::string& message, int width) {
        assert(width > message.length());
        
        std::string s(width - message.length(), ' ');
        return s + message;
    }

    // pads the message to the right (e.g. "something" becomes "something   ", based off the width)
    std::string padRight(const std::string& message, int width) {
        assert(width > message.length());

        std::string s(width - message.length(), ' ');
        return message + s;
    }

    // pads the message to the center (e.g. "something" becomes "   something   ", based off the width)
    std::string padCenter(const std::string& message, int width) {
        assert(width > message.length());
        
        std::string s((width - message.length()) / 2, ' ');
        return s + message + s;
    }

    // fills a string with zeros (e.g. 12, (size 3) becomes (012))
    std::string zeroFill(int input, size_t size) {
        std::string in = std::to_string(input);
        
        if (in.length() >= size)
            return in;

        return std::string(size - in.length(), '0') + in;
    }

    // tests that the string utils works correctly
    void stringUtilsTestDriver() {
        std::cout << "String utils test \n";

        std::string msgLeftPadded = padLeft("something cool", 18);
        assert(msgLeftPadded == "    something cool");
        std::cout << "Message padded left: >" << msgLeftPadded << "<\n";

        std::string msgRightPadded = padRight("something cool", 20);
        assert(msgRightPadded == "something cool      ");
        std::cout << "Message padded right: >" << msgRightPadded << "<\n";

        std::string msgCenterPadded = padCenter("something cool", 21);
        assert(msgCenterPadded == "   something cool   ");
        std::cout << "Message padded center: >" << msgCenterPadded << "<\n";

        std::string zeroFillStr = zeroFill(123, 5);
        std::cout << "Zero filled (str): " << zeroFillStr << "\n";
        assert(zeroFillStr == "00123");

        std::cout << "String utils test success\n";
    }

} // namespace common
