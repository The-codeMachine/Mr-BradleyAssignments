package common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum LogLevel {
    Trace,
    Warning,
    Error
}

/*
 * TODO:
 * Add file logging to the logger class.
 */

/**
 * 
 * The logger class logs messages to the console
 * and to a file. This class allows you to change
 * its log level, and log to certain levels. It
 * uses an enum to represent its levels, this
 * consists of:
 * - Trace
 * - Warning
 * - Error
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
public class Logger {

    Logger(LogLevel level) {
        this.level = level;
    }

    /**
     * 
     * Gets the log level for this logger.
     * 
     * @return this logger's log level.
     */
    public LogLevel getLogLevel() {
        return level;
    }

    /**
     * 
     * Sets the log level of this logger.
     * 
     * @param level
     */
    public void setLogLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * 
     * Logs a message to the console (no file logging yet).
     * Checks that the log level supports logging that type.
     * 
     * @param level
     * @param msg
     */
    public void log(LogLevel level, String msg) {
        if (this.level.ordinal() < level.ordinal()) {
            // (e.g. warning logs does not log trace logs)
            return;
        }

        logMessage(msg);
    }

    /**
     * 
     * Prints the log message to the console. This
     * log message is based off an exception stack
     * trace.
     * 
     * @param e
     */
    public void exception(Exception e) {
        List<String> stackTrace = traceStack(e);

        stackTrace.forEach(System.out::println);
    }

    /**
     * 
     * Logs the message to the console (will add
     * file logging, but later).
     * 
     * @param msg
     */
    private void logMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * 
     * Constructs a list of strings representing
     * the stack trace.
     * 
     * @return a list of strings representing the stack trace.
     */
    private static List<String> traceStack(Exception e) {
        List<String> stackTraceList = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());

        return stackTraceList;
    }

    private LogLevel level;
}
