package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Multitude;

import java.util.function.Predicate;

public interface CanContain<T> extends Multitude {
    Predicate<CanContain<?>> EMPTY = CanContain::isEmpty;

    boolean contains(T toCheck);

    boolean isEmpty();
}
