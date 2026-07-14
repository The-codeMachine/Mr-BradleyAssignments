package QuadrantMap;

/**
 * 
 * QuadrantString encapsulates the fixed-width 
 * string used to represent the content of a Quadrant
 * Rather than exposing raw String manipulation 
 * methods, this class treats every 3 characters as a
 * cell, and manipulates those cells. These cells can
 * store any one of these:
 *  - An Enterprise
 *  - A Klingon
 *  - A star
 *  - A star base
 *  - Nothing
 * 
 * Internal storage of this object is a 192 character 
 * long String. Externally, this represents a 64 cell
 * grid. 
 * 
 * All functions use base-0 indices. 
 * 
 */
public class QuadrantString {
    public QuadrantString() {
        quadrantString = " ".repeat(ARRAY_SIZE * CELL_SIZE);
    }

    /**
     * 
     * Gets what the cell at index is. Returns 
     * what string is held there, 
     * 
     * @param index
     * @return the string the cell represents
     */
    public String at(int index) {
        index = formatIndex(index);
        assert isValidIndex(index) : "Index is invalid";
        
        return quadrantString.substring(index, index + CELL_SIZE);
    }

    /**
     * 
     * Places the value at index, overrides
     * whatever was there already.
     * 
     * @param index
     * @param value
     */
    public void place(int index, String value) {
        index = formatIndex(index);
        assert isValidIndex(index) : "Index is invalid";
        assert value.length() == CELL_SIZE : "Value must be equal to CELL_SIZE";

        quadrantString = quadrantString.substring(0, index) // prefix
                + value // infix
                + quadrantString.substring(index + CELL_SIZE); // postfix
    }

    /**
     * 
     * Clears a cell at index (replaces it
     * with empty).
     * 
     * @param index
     */
    public void clear(int index) {
        // checks are done within place
        place(index, EMPTY);
    }

    /**
     * 
     * Checks whether the the cell contains value. 
     * 
     * @param index
     * @param value
     * @return true if the cell contains value
     */
    public boolean contains(int index, String value) {
        // checks are done within other functions
        return at(index).equals(value);
    }

    /**
     * 
     * Checks whether the cell at index is empty
     * or not. 
     * 
     * @param index
     * @return True if the cell at index is empty
     */
    public boolean isEmpty(int index) {
        // checks are done within other functions
        return contains(index, EMPTY);
    }

    /**
     * 
     * Gets the size if the 1D array. 
     * 
     * @return the size of the 1D array. 
     */
    public int size() {
        return ARRAY_SIZE;
    }

    /**
     * 
     * Gets the length of the quadrantString.
     * 
     * @return the length of the quadrantString
     */
    public int length() {
        return quadrantString.length();
    }

    @Override
    public String toString() {
        return quadrantString;
    }

    /**
     * 
     * Checks whether the inputted index is a valid
     * position. 
     * 
     * @param index
     * @return True if the index is a valid position
     */
    private boolean isValidIndex(int index) {
        return index >= 0 && index < ARRAY_SIZE * CELL_SIZE;
    }

    /**
     * 
     * Formats the user-inputted index (0-63) to 
     * a string format (e.g. 63 would become 189)
     * 
     * @param index
     * @return The index formatted
     */
    private int formatIndex(int index) {
        return index * CELL_SIZE;
    }

    private String quadrantString; 

    private static final String EMPTY = "   ";

    private static final int ARRAY_SIZE = 64;
    private static final int CELL_SIZE = 3;
}