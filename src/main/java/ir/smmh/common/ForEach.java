package ir.smmh.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface ForEach<I, O> {
    O forEach(I i);

    default Set<O> toSet(Iterable<I> iterable) {
        final Set<O> set = new HashSet<>();
        for (final I i : iterable) {
            set.add(forEach(i));
        }
        return set;
    }

    interface Mappable<I, K, V> extends ForEach<I, Map.Entry<K, V>> {
        default Map<K, V> toMap(Iterable<I> iterable) {
            final Map<K, V> map = new HashMap<>();
            for (final I i : iterable) {
                final Map.Entry<K, V> entry = forEach(i);
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        }
    }
}
