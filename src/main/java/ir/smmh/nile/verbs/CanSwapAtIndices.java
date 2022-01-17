package ir.smmh.nile.verbs;

public interface CanSwapAtIndices<T> extends CanSetAtIndex<T>, CanGetAtIndex<T> {

    static <T> void swap(CanSwapAtIndices<T> canSwapAtIndices, int i, int j) {
        canSwapAtIndices.swap(i, j);
    }

    default void swap(int i, int j) {
        if (i != j) {
            T temp = getAtIndex(i);
            setAtIndex(i, getAtIndex(j));
            setAtIndex(j, temp);
        }
    }
}
