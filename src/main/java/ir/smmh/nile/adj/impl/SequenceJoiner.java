package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class SequenceJoiner<T> implements Sequential.Mutable<T>, Mutable.Injected {

    private final Sequential.Mutable<Sequential<? extends T>> subSequences = new SequentialList<>(new LinkedList<>());
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

    @Override
    public T getAt(int index) throws IndexOutOfBoundsException {
        for (Sequential<? extends T> s : subSequences) {
            int n = s.getLength();
            if (index < n) {
                return s.getAt(index);
            } else {
                index -= n;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getLength() {
        int n = 0;
        for (Sequential<? extends T> s : subSequences) {
            n += s.getLength();
        }
        return n;
    }

    @Override
    public void set(int index, @Nullable T toSet) {

    }

    @Override
    public void append(T toAppend) {

    }

    @Override
    public void removeIndexFrom(int toRemove) {

    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }
}
