package ir.smmh.nile.adj;

import ir.smmh.nile.verbs.CanContain;
import ir.smmh.util.Mutable;

import java.util.function.Predicate;

public interface Multitude {
    Predicate<CanContain<?>> EMPTY = Multitude::isEmpty;

    int getSize();

    default boolean isEmpty() {
        return getSize() == 0;
    }

    default void assertSingleton() {
        assert getSize() == 1;
    }

    interface VariableSize extends Multitude, Mutable {
    }
}
