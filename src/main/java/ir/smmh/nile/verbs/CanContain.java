package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Multitude;

public interface CanContain<T> extends Multitude {
    boolean contains(T toCheck);
}
