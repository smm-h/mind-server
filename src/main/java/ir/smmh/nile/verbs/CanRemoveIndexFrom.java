package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Indexible;
import ir.smmh.nile.adj.Multitude;

public interface CanRemoveIndexFrom<T> extends Indexible, Multitude.VariableSize {

    static <T> void removeIndexFrom(CanRemoveIndexFrom<T> canRemoveIndexFrom, int toRemove) {
        canRemoveIndexFrom.removeIndexFrom(toRemove);
    }

    /**
     * @param toRemove Index to remove
     * @throws IndexOutOfBoundsException If index is invalid
     */
    void removeIndexFrom(int toRemove);
}
