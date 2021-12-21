package ir.smmh.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface Comprehension<I, O> {
    O forEach(I i);

    interface List<I, O> extends Comprehension<I, O> {
        default java.util.List<O> comprehend(@Nullable Iterable<I> iterable) {
            final java.util.List<O> list = new ArrayList<>();
            if (iterable != null)
                for (final I i : iterable)
                    list.add(forEach(i));
            return list;
        }
    }

    interface Set<I, O> extends Comprehension<I, O> {
        default java.util.Set<O> comprehend(@Nullable Iterable<I> iterable) {
            final java.util.Set<O> set = new HashSet<>();
            if (iterable != null)
                for (final I i : iterable)
                    set.add(forEach(i));
            return set;
        }
    }

    interface Map<I, K, V> extends Comprehension<I, java.util.Map.Entry<K, V>> {
        default java.util.Map<K, V> comprehend(@Nullable Iterable<I> iterable) {
            final java.util.Map<K, V> map = new HashMap<>();
            if (iterable != null) {
                for (final I i : iterable) {
                    final java.util.Map.Entry<K, V> entry = forEach(i);
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            return map;
        }
    }
}
