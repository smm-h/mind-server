package ir.smmh.util.impl;

import ir.smmh.util.Lookup;
import ir.smmh.util.Named;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MultiLookupImpl<T extends Named> implements Lookup.Multi.Iterable<T> {

    protected Map<String, Set<T>> map;

    @Override
    public @NotNull Set<T> find(@NotNull String name) {
        return map.containsKey(name) ? Collections.unmodifiableSet(map.get(name)) : Collections.emptySet();
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int count(@NotNull String name) {
        return map.containsKey(name) ? map.get(name).size() : 0;
    }

    public static class Mutable<T extends Named> extends MultiLookupImpl<T> implements Lookup.Multi.Mutable<T> {

        @Override
        public void add(@NotNull T element) {
            final String name = element.getName();
            if (!map.containsKey(name)) {
                map.put(name, new HashSet<>());
            }
            map.get(name).add(element);
        }
    }
}
