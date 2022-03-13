package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Indexible;
import ir.smmh.nile.adj.Multitude;

public interface CanInsertAtIndex<T> extends Indexible, Multitude.VariableSize {

    static <T> void insertAtIndex(CanInsertAtIndex<T> canInsertAtIndex, int index, T toInsert) {
        canInsertAtIndex.insertAtIndex(index, toInsert);
    }

    /**
     * @param index Index
     * @param toInsert The object to insert at that index
     * @throws IndexOutOfBoundsException If index is invalid
     */
    void insertAtIndex(int index, T toInsert);
}
