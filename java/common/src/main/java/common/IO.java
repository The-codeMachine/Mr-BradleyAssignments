package common;

import java.util.Scanner;

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
public class IO {
    
    /**
     * 
     * Prints a message to the console. This message
     * will not be formatted. 
     * 
     * @param msg
     */
    public static void print(String msg) {
        System.out.print(msg);
    }

    /**
     * 
     * Prints a message as a line to the console. This will
     * produce the message and a new line. 
     * 
     * @param msg
     */
    public static void println(String msg) {
        System.out.println(msg);
    }

    /**
     * 
     * Formats, and prints a message to the console. 
     * 
     * @param msg
     * @param args
     */
    public static void printf(String msg, Object... args) {
        System.out.printf(msg, args);
    }

    /**
     * 
     * Prompts the user with a message. The scanner will
     * then read the user's response to the message as a String. 
     * 
     * @param msg
     * @return the user's response to the message as a String.
     */
    public static String prompt(String msg) {
        print(msg);
        return SCANNER.nextLine();
    }

    /**
     * 
     * Reads the next line from the user.
     * 
     * @return the next line from the user.
     */
    public static String readLine() {
        return SCANNER.nextLine();
    }

    /**
     * 
     * Reads the next integer from the user.
     * 
     * @return the next integer from the user.
     */
    public static int readInt() {
        return SCANNER.nextInt();
    }
    
    /**
     * 
     * Reads the next double from the user.
     * 
     * @return the next double from the user. 
     */
    public static double readDouble() {
        return SCANNER.nextDouble();
    }

    /**
     * 
     * Logs a trace message.
     * 
     * @param msg
     */
    public static void trace(String msg) {
        TRACE_LOGGER.log(LogLevel.Trace, msg);
    }

    /**
     * 
     * Logs a warning message.
     * 
     * @param msg
     */
    public static void warning(String msg) {
        WARNING_LOGGER.log(LogLevel.Warning, msg);
    }
    
    /**
     * 
     * Logs an error message.
     * 
     * @param msg
     */
    public static void error(String msg) {
        ERROR_LOGGER.log(LogLevel.Error, msg);
    }
    
    /**
     * 
     * Logs an exception.
     * 
     * @param e
     */
    public static void exception(Exception e) {
        ERROR_LOGGER.exception(e);
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    private static Logger TRACE_LOGGER = new Logger(LogLevel.Trace, "logs/trace.log");
    private static Logger WARNING_LOGGER = new Logger(LogLevel.Warning, "logs/warning.log");
    private static Logger ERROR_LOGGER = new Logger(LogLevel.Error, "logs/error.log");
}
