package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

public class BinarySequentialImpl<T> extends Sequential.AbstractSequential<T> implements Sequential.Mutable<T>, ir.smmh.util.Mutable.Injected {

    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);
    private T first, second;

    public BinarySequentialImpl() {
    }

    public BinarySequentialImpl(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void setAtIndex(int index, T toSet) {
        validateIndex(index);
        preMutate();
        if (index == 0) first = toSet; else second = toSet;
        postMutate();
    }

    @Override
    public T getAtIndex(int index) throws IndexOutOfBoundsException {
        validateIndex(index);
        return index == 0 ? first : second;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }
}
