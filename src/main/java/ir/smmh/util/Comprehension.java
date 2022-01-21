package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@FunctionalInterface
public interface Comprehension<Input, Output> {
    Output forEach(Input input);

    @FunctionalInterface
    interface List<Input, Output> extends Comprehension<Input, Output> {
        default @NotNull java.util.List<Output> comprehend(@Nullable Iterable<? extends Input> iterable) {
            java.util.List<Output> list = new ArrayList<>();
            if (iterable != null)
                for (Input input : iterable)
                    list.add(forEach(input));
            return list;
        }
    }

    @FunctionalInterface
    interface Set<Input, Output> extends Comprehension<Input, Output> {
        default @NotNull java.util.Set<Output> comprehend(@Nullable Iterable<? extends Input> iterable) {
            java.util.Set<Output> set = new HashSet<>();
            if (iterable != null)
                for (Input input : iterable)
                    set.add(forEach(input));
            return set;
        }
    }

    @FunctionalInterface
    interface Map<Input, K, V> extends Comprehension<Input, java.util.Map.Entry<K, V>> {
        default @NotNull java.util.Map<K, V> comprehend(@Nullable Iterable<? extends Input> iterable) {
            java.util.Map<K, V> map = new HashMap<>();
            if (iterable != null) {
                for (Input input : iterable) {
                    java.util.Map.Entry<K, V> entry = forEach(input);
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            return map;
        }
    }
}
