package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

// TODO TEST
public class SequenceJoiner<T> extends Sequential.AbstractMutableSequential<T> implements Sequential.Mutable.VariableSize<T> {

    private final Sequential.Mutable<Sequential<? extends T>> subSequences = new SequentialImpl<>(new ArrayList<>());
    private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();

    @Override
    public final T getAtIndex(int index) throws IndexOutOfBoundsException {
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
    public final int getSize() {
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
    public final @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public final Sequential<T> clone(boolean deepIfPossible) {
        return null;
    }
}
