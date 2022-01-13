package ir.smmh.nile.verbs;

import java.util.function.Predicate;

public interface CanContain<T> {
    Predicate<CanContain<?>> EMPTY = CanContain::isEmpty;

    boolean contains(T toCheck);

    boolean isEmpty();
}
