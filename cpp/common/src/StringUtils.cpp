#include "../include/common/StringUtils.hpp"

#include <iostream>
#include <cmath>
#include <cassert>

namespace common {

    // pads the message to the left (e.g. "something" becomes "   something")
    std::string padLeft(const std::string& message) {
        return "\t" + message;
    }

    // pads the message to the right (e.g. "something" becomes "something   ")
    std::string padRight(const std::string& message) {
        return message + "\t";
    }

    // pads the message to the center (e.g. "something" becomes "   something   ")
    std::string padCenter(const std::string& message) {
        return "\t" + message + "\t";
    }

    // fills a string with zeros (e.g. 12, (size 3) becomes (012))
    std::string zeroFill(std::string in, size_t size) {
        if (in.length() >= size)
            return in;

        return std::string(size - in.length(), '0') + in;
    }

    void stringUtilsTestDriver() {
        std::cout << "String utils test \n";

        std::string msgLeftPadded = padLeft("something cool");
        assert(msgLeftPadded == "\tsomething cool");
        std::cout << "Message padded left: " << msgLeftPadded << "\n";

        std::string msgRightPadded = padRight("something cool");
        assert(msgRightPadded == "something cool\t");
        std::cout << "Message padded right: " << msgRightPadded << "\n";

        std::string msgCenterPadded = padCenter("something cool");
        assert(msgCenterPadded == "\tsomething cool\t");
        std::cout << "Message padded center: " << msgCenterPadded << "\n";

        std::string zeroFillStr = zeroFill("189", 4);
        std::cout << "Zero filled (str): " << zeroFillStr << "\n";
        assert(zeroFillStr == "0189");

        std::cout << "String utils test success\n";
    }

} // namespace common
