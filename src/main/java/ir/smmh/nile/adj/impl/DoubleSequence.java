package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;

public class DoubleSequence<T> extends Sequential.AbstractMutableSequential<T> {

    private T first, second;

    public DoubleSequence(T first, T second) {
        super();
        this.first = first;
        this.second = second;
    }

    @Override
    public final void setAtIndex(int index, T toSet) {
        validateIndex(index);
        preMutate();
        if (index == 0) first = toSet;
        else second = toSet;
        postMutate();
    }

    @Override
    public final T getAtIndex(int index) {
        validateIndex(index);
        return index == 0 ? first : second;
    }

    @Override
    public final int getSize() {
        return 2;
    }

}
