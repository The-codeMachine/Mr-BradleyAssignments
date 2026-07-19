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

        std::string readString();
        int readInt();
        double readDouble();

        std::string prompt(const std::string &message);
        int promptInt(const std::string &message);
        double promptDouble(const std::string &message);
        std::string readCommand();

        void trace(const std::string& message);
        void warning(const std::string& message);
        void error(const std::string& message);

        static common::Logger LOGGER(LogLevel::Trace, "logs/game.log");

        static const std::string COMMANDS = "NAVSRSLRSPHATORSHEDAMCOMXXX";
        static constexpr int COMMAND_SIZE = 3;

    } // namespace IO

} // namepsace common