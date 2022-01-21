package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanContainPlace;
import ir.smmh.nile.verbs.CanGetAtPlace;
import ir.smmh.nile.verbs.CanSetAtPlace;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Map<K, V> extends CanContainPlace<K>, CanContain<V> {

    @NotNull Iterable<K> overKeys();

    interface Mutable<K, V> extends Map<K, V>, CanSetAtPlace<K, V> {

    }

    interface SingleValue<K, V> extends Map<K, V>, CanGetAtPlace<K, V> {

        @Override
        default boolean containsPlace(K key) {
            return getAtPlace(key) != null;
        }

        @NotNull Iterable<V> overValues();

        interface Mutable<K, V> extends Map.Mutable<K, V>, SingleValue<K, V> {
        }
    }

    interface MultiValue<K, V> extends Map<K, V>, CanGetAtPlace<K, Sequential<V>> {

        @Override
        @NotNull Sequential<V> getAtPlace(K key);

        @Override
        default boolean containsPlace(K key) {
            return count(key) > 0;
        }

        default int count(K key) {
            return getAtPlace(key).getSize();
        }

        interface Mutable<K, V> extends Map.Mutable<K, V>, MultiValue<K, V> {

        }
    }
}
