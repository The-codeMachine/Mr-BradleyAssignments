package common;

import java.util.ArrayList;
import java.util.Arrays;
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
        return Integer.parseInt(readLine().trim());
    }
    
    /**
     * 
     * Reads the next double from the user.
     * 
     * @return the next double from the user. 
     */
    public static double readDouble() {
        return Double.parseDouble(readLine().trim());
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
        return readLine();
    }

    /**
     * 
     * Requests an int from the user. Prints a message and
     * then reads the next int. 
     * 
     * @param msg
     * @return the user provided int
     */
    public static int promptInt(String msg) {
        print(msg);
        return readInt();
    }

    /**
     * 
     * Requests a double from the user. Prints a message and
     * then reads the next double. 
     * 
     * @param msg
     * @return the user provided double
     */
    public static double promptDouble(String msg) {
        print(msg);
        return readDouble();
    }

    /**
     * 
     * Reads a command from the user. This method
     * verifies it is of the correct length, and 
     * is an actual valid command based off the
     * COMMANDS string. 
     * 
     * @return the command represented as a string. 
     */
    public static ArrayList<String> readCommand() {
        String line = prompt("Enter your next command: ");

        if (line.isEmpty()) {
            IO.warning("No command entered");
            return new ArrayList<>();
        }

        String[] parts = line.split("\\s+");
        parts[0] = parts[0].toUpperCase();
        String cmd = parts[0];

        for (int i = 0; i < COMMANDS.length(); i += COMMAND_SIZE) {
            if (COMMANDS.substring(i, i + COMMAND_SIZE).equals(cmd))
                return new ArrayList<>(Arrays.asList(parts));
        }

        warning("Invalid command was entered: " + cmd);
        return new ArrayList<>();
    }

    /**
     * 
     * Logs a trace message.
     * 
     * @param msg
     */
    public static void trace(String msg) {
        LOGGER.log(LogLevel.Trace, msg);
    }

    /**
     * 
     * Logs a warning message.
     * 
     * @param msg
     */
    public static void warning(String msg) {
        LOGGER.log(LogLevel.Warning, msg);
    }
    
    /**
     * 
     * Logs an error message.
     * 
     * @param msg
     */
    public static void error(String msg) {
        LOGGER.log(LogLevel.Error, msg);
    }
    
    /**
     * 
     * Logs an exception.
     * 
     * @param e
     */
    public static void exception(Exception e) {
        LOGGER.exception(e);
    }

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Logger LOGGER = new Logger(LogLevel.Trace, "logs/game.log");
    
    private static final String COMMANDS = "NAVSRSLRSPHATORSHEDAMCOMXXX";
    private static final int COMMAND_SIZE = 3;
}
