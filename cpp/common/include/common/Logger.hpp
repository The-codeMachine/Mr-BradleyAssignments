#pragma once

#include <string>
#include <vector>
#include <stdexcept>
#include <filesystem>
#include <fstream>

namespace common
{

    enum class LogLevel
    {
        Trace,
        Warning,
        Error
    };

    /**
     *
     * The logger class logs messages to the console
     * and to a file. This class allows you to change
     * its log level, and log to certain levels. It
     * uses an enum to represent its levels, this
     * consists of:
     *  - Trace
     *  - Warning
     *  - Error
     *
     * The logger also has to ability to print stack
     * traces. It is used within the IO library, and
     * should only really be access through the IO
     * library.
     *
     * Logger flushes every 20 writes, and when the
     * file is closed, or this object is destroyed. 
     *
     */
    class Logger
    {
    public:
        Logger(LogLevel level, const std::filesystem::path& path);
        ~Logger();

        LogLevel getLogLevel() const;
        void setLogLevel(LogLevel level);
        
        void open();
        void close();
        void flush();

        bool isOpen() const;
        
        void log(LogLevel level, const std::string &message);
        void exception(const std::exception &e);

        static void testLogger();

    private:
        void logMessage(const std::string &message);

    private:
        LogLevel level;

        std::filesystem::path path;
        std::ofstream logFile;

        size_t pendingWrites = 0;

    };

} // namepsace common

/*
Sample Output

Logger test
This is a test message from the logger
This is an error coded log test message
This message should appear
This message should also appear
This message should appear within both the log file and console
Logger test success

*/