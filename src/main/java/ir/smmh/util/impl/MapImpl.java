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
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Map: {", "}");
        for (K key : overKeys()) {
            joiner.add(key.toString());
        }
        return joiner.toString();
    }

    public static class SingleValue<K, V> extends MapImpl<K, V> implements Map.SingleValue<K, V> {

        protected final java.util.Map<K, V> map;

        public SingleValue() {
            this(new HashMap<>());
        }

        private SingleValue(java.util.Map<K, V> map) {
            this.map = map;
        }

        @Override
        public final String toString() {
            StringJoiner joiner = new StringJoiner(", ", "Map: {", "}");
            for (K key : overKeys()) {
                joiner.add(key.toString() + " -> " + getAtPlace(key));
            }
            return joiner.toString();
        }

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

        @Override
        public Map.SingleValue<K, V> clone(boolean deepIfPossible) {
            return new MapImpl.SingleValue<>(new HashMap<>(map));
        }

        @Override
        public Map.SingleValue<K, V> specificThis() {
            return this;
        }

        public static class Mutable<K, V> extends MapImpl.SingleValue<K, V> implements Map.SingleValue.Mutable<K, V>, ir.smmh.util.Mutable.Injected {

            private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();

            public Mutable() {
                super();
            }

            private Mutable(java.util.Map<K, V> map) {
                super(map);
            }

            @Override
            public final void setAtPlace(K place, V toSet) {
                preMutate();
                map.put(place, toSet);
                postMutate();
            }

            @Override
            public final @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
                return injectedMutable;
            }

            @Override
            public void removeAtPlace(K toRemove) {
                preMutate();
                map.remove(toRemove);
                postMutate();
            }

            @Override
            public void removeAllPlaces() {
                preMutate();
                map.clear();
                postMutate();
            }

            @Override
            public Map.SingleValue.Mutable<K, V> clone(boolean deepIfPossible) {
                return new MapImpl.SingleValue.Mutable<>(new HashMap<>(map));
            }
        }
    }

    public static class MultiValue<K, V> extends MapImpl<K, V> implements Map.MultiValue<K, V> {
        protected final java.util.Map<K, Sequential.Mutable.VariableSize<V>> map;

        public MultiValue() {
            this(new HashMap<>());
        }

        private MultiValue(java.util.Map<K, Sequential.Mutable.VariableSize<V>> map) {
            this.map = map;
        }

        @Override
        public final String toString() {
            StringJoiner joiner = new StringJoiner(", ", "Map: {", "}");
            for (K key : overKeys()) {
                joiner.add(key.toString() + " -> " + getAtPlace(key));
            }
            return joiner.toString();
        }

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
        public @Nullable K containingKey(V toCheck) {
            for (K key : map.keySet()) {
                if (map.get(key).contains(toCheck)) {
                    return key;
                }
            }
            return null;
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

        @Override
        public Map.MultiValue<K, V> clone(boolean deepIfPossible) {
            return new MapImpl.MultiValue<>(new HashMap<>(map));
        }

        @Override
        public Map.MultiValue<K, V> specificThis() {
            return this;
        }

        public static class Mutable<K, V> extends MapImpl.MultiValue<K, V> implements Map.MultiValue.Mutable<K, V>, ir.smmh.util.Mutable.Injected {
            private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();

            public Mutable() {
                super();
            }

            private Mutable(java.util.Map<K, Sequential.Mutable.VariableSize<V>> map) {
                super(map);
            }

            @Override
            public final void setAtPlace(K place, V toSet) {
                preMutate();
                Sequential.Mutable.VariableSize<V> s = map.computeIfAbsent(place, k -> new SequentialImpl<>(new ArrayList<>()));
                s.append(toSet);
                postMutate();
            }

            @Override
            public final @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
                return injectedMutable;
            }

            @Override
            public void removeAtPlace(K toRemove) {
                preMutate();
                map.remove(toRemove);
                postMutate();
            }

            @Override
            public void removeAtPlace(K place, V toRemove) {
                var s = map.get(place);
                if (s != null) {
                    int i = s.findFirst(toRemove);
                    if (i != -1) {
                        preMutate();
                        s.removeIndexFrom(i);
                        postMutate();
                    }
                }
            }

            @Override
            public void removeAllAtPlace(K place) {
                var s = map.get(place);
                if (s != null) {
                    preMutate();
                    s.clear();
                    postMutate();
                }
            }

            @Override
            public void removeAllPlaces() {
                preMutate();
                map.clear();
                postMutate();
            }

            @Override
            public Map.MultiValue.Mutable<K, V> clone(boolean deepIfPossible) {
                return new MapImpl.MultiValue.Mutable<>(new HashMap<>(map));
            }
        }
    }
}
