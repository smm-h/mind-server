package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import static ir.smmh.util.FunctionalUtil.with;

@ParametersAreNonnullByDefault
public abstract class MapImpl<K, V> implements Map<K, V> {

    @Override
    public final String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Lookup: {", "}");
        for (K key : overKeys()) {
            joiner.add(key.toString());
        }
        return joiner.toString();
    }

    public static class SingleValue<K, V> extends MapImpl<K, V> implements Map.SingleValue<K, V> {

        protected final java.util.Map<K, V> map = new HashMap<>();

        @Override
        public final int getSize() {
            return map.size();
        }

        @Override
        public final boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public final @NotNull Iterable<K> overKeys() {
            return map.keySet();
        }

        @Override
        public final @Nullable V getAtPlace(K key) {
            return map.get(key);
        }

        @Override
        public final boolean containsPlace(K key) {
            return map.containsKey(key);
        }

        @Override
        public final boolean contains(V value) {
            return map.containsValue(value);
        }

        @Override
        public final @NotNull Iterable<V> overValues() {
            return map.values();
        }

        public static class Mutable<K, V> extends MapImpl.SingleValue<K, V> implements Map.SingleValue.Mutable<K, V>, ir.smmh.util.Mutable.Injected {

            private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();

            @Override
            public final void setAtPlace(K place, V toSet) {
                map.put(place, toSet);
            }

            @Override
            public final @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
                return injectedMutable;
            }

            @Override
            public void removeAtPlace(K toRemove) {
                map.remove(toRemove);
            }
        }
    }

    public static class MultiValue<K, V> extends MapImpl<K, V> implements Map.MultiValue<K, V> {
        protected final java.util.Map<K, Sequential.Mutable.VariableSize<V>> map = new HashMap<>();

        @Override
        public boolean contains(V toCheck) {
            for (K key : map.keySet()) {
                if (map.get(key).contains(toCheck)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public final boolean containsPlace(K toCheck) {
            return map.containsKey(toCheck);
        }

        @Override
        public final boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public final @NotNull Iterable<K> overKeys() {
            return map.keySet();
        }

        @Override
        public final @NotNull Sequential<V> getAtPlace(K key) {
            return with(map.get(key), Sequential.empty());
        }

        @Override
        public final int getSize() {
            return map.size();
        }

        public static class Mutable<K, V> extends MapImpl.MultiValue<K, V> implements Map.MultiValue.Mutable<K, V>, ir.smmh.util.Mutable.Injected {
            private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();

            @Override
            public final void setAtPlace(K place, V toSet) {
                Sequential.Mutable.VariableSize<V> s = map.computeIfAbsent(place, k -> new SequentialImpl<>(new ArrayList<>()));
                s.append(toSet);
            }

            @Override
            public final @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
                return injectedMutable;
            }

            @Override
            public void removeAtPlace(K toRemove) {
                map.remove(toRemove);
            }
        }
    }
}
