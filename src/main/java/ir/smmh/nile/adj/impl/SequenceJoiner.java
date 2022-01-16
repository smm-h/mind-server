package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

// TODO TEST
public class SequenceJoiner<T> extends Sequential.AbstractMutableSequential<T> implements Sequential.Mutable.VariableSize<T>, Mutable.Injected {

    private final Sequential.Mutable<Sequential<? extends T>> subSequences = new SequentialImpl<>(new LinkedList<>());
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

    @Override
    public T getAtIndex(int index) throws IndexOutOfBoundsException {
        for (Sequential<? extends T> s : subSequences) {
            int n = s.getSize();
            if (index < n) {
                return s.getAtIndex(index);
            } else {
                index -= n;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getSize() {
        int n = 0;
        for (Sequential<? extends T> s : subSequences) {
            n += s.getSize();
        }
        return n;
    }

    @Override
    public void setAtIndex(int index, @Nullable T toSet) {
        // TODO pre and post mutate
    }

    @Override
    public void append(T toAppend) {

    }

    @Override
    public void removeIndexFrom(int toRemove) throws IndexOutOfBoundsException {

    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public Sequential<T> clone(boolean deepIfPossible) {
        return null;
    }
}
