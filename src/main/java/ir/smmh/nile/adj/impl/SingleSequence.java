package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

public class SingleSequence<T> extends Sequential.AbstractMutableSequential<T> {

    private T singleton;

    public SingleSequence(T singleton) {
        super();
        this.singleton = singleton;
    }

    @Override
    public final void setAtIndex(int index, T toSet) {
        validateIndex(index);
        preMutate();
        singleton = toSet;
        postMutate();
    }

    @Override
    public final T getAtIndex(int index) throws IndexOutOfBoundsException {
        validateIndex(index);
        return singleton;
    }

    @Override
    public final int getSize() {
        return 1;
    }
}
