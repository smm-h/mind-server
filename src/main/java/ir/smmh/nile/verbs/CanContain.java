package ir.smmh.nile.verbs;

import ir.smmh.util.jile.Quality;

public interface CanContain<T> {
    Quality<CanContain<?>> EMPTY = CanContain::isEmpty;

    boolean contains(T toCheck);

    boolean isEmpty();
}
