package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Indexible;
import ir.smmh.util.Mutable;
import org.jetbrains.annotations.Nullable;

public interface CanSetAtIndex<T> extends CanContain<T>, Indexible, Mutable {

    static <T> void setAtIndex(CanSetAtIndex<T> canSetAtIndex, int index, T toSet) {
        canSetAtIndex.setAtIndex(index, toSet);
    }

    /**
     * @param index Index
     * @param toSet The object to assign to that index
     * @throws IndexOutOfBoundsException If index is invalid
     */
    void setAtIndex(int index, @Nullable T toSet);
}
