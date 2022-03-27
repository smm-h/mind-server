package ir.smmh;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * This is a helper class that allows you
 * to export an Intellij IDEA project to Android Studio.
 */
public interface Backward {
    static <T> Iterator<T> asIterator(final Enumeration<T> enumeration) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }
}
