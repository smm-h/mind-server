package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringJoiner;

import static ir.smmh.util.FunctionalUtil.with;

@ParametersAreNonnullByDefault
public abstract class MapImpl<K> implements Map<K> {

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Lookup: {", "}");
        for (K key : overKeys()) {
            joiner.add(key.toString());
        }
        return joiner.toString();
    }

    public static class SingleValue<K, V> extends MapImpl<K> implements Map.SingleValue<K, V> {

        protected final java.util.Map<K, V> map = new HashMap<>();

        @Override
        public int getSize() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public @NotNull Iterable<K> overKeys() {
            return map.keySet();
        }

        @Override
        public @Nullable V getAtPlace(K key) {
            return map.get(key);
        }

        @Override
        public boolean contains(K key) {
            return map.containsKey(key);
        }

        @Override
        public @NotNull Iterable<V> overValues() {
            return map.values();
        }

        public static class Mutable<K, V> extends MapImpl.SingleValue<K, V> implements Map.SingleValue.Mutable<K, V>, ir.smmh.util.Mutable.Injected {

            private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

            @Override
            public void setAtPlace(K place, V toSet) {
                map.put(place, toSet);
            }

            @Override
            public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
                return injectedMutable;
            }
        }
    }

    public static class MultiValue<K, V> extends MapImpl<K> implements Map.MultiValue<K, V> {
        protected final java.util.Map<K, Sequential.Mutable.VariableSize<V>> map = new HashMap<>();

        @Override
        public boolean contains(K toCheck) {
            return map.containsKey(toCheck);
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public @NotNull Iterable<K> overKeys() {
            return map.keySet();
        }

        @Override
        public @NotNull Sequential<V> getAtPlace(K key) {
            return with(map.get(key), Sequential.empty());
        }

        @Override
        public int getSize() {
            return map.size();
        }

        public static class Mutable<K, V> extends MapImpl.MultiValue<K, V> implements Map.MultiValue.Mutable<K, V>, ir.smmh.util.Mutable.Injected {
            private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

            @Override
            public void setAtPlace(K place, V toSet) {
                Sequential.Mutable.VariableSize<V> s = map.computeIfAbsent(place, k -> new SequentialImpl<>(new ArrayList<>()));
                s.append(toSet);
            }

            @Override
            public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
                return injectedMutable;
            }
        }
    }
}
