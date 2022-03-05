package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Multitude;

public interface CanAddTo<T> extends Multitude.VariableSize {

    static <T> void addTo(CanAddTo<T> canAddTo, T toAdd) {
        canAddTo.add(toAdd);
    }

    void add(T toAdd);

    default void addAll(Iterable<T> toAdd) {
        for (T i : toAdd)
            add(i);
    }
}
