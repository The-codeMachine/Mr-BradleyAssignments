#include "Logger.hpp"

#include <string>
#include <cstdio>

/**
 *
 * This is the input/output library. It allows
 * you to prompt the user for input, recieve the
 * input, and output lines. Current list of
 * operations include:
 *  - Print a String to the console
 *  - Print a line to the console
 *  - Prompt the user for input
 *  - Read a String/int/double from the user
 *  - Set a log level
 *  - Log a trace/warning/error
 *  - Trace the stack (prints the stack to the exception)
 *
 */
namespace common
{

    namespace IO
    {

        void print(const std::string &message);
        void println(const std::string &message);
        template <typename T, typename... Args>
        void printf(const std::string& message, Args... args) {
            ::printf(message, args);
        }

        std::string prompt(const std::string &message);

        std::string readString();
        int readInt();
        double readDouble();

        void trace(const std::string& message);
        void warning(const std::string& message);
        void error(const std::string& message);

        static common::Logger TRACE_LOGGER(LogLevel::Trace, "logs/trace.log");
        static common::Logger WARNING_LOGGER(LogLevel::Warning, "logs/warning.log");
        static common::Logger ERROR_LOGGER(LogLevel::Error, "logs/error.log");

    } // namespace IO

} // namepsace common