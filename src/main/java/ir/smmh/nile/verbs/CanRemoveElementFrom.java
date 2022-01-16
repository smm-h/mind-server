package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Multitude;

public interface CanRemoveElementFrom<T> extends Multitude.VariableSize {

    static <T> void removeFrom(CanRemoveElementFrom<T> canRemoveElementFrom, T toRemove) {
        canRemoveElementFrom.removeElementFrom(toRemove);
    }

    void removeElementFrom(T toRemove);
}
