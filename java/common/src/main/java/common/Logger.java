package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
 */
public class Logger {

    Logger(LogLevel level, Path path) {
        this.level = level;
        logFilePath = path;
    }

    Logger(LogLevel level, String path) {
        this(level, Path.of(path));
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

        stackTrace.forEach(this::logMessage);
    }

    /**
     * 
     * Tests the logger ensuring that it works as
     * expected. 
     * 
     */
    public static void testLogger() {
        System.out.println("Logger test");

        Logger logger = new Logger(LogLevel.Trace, 
            "D:/Developer/Mr-BradleyAssignments/java/test_logs/logger_test.log");

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
            String content = Files.readString(
                Path.of("D:/Developer/Mr-BradleyAssignments/java/test_logs/logger_test.log"));

            assert content.equals("""
            This is a test message from the logger\n
            This is an error coded log test message\n
            This message should appear\n
            This message should also appear\n
            This message should appear within both the log file and console\n
            """) : "Log file is not equal to the expected output";

            Files.deleteIfExists(Path.of("D:/Developer/Mr-BradleyAssignments/java/test_logs/logger_test.log"));

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
     * @param msg
     */
    private void logMessage(String msg) {
        System.out.println(msg);

        try {
            Files.write(logFilePath, msg.getBytes());
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

    private LogLevel level;
    private Path logFilePath;
}
