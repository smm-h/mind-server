package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Indexible;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public interface CanGetAtIndex<T> extends Indexible, IntFunction<T> {

    static <T> T getAtIndex(CanGetAtIndex<T> canGetAtIndex, int index) {
        return canGetAtIndex.getAtIndex(index);
    }

    @Override
    default T apply(int index) {
        return getAtIndex(index);
    }

    /**
     * @param index Index
     * @return Object at that index
     * @throws IndexOutOfBoundsException If index is invalid
     */
    T getAtIndex(int index);

    default T getAtIndexWrapAround(int index) {
        return getAtIndex(index % getSize());
    }

    default T getAtLastIndex() {
        return getAtIndexWrapAround(-1);
    }

    default @NotNull T getSingleton() {
        assertSingleton();
        return getAtIndex(0);
    }
}
