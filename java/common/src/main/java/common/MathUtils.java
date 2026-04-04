package common;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class MathUtils {

    /**
     * constrains value to be within [low->high]
     * using the technique of selecting the highest low value, and the lowest high value
     * 
     * @param value
     * @param low
     * @param high
     * @return a value low <= v <= high
     */
    public static int clamp(int value, int low, int high) {
        return min(max(value, low), high);
    }
}
