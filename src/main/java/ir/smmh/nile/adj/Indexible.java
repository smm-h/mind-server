package ir.smmh.nile.adj;

public interface Indexible extends Multitude {
    boolean hasIndex(int index);

    default void validateIndex(int index) throws IndexOutOfBoundsException {
        if (!hasIndex(index)) throw new IndexOutOfBoundsException();
    }
}
