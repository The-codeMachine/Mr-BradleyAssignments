package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import common.GameLib.Location;;

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

        println("Valid commands: ");
        println(" - NAV (Sets a course)");
        println(" - SRS (Scans the current quadrant)");
        println(" - LRS (Scans all quadrants around you)");
        println(" - PHA (Fires your phasers");
        println(" - TOR (Fires a torpedoe)");
        println(" - SHE (Raises/lowers the shields)");
        println(" - DAM (Gets the damage report)");
        println(" - COM (Access the library's computer)");
        println(" - XXX (Quits the game)");

        return new ArrayList<>();
    }

    /**
     * 
     * Separates a string into numbers by commas. 
     * 
     * @param str
     * @return All numbers separated by commas. 
     */
    private static List<Integer> separateByCommas(String str) {
        StringBuilder cleaned = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c) || c == ',') {
                cleaned.append(c);
            }
        }
        
        List<Integer> numbers = new ArrayList<>();
        String[] tokens = cleaned.toString().split(",");
        for (String token : tokens) {
            if (!token.isEmpty()) {
                numbers.add(Integer.parseInt(token));
            }
        }
        return numbers;
    }

    /**
     * 
     * Checks whether the number is an invalid position
     * for the world (between 1 and 8).
     * 
     * @param num
     */
    private static boolean invalidPosition(int num) {
        return num > 8 || num < 1;
    }

    /**
     * 
     * Prompts the user to input a valid location within the
     * quadrant. Checks that they are valid, converts them to
     * (column, row) from (row, column) and validates their positions
     * 
     * @return a parsed location inputted by the user
     */
    public static Location promptLocation() {
        print("Input a location (row, column), (row, column): ");

        String input = prompt("");
        List<Integer> numbers = separateByCommas(input);
        if (numbers.size() != 4) {
            warning("A location must have 4 coordinates");
            return new Location(-1, -1, -1, -1);
        }

        if (invalidPosition(numbers.get(0)) || invalidPosition(numbers.get(1)) ||
            invalidPosition(numbers.get(2)) || invalidPosition(numbers.get(3))) {
            warning("Number is invalid, try again");
            return new Location(-1, -1, -1, -1);
        }

        return new Location(numbers.get(1), numbers.get(0), 
                            numbers.get(3), numbers.get(4)); 
    }

    /**
     * 
     * Prompts the user to enter a valid sector using the (row, column) convention.
     * Will return a base-0 coordinate using the Location class. 
     * 
     * @return a parsed sector inputted by the user
     */
    public static Location promptSector() {
        print("Input a sector (row, column): ");

        String input = prompt("");
        List<Integer> numbers = separateByCommas(input);
        if (numbers.size() != 2) {
            warning("A sector must have 2 coordinates");
            return new Location(-1, -1, -1, -1);
        }

        if (invalidPosition(numbers.get(0)) || invalidPosition(numbers.get(1))) {
            warning("Number is invalid, try again");
            return new Location(-1, -1, -1, -1);
        }

        return new Location(numbers.get(1), numbers.get(0), -1, -1); 
    }
    
    /**
     * 
     * Prompts the user to enter a valid quadrant using the (row, column) convention.
     * Will return a base-0 coordinate using the Location class. 
     * 
     * @return a parsed quadrant inputted by the user
     */
    public static Location promptQuadrant() {
        print("Input a quadrant (row, column): ");

        String input = prompt("");
        List<Integer> numbers = separateByCommas(input);
        if (numbers.size() != 2) {
            warning("A quadrant must have 2 coordinates");
            return new Location(-1, -1, -1, -1);
        }

        if (invalidPosition(numbers.get(0)) || invalidPosition(numbers.get(1))) {
            warning("Number is invalid, try again");
            return new Location(-1, -1, -1, -1);
        }

        return new Location(-1, -1, numbers.get(1), numbers.get(0)); 
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
