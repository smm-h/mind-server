package ir.smmh.nile.adj;

import ir.smmh.nile.verbs.CanContainPlace;

public interface Indexible extends CanContainPlace<Integer> {

    default boolean hasIndex(int index) {
        return index >= 0 && index < getSize();
    }

    /**
     * @param index Index
     * @throws IndexOutOfBoundsException If index is invalid
     */
    default void validateIndex(int index) {
        if (!hasIndex(index)) throw new IndexOutOfBoundsException();
    }

    @Override
    default boolean containsPlace(Integer toCheck) {
        return toCheck != null && hasIndex(toCheck);
    }
}
