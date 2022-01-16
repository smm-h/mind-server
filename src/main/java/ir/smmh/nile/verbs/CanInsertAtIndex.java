package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Indexible;
import ir.smmh.nile.adj.Multitude;

public interface CanInsertAtIndex<T> extends Indexible, Multitude.VariableSize {

    static <T> void insertAtIndex(CanInsertAtIndex<T> canInsertAtIndex, int index, T toInsert) {
        canInsertAtIndex.insertAtIndex(index, toInsert);
    }

    void insertAtIndex(int index, T toInsert) throws IndexOutOfBoundsException;
}
