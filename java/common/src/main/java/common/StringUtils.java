package common;

/**
 * 
 * This holds string utilities. Operations include:
 * - padding a message to the left
 * - padding a message to the right
 * - padding a message to the center
 * 
 * - zero filling an integer (e.g. 12 (with size 3) becomes 012)
 * - a test driver to test that all the functions are working
 * 
 */

public class StringUtils {

    /**
     * 
     * Pads a message to the left
     * 
     * @param message
     * @param width the width of the message plus padding
     * @return a string with the message padded to the left
     */
    public static String padLeft(String message, int width) {
        assert width > message.length();

        String spaces = " ".repeat(width - message.length());

        return spaces + message;
    }

    /**
     * 
     * Pads a message to the right
     * 
     * @param message
     * @param width the width of the message plus padding
     * @return a string with the message padded to the right
     */
    public static String padRight(String message, int width) {
        assert width > message.length();

        String spaces = " ".repeat(width - message.length());

        return message + spaces;
    }

    /**
     * 
     * Pads a message to the center
     * 
     * @apiNote if the width ends with a odd number (after the message), the extra
     *          space not be accounted for
     * 
     * @param message
     * @param width the width of the message plus padding
     * @return a string with the message padded to the center
     */
    public static String padCenter(String message, int width) {
        assert width > message.length();

        String spaces = " ".repeat((width - message.length()) / 2);

        return spaces + message + spaces;
    }

    /**
     * 
     * Zero fills a message with size many zeros
     * 
     * @param in
     * @param width
     * @return the message zero filled (size many zeros)
     */
    public static String zeroFill(String in, int width) {
        if (in.length() >= width)
            return in;

        return "0".repeat(width - in.length()) + in;
    }

    public static void stringUtilsTestDriver() {
        System.out.println("String utils test");

        // 14 characters is "something cool"
        String msgLeftPadded = padLeft("something cool", 18);
        assert msgLeftPadded.equals("    something cool") : "Message was not padded correctly";
        System.out.printf("Message padded left: %s\n", msgLeftPadded);

        String msgRightPadded = padRight("something cool", 20);
        assert msgRightPadded.equals("something cool      ") : "Message was not padded correctly";
        System.out.printf("Message padded right: %s\n", msgRightPadded);

        String msgCenterPadded = padCenter("something cool", 21);
        assert msgCenterPadded.equals("   something cool   ") : "Message was not padded correctly";
        System.out.printf("Message padded center: %s\n", msgCenterPadded);

        String zeroFillStr = zeroFill("189", 4);
        System.out.printf("Zero filled (str): %s\n", zeroFillStr);
        assert zeroFillStr.equals("0189") : "Zero fill did not return a correct zero fill string";

        System.out.println("String utils test success");
    }

}

/**
 * 
 * Sample Output
 * 
 * String utils test
 * Message padded left:     something cool
 * Message padded right: something cool      
 * Message padded center:    something cool   
 * Zero filled (str): 0189
 * String utils test success
 * 
 */