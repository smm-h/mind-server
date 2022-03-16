package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.*;
import java.util.Map.Entry;

public interface Comprehend {
    // list without counter
    static @NotNull <Input, Output> List<Output> list(Iterable<? extends Input> iterable, WithoutCounter<? super Input, ? extends Output> forEach) {
        List<Output> list = new ArrayList<>();
        for (Input input : iterable) {
            list.add(forEach.forEach(input));
        }
        return list;
    }

    // list with counter
    static @NotNull <Input, Output> List<Output> list(Iterable<? extends Input> iterable, WithCounter<? super Input, ? extends Output> forEach) {
        List<Output> list = new ArrayList<>();
        int counter = 0;
        for (Input input : iterable) {
            list.add(forEach.forEach(input, counter++));
        }
        return list;
    }

    // set without counter
    static @NotNull <Input, Output> Set<Output> set(Iterable<? extends Input> iterable, WithoutCounter<? super Input, ? extends Output> forEach) {
        Set<Output> set = new HashSet<>();
        for (Input input : iterable) {
            set.add(forEach.forEach(input));
        }
        return set;
    }

    // set with counter
    static @NotNull <Input, Output> Set<Output> set(Iterable<? extends Input> iterable, WithCounter<? super Input, ? extends Output> forEach) {
        Set<Output> set = new HashSet<>();
        int counter = 0;
        for (Input input : iterable) {
            set.add(forEach.forEach(input, counter++));
        }
        return set;
    }

    // map without counter
    static @NotNull <Input, K, V> Map<K, V> map(Iterable<? extends Input> iterable, WithoutCounter<? super Input, ? extends Entry<K, V>> forEach) {
        Map<K, V> map = new HashMap<>();
        for (Input input : iterable) {
            Entry<K, V> entry = forEach.forEach(input);
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    // map with counter
    static @NotNull <Input, K, V> Map<K, V> map(Iterable<? extends Input> iterable, WithCounter<? super Input, ? extends Entry<K, V>> forEach) {
        Map<K, V> map = new HashMap<>();
        int counter = 0;
        for (Input input : iterable) {
            Entry<K, V> entry = forEach.forEach(input, counter++);
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    // map with separate functions for key (without counter), and value (without counter)
    static @NotNull <Input, K, V> Map<K, V> map(Iterable<? extends Input> iterable, WithoutCounter<? super Input, ? extends K> k, WithoutCounter<? super Input, ? extends V> v) {
        Map<K, V> map = new HashMap<>();
        for (Input input : iterable) {
            map.put(k.forEach(input), v.forEach(input));
        }
        return map;
    }

    // map with separate functions for key (with counter), and value (with counter)
    static @NotNull <Input, K, V> Map<K, V> map(Iterable<? extends Input> iterable, WithCounter<? super Input, ? extends K> k, WithCounter<? super Input, ? extends V> v) {
        Map<K, V> map = new HashMap<>();
        int counter = 0;
        for (Input input : iterable) {
            map.put(k.forEach(input, counter++), v.forEach(input, counter++));
        }
        return map;
    }

    // map with separate functions for key (without counter), and value (with counter)
    static @NotNull <Input, K, V> Map<K, V> map(Iterable<? extends Input> iterable, WithoutCounter<? super Input, ? extends K> k, WithCounter<? super Input, ? extends V> v) {
        Map<K, V> map = new HashMap<>();
        int counter = 0;
        for (Input input : iterable) {
            map.put(k.forEach(input), v.forEach(input, counter++));
        }
        return map;
    }

    // map with separate functions for key (with counter), and value (without counter)
    static @NotNull <Input, K, V> Map<K, V> map(Iterable<? extends Input> iterable, WithCounter<? super Input, ? extends K> k, WithoutCounter<? super Input, ? extends V> v) {
        Map<K, V> map = new HashMap<>();
        int counter = 0;
        for (Input input : iterable) {
            map.put(k.forEach(input, counter++), v.forEach(input));
        }
        return map;
    }

    @FunctionalInterface
    interface WithoutCounter<Input, Output> {
        Output forEach(Input input);
    }

    @FunctionalInterface
    interface WithCounter<Input, Output> {
        Output forEach(Input input, int counter);
    }
}
