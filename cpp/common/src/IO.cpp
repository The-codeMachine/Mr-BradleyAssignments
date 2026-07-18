#include "../include/common/IO.hpp"

#include <iostream>

namespace common {

    namespace IO {

        // Prints a message to the console. This message
        // will not be formatted. 
        void print(const std::string &message) {
            std::cout << message;
        }


        // Prints a message as a line to the console. This will
        // produce the message and a new line. 
        void println(const std::string &message) {
            std::cout << message << "\n";
        }

        // Prompts the user with a message. The scanner will
        // then read the user's response to the message as a String. 
        std::string prompt(const std::string &message) {
            std::cout << message;
            
            std::string response;
            std::cin >> response;

            return response;
        }

        // Reads the next line from the user.
        std::string readString() {
            std::string r;
            std::cin >> r;

            return r;
        }
        
        // Reads the next integer from the user.
        int readInt() {
            int r;
            std::cin >> r;

            return r;
        }
        
        // Reads the next double from the user. 
        double readDouble() {
            double r;
            std::cin >> r;

            return r;
        }

        // Logs a trace message. 
        void trace(const std::string& message) {
            TRACE_LOGGER.log(LogLevel::Trace, message);
        }

        // Logs a warning message. 
        void warning(const std::string& message) {
            WARNING_LOGGER.log(LogLevel::Warning, message);
        }

        // Logs an error message. 
        void error(const std::string& message) {
            ERROR_LOGGER.log(LogLevel::Error, message);
        }

    } // namespace IO

} // namepsace common