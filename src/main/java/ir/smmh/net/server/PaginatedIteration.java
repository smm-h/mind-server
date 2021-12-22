package ir.smmh.net.server;

import org.json.JSONArray;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PaginatedIteration implements Pagination<String> {
    private final Iterator<String> iterator;

    public PaginatedIteration(Iterable<String> iterable) {
        this(iterable.iterator());
    }

    public PaginatedIteration(Iterator<String> iterator) {
        this.iterator = iterator;
    }

    @Override
    public void discardNext() {
        iterator.next();
    }

    @Override
    public void discardNext(final int count) {
        for (int i = 0; i < count && iterator.hasNext(); i++) {
            iterator.next();
        }
    }

    @Override
    public String getNext() {
        try {
            return iterator.next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public JSONArray getNext(final int count) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < count && iterator.hasNext(); i++) {
            array.put(iterator.next());
        }
        return array;
    }

    @Override
    public boolean isDepleted() {
        return !iterator.hasNext();
    }
}
