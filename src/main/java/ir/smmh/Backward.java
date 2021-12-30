package ir.smmh;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * This is a helper class that allows older versions of Java to
 * compile this codebase. This is particularly useful when trying
 * to export an Intellij IDEA project to Android Studio.
 */
public class Backward {
    private Backward() {
    }

    public static String repeat(String string, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(string);
        }
        return builder.toString();
    }

    public static boolean isBlank(final String s) {
        return s.isBlank();
//        throw new RuntimeException("TODO");
    }

    public static String strip(final String input) {
        return input.strip();
//        throw new RuntimeException("TODO");
    }

    public static <T> Iterator<T> asIterator(final Enumeration<T> entries) {
        return entries.asIterator();
//        throw new RuntimeException("TODO");
    }
}
