package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Map<K, V> extends CanContainPlace<K>, CanContain<V> {

    @NotNull Iterable<K> overKeys();

    interface Mutable<K, V> extends Map<K, V>, CanSetAtPlace<K, V>, CanRemoveAtPlace<K>, CanClear {
        void removeAllPlaces();

        @Override
        default void clear() {
            removeAllPlaces();
        }
    }

    interface SingleValue<K, V> extends Map<K, V>, CanGetAtPlace<K, V>, CanClone<SingleValue<K, V>> {

        @Override
        default boolean containsPlace(K key) {
            return getAtPlace(key) != null;
        }

        @NotNull Iterable<V> overValues();

        interface Mutable<K, V> extends Map.Mutable<K, V>, SingleValue<K, V> {
            @Override
            SingleValue.Mutable<K, V> clone(boolean deepIfPossible);

            default void setAllFrom(Map.SingleValue<K, V> map) {
                for (K key : map.overKeys())
                    setAtPlace(key, map.getAtPlace(key));
            }

            default void setAllFrom(java.util.Map<K, V> map) {
                for (K key : map.keySet())
                    setAtPlace(key, map.get(key));
            }
        }
    }

    interface MultiValue<K, V> extends Map<K, V>, CanGetAtPlace<K, Sequential<V>>, CanClone<MultiValue<K, V>> {

        @Override
        @NotNull Sequential<V> getAtPlace(K key);

        @Override
        default boolean containsPlace(K key) {
            return count(key) > 0;
        }

        default int count(K key) {
            return getAtPlace(key).getSize();
        }

        @Nullable K containingKey(V toCheck);

        interface Mutable<K, V> extends Map.Mutable<K, V>, MultiValue<K, V> {
            @Override
            MultiValue.Mutable<K, V> clone(boolean deepIfPossible);

            void removeAtPlace(K place, V toRemove);

            void removeAllAtPlace(K place);

            default void clearAtPlace(K place) {
                removeAllAtPlace(place);
            }

            default void addAtPlace(K place, V toAdd) {
                setAtPlace(place, toAdd);
            }

            default void addAllAtPlace(K place, Iterable<V> toSet) {
                for (V value : toSet)
                    addAtPlace(place, value);
            }

            default void addAllFrom(Map.MultiValue<K, V> map) {
                for (K key : map.overKeys())
                    addAllAtPlace(key, map.getAtPlace(key));
            }
        }
    }
}
