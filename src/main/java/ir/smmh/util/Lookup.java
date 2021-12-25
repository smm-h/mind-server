package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@FunctionalInterface
public interface Lookup<T extends Named> {
    @Nullable
    T find(@NotNull String name);

    default boolean contains(@NotNull String name) {
        return find(name) != null;
    }

    interface Iterable<T extends Named> extends Lookup<T>, java.lang.Iterable<String> {
    }

    interface Mutable<T extends Named> extends Iterable<T>, ir.smmh.util.Mutable {
        boolean add(@NotNull T element);

        default void addAll(@Nullable java.lang.Iterable<T> iterable) {
            if (iterable != null)
                for (T element : iterable)
                    add(element);
        }
    }

    @FunctionalInterface
    interface Multi<T extends Named> {

        @NotNull Set<T> find(@NotNull String name);

        default int count(@NotNull String name) {
            return find(name).size();
        }

        interface Iterable<T extends Named> extends Multi<T>, java.lang.Iterable<String> {
        }

        interface Mutable<T extends Named> extends Iterable<T> {
            void add(@NotNull T element);

            default void addAll(@Nullable java.lang.Iterable<T> iterable) {
                if (iterable != null)
                    for (T element : iterable)
                        add(element);
            }
        }
    }
}
