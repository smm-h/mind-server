package ir.smmh.net.server;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.Iterator;

public interface Pagination<T> extends Iterable<T> {

    void discardNext();

    void discardNext(final int count);

    T getNext();

    JSONArray getNext(final int count);

    boolean isDepleted();

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !isDepleted();
            }

            @Override
            public T next() {
                return getNext();
            }
        };
    }
}
