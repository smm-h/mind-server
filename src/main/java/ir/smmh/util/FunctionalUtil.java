package ir.smmh.util;

import java.util.Iterator;

public interface FunctionalUtil {
    static <T> T itself(T self) {
        return self;
    }

    static <T> Iterable<T> over(Iterator<T> iterator) {
        return () -> iterator;
    }

    static <T> Iterable<T> over(T[] array) {
        return () -> ArrayUtil.makeArrayIterator(array);
    }
}
