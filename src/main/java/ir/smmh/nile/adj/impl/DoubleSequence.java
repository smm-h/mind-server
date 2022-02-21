package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

public class DoubleSequence<T> extends Sequential.AbstractSequential<T> implements Sequential.Mutable<T>, ir.smmh.util.Mutable.Injected {

    private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();
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
    public final T getAtIndex(int index) throws IndexOutOfBoundsException {
        validateIndex(index);
        return index == 0 ? first : second;
    }

    @Override
    public final int getSize() {
        return 2;
    }

    @Override
    public final @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }
}
