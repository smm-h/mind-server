package ir.smmh.util.impl;

import ir.smmh.util.Lookup;
import ir.smmh.util.Named;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MultiLookupImpl<T extends Named> implements Lookup.Multi.Iterable<T> {

    protected Map<String, Set<T>> map;

    @Override
    public @NotNull Set<T> find(@NotNull String name) {
        if (map.containsKey(name)) {
            Set<T> set = map.get(name);
            if (set != null) {
                return Collections.unmodifiableSet(set);
            }
        }
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int count(@NotNull String name) {
        if (map.containsKey(name)) {
            Set<T> set = map.get(name);
            if (set != null) {
                return set.size();
            }
        }
        return 0;
    }

    public static class Mutable<T extends Named> extends MultiLookupImpl<T> implements Lookup.Multi.Mutable<T> {

        @Override
        public void add(@NotNull T element) {
            final String name = element.getName();
            if (!map.containsKey(name)) {
                map.put(name, new HashSet<>());
            }
            Objects.requireNonNull(map.get(name)).add(element);
        }
    }
}
