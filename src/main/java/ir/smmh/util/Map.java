package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanGetAtPlace;
import ir.smmh.nile.verbs.CanSetAtPlace;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Map<K> extends CanContain<K> {

    @NotNull Iterable<K> overKeys();

    interface Mutable<K, V> extends Map<K>, CanSetAtPlace<K, V>, ir.smmh.util.Mutable {

    }

    interface SingleValue<K, V> extends Map<K>, CanGetAtPlace<K, V> {

        @Override
        default boolean contains(K key) {
            return getAtPlace(key) != null;
        }

        @NotNull Iterable<V> overValues();

        interface Mutable<K, V> extends Map.Mutable<K, V>, SingleValue<K, V> {
        }
    }

    interface MultiValue<K, V> extends Map<K>, CanGetAtPlace<K, Sequential<V>> {

        @NotNull Sequential<V> getAtPlace(K key);

        default int count(K key) {
            return getAtPlace(key).getSize();
        }

        interface Mutable<K, V> extends Map.Mutable<K, V>, MultiValue<K, V> {

        }
    }
}
