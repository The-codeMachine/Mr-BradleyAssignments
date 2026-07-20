#include "../include/common/Logger.hpp"

#include <sstream>
#include <cassert>
#include <iostream>

namespace common
{
    Logger::Logger(LogLevel level, const std::filesystem::path& path) :
        level(level), path(path) {
            open();
        }

    Logger::~Logger() {
        close();
    }

    // Logs the message to the console (will add file logging later).
    void Logger::logMessage(const std::string &message)
    {
        if (!isOpen())
            open();

        std::cout << message << "\n";
        logFile << message << "\n";
        
        pendingWrites++;
        if (pendingWrites >= 20) {
            flush();
        }
    }

    // Gets the log level for this logger
    LogLevel Logger::getLogLevel() const
    {
        return level;
    }

    // Sets the log level of this logger.
    void Logger::setLogLevel(LogLevel level)
    {
        this->level = level;
    }

    // Opens the log file. 
    void Logger::open() {
        if (isOpen())
            return;

        std::filesystem::create_directories(path.parent_path());
        logFile.open(path, std::ios::app);

        if (!logFile) {
            throw std::runtime_error("Failed to open log file: " + path.string());
        }
    }

    // Closes the log file and does all writes. 
    void Logger::close() {
        if (!isOpen())
            return;

        flush();
        logFile.close();
    }

    // Flushes all writes to the log file. 
    void Logger::flush() {
        if (isOpen())
            logFile.flush();

        pendingWrites = 0;
    }

    // Checks if the log file is open. 
    bool Logger::isOpen() const {
        return logFile.is_open();
    }

    // Logs a message to the console (no file logging yet).
    // Checks that the log level supports logging that type.
    void Logger::log(LogLevel level, const std::string &message)
    {
        if (static_cast<uint8_t>(level) < static_cast<uint8_t>(this->level))
        {
            // (e.g. warning logs does not log trace logs)
            return;
        }

        logMessage(message);
    }

    // Prints the log message to the console. This log message
    // is based of the exception error (no full stack trace,
    // requires C++23).
    void Logger::exception(const std::exception &e)
    {
        logMessage(e.what());
    }

    // Tests the logger ensuring that it works as expected.
    void Logger::testLogger()
    {
        std::cout << "Logger test\n";

        std::string path = "D:/Developer/Mr-BradleyAssignments/cpp/test_logs/logger_test.log";

        try
        {
            std::filesystem::remove(path);
        }
        catch (const std::exception &e)
        {
            std::cout << "An error occurred: " << e.what() << "\n";
        }

        {
            Logger logger(LogLevel::Trace, path);

            logger.log(LogLevel::Trace, "This is a test message from the logger");
            logger.log(LogLevel::Error, "This is an error coded log test message");

            logger.setLogLevel(LogLevel::Warning);

            logger.log(LogLevel::Trace, "This message should not appear");
            logger.log(LogLevel::Warning, "This message should appear");
            logger.log(LogLevel::Error, "This message should also appear");

            logger.setLogLevel(LogLevel::Error);

            logger.log(LogLevel::Trace, "This message should not appear");
            logger.log(LogLevel::Warning, "This message should appear either");
            logger.log(LogLevel::Error, "This message should appear within both the log file and console");
        }

        try
        {
            std::ifstream file(path);
            std::stringstream buffer;
            buffer << file.rdbuf();

            assert(buffer.str() ==
                   "This is a test message from the logger\n"
                   "This is an error coded log test message\n"
                   "This message should appear\n"
                   "This message should also appear\n"
                   "This message should appear within both the log file and console\n");
        }
        catch (const std::exception &e)
        {
            std::cout << "An error occurred: " << e.what() << "\n";
        }

        std::cout << "Logger test success\n";
    }

} // namespace common