package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Indexible;

import java.util.function.IntFunction;

public interface CanGetAtIndex<T> extends Indexible, IntFunction<T> {

    static <T> T getAtIndex(CanGetAtIndex<T> canGetAtIndex, int index) throws IndexOutOfBoundsException {
        return canGetAtIndex.getAtIndex(index);
    }

    @Override
    default T apply(int index) {
        return getAtIndex(index);
    }

    T getAtIndex(int index) throws IndexOutOfBoundsException;

    default T getAtIndexWrapAround(int index) {
        return getAtIndex(index % getSize());
    }

    default T getAtLastIndex() {
        return getAtIndexWrapAround(-1);
    }

    default T getSingleton() {
        assertSingleton();
        return getAtIndex(0);
    }
}
