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
    public static String readString() {
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

    private static final Scanner SCANNER = new Scanner(System.in);
}
