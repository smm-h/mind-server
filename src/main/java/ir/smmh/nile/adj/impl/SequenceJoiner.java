package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;

import java.util.ArrayList;

public class SequenceJoiner<T> extends Sequential.AbstractSequential<T> implements Sequential<T> {

    private final Sequential.Mutable.VariableSize<Sequential<? extends T>> subSequences = new SequentialImpl<>(new ArrayList<>());

    public void join(Sequential<? extends T> sequential) {
        subSequences.append(sequential);
    }

    public void startOver() {
        subSequences.clear();
    }

    @Override
    public final T getAtIndex(int index) {
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
}
