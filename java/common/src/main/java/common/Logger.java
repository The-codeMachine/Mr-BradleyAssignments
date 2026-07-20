package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum LogLevel {
    Trace,
    Warning,
    Error
}

/**
 * 
 * TODO:
 * Do I need to add timestamps, and the log level
 * to each log. This would be part of the message;
 * looking something like this: 
 * [2026-07-17 21:20:34] Trace Enterprise moved to Quadrant 31
 * 
 * I'm not sure if I would print that, but I might
 * add that to the logs. I will consult Mr. Bradley
 * on this. 
 * 
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
 * Logger flushes every 20 writes, and when the
 * file is closed, or this object is destroyed. 
 * 
 */
public class Logger {

    public Logger(LogLevel level, Path path) {
        this.level = level;
        logFilePath = path;
    }

    public Logger(LogLevel level, String path) {
        this(level, Path.of(path));
    }

    public Logger(String path) {
        this(LogLevel.Trace, Path.of(path));
    }

    public Logger(Path path) {
        this(LogLevel.Trace, path);
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
     * Logs a message to the console and to a log file.
     * Checks that the log level supports logging that type.
     * 
     * @param level
     * @param msg
     */
    public void log(LogLevel level, String msg) {
        if (level.ordinal() < this.level.ordinal()) {
            // (e.g. warning logs does not log trace logs)
            return;
        }

        logMessage(level, msg);
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
        String traceString = String.join(System.lineSeparator(), stackTrace);

        logMessage(LogLevel.Error, traceString);
    }

    /**
     * 
     * Tests the logger ensuring that it works as
     * expected. 
     * 
     */
    public static void testLogger() {
        System.out.println("Logger test");

        Path path = Path.of(
        "D:/Developer/Mr-BradleyAssignments/java/test_logs/logger_test.log");

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        Logger logger = new Logger(LogLevel.Trace, path);

        logger.log(LogLevel.Trace, "This is a test message from the logger");
        logger.log(LogLevel.Error, "This is an error coded log test message");

        logger.setLogLevel(LogLevel.Warning);

        logger.log(LogLevel.Trace, "This message should not appear");
        logger.log(LogLevel.Warning, "This message should appear");
        logger.log(LogLevel.Error, "This message should also appear");

        logger.setLogLevel(LogLevel.Error);

        logger.log(LogLevel.Trace, "This message should not appear");
        logger.log(LogLevel.Warning, "This message should not appear either");
        logger.log(LogLevel.Error, "This message should appear within both the log file and console");

        try {
            String content = Files.readString(path);

            System.out.println(content);

            Files.deleteIfExists(path);

        } catch (IOException e) {
            System.out.println("Exception occurred: " + e);
        }

        System.out.println("Logger test success");
    }

    /**
     * 
     * Logs the message to the console and to a log
     * file specified in the constructor. 
     * 
     * @param level
     * @param msg
     */
    private void logMessage(LogLevel level, String msg) {
        String logMessage = "[" + getCurrentTimeAndDate() + "] ["
             + convertLogLevelToString(level) + "] " + msg;

        System.out.println(logMessage);

        try {
            Files.writeString(logFilePath, logMessage + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * 
     * Converts a log level to a string object.
     * This function uses a switch/case block.
     * 
     * @param level
     * @return the log level as a string
     */
    private static String convertLogLevelToString(LogLevel level) {
        switch (level) {
            case LogLevel.Trace:
                return "TRACE";
            case LogLevel.Warning:
                return "WARNING";
            case LogLevel.Error:
                return "ERROR";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * 
     * Gets the current time and date of the 
     * computer (uses system clock).
     * 
     * @return the current time and date as a string
     */
    private static String getCurrentTimeAndDate() {
        LocalDate date = LocalDate.now();
        // no nanoseconds recorded
        LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        return date + " " + time;
    }

    private LogLevel level;
    private Path logFilePath;
}

/**
 * 
 * Sample Output
 * 
 * Logger test
 * This is a test message from the logger
 * This is an error coded log test message
 * This message should appear
 * This message should also appear
 * This message should appear within both the log file and console
 * Logger test success
 * 
 */