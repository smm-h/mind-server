package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanPlaceIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public interface Map<K> extends CanContain<K> {

    @NotNull Iterable<K> overKeys();

    interface Mutable<K, V> extends Map<K>, CanPlaceIn<K, V>, ir.smmh.util.Mutable {

    }

    interface SingleValue<K, V> extends Map<K>, Function<K, V> {
        @Nullable V get(K key);

        @Override
        default V apply(K k) {
            return get(k);
        }

        @Override
        default boolean contains(K key) {
            return get(key) != null;
        }

        @NotNull Iterable<V> overValues();

        interface Mutable<K, V> extends Map.Mutable<K, V>, SingleValue<K, V> {
        }
    }

    interface MultiValue<K, V> extends Map<K> {

        @NotNull Sequential<V> get(K key);

        default int count(K key) {
            return get(key).getLength();
        }

        interface Mutable<K, V> extends Map.Mutable<K, V>, MultiValue<K, V> {

        }
    }
}
