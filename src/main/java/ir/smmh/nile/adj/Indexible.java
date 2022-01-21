package ir.smmh.nile.adj;

import ir.smmh.nile.verbs.CanContainPlace;

public interface Indexible extends CanContainPlace<Integer> {
    boolean hasIndex(int index);

    default void validateIndex(int index) throws IndexOutOfBoundsException {
        if (!hasIndex(index)) throw new IndexOutOfBoundsException();
    }

    @Override
    default boolean containsPlace(Integer toCheck) {
        return toCheck != null && hasIndex(toCheck);
    }
}
