#include "../include/common/Logger.hpp"

#include <iostream>

namespace common {

    Logger::Logger(LogLevel level) : level(level) {}

    // Logs the message to the console (will add file logging later).
    void Logger::logMessage(const std::string& message) const {
        std::cout << message << "\n";
    }

    // Gets the log level for this logger
    LogLevel Logger::getLogLevel() const {
        return level;
    }

    // Sets the log level of this logger. 
    void Logger::setLogLevel(LogLevel level) {
        this->level = level;
    }

    // Logs a message to the console (no file logging yet). 
    // Checks that the log level supports logging that type. 
    void Logger::log(LogLevel level, const std::string& message) const {
        if (static_cast<uint8_t>(this->level) < static_cast<uint8_t>(level)) {
            // (e.g. warning logs does not log trace logs)
            return;
        }

        logMessage(message);
    }

    // Prints the log message to the console. This log message
    // is based of the exception error (no full stack trace, 
    // requires C++23).
    void Logger::exception(const std::exception& e) const {
        logMessage(e.what());
    }

} // namespace common