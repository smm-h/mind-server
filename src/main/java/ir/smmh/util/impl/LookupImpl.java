package ir.smmh.util.impl;

import ir.smmh.util.Lookup;
import ir.smmh.util.Named;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

public class LookupImpl<T extends Named> implements Lookup.Iterable<T> {

    protected Map<String, T> map;

    @Override
    public @Nullable T find(@NotNull String name) {
        return map.get(name);
    }

    @Override
    public boolean contains(@NotNull String name) {
        return map.containsKey(name);
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }

    public static class Mutable<T extends Named> extends LookupImpl<T> implements Lookup.Mutable<T>, ir.smmh.util.Mutable.Injected {

        public Mutable(@Nullable java.lang.Iterable<T> iterable) {
            addAll(iterable);
        }

        @Override
        public boolean add(@NotNull T element) {
            final String name = element.getName();
            if (map.containsKey(name)) {
                return false;
            }
            map.put(name, element);
            return true;
        }

        private final ir.smmh.util.Mutable injectedMutable = new MutableImpl();

        @Override
        public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
            return injectedMutable;
        }
    }
}
