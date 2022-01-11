package ir.smmh;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * This is a helper class that allows you
 * to export an Intellij IDEA project to Android Studio.
 */
@SuppressWarnings("ALL")
public interface Backward {

    static String repeat(String string, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(string);
        }
        return builder.toString();
    }

    static boolean isBlank(final String s) {
        return s.isBlank();
        //        throw new RuntimeException("TODO");
    }

    static String strip(final String input) {
        return input.strip();
        //        throw new RuntimeException("TODO");
    }

    static <T> Iterator<T> asIterator(final Enumeration<T> entries) {
        return entries.asIterator();
        //        throw new RuntimeException("TODO");
    }
}
