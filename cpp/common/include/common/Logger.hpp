#include <string>
#include <vector>
#include <stdexcept>

namespace common
{

    enum class LogLevel
    {
        Trace,
        Warning,
        Error
    };

    /*
    TODO:
    Add file logging to the logger class.
    */

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
     * Currently, the logger only logs to the console.
     * There is no file logging yet.
     *
     */
    class Logger
    {
    public:
        Logger(LogLevel level);

        LogLevel getLogLevel() const;
        void setLogLevel(LogLevel level);

        void log(LogLevel level, const std::string &message) const;

        void exception(const std::exception &e) const;

    private:
        void logMessage(const std::string &message) const;
        static std::vector<std::string> traceStack(const std::exception &e);

    private:
        LogLevel level;
    };

} // namepsace common