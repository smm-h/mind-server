package ir.smmh.util;

import java.util.Iterator;

public interface ArrayUtil {

    /**
     * Searches through the entire array to find an element that equals
     * the given value and then returns its index.
     *
     * @param array The array to search
     * @param value The value to search for
     * @param <T>   The type of the array
     * @return The index of the value, or -1 if it is not found
     */
    static <T> int getIndexOf(T[] array, T value) {
        return getIndexOf(array, value, 0, array.length);
    }

    /**
     * Searches through the array from from  (inclusive) to to (exclusive)
     * to find an element that equals the given value and then returns its
     * index.
     *
     * @param array The array to search
     * @param value The value to search for
     * @param from  The starting index to start the search from
     * @param to    The ending index to stop the search before
     * @param <T>   The type of the array
     * @return The index of the value, or -1 if it is not found
     */
    static <T> int getIndexOf(T[] array, T value, int from, int to) {
        for (int i = from; i < to; i++)
            if (array[i].equals(value))
                return i;
        return -1;
    }

}
