package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SequentialImpl<T> extends Sequential.AbstractMutableSequential<T> implements Sequential.Mutable.VariableSize<T> {

    private final List<T> list = new ArrayList<>();

    public SequentialImpl() {
        super();
    }

    public SequentialImpl(Collection<? extends T> collection) {
        super();
        list.addAll(collection);
    }

    public SequentialImpl(Iterable<? extends T> iterable) {
        super();
        for (T element : iterable) {
            list.add(element);
        }
    }

    @Override
    public final void removeIndexFrom(int toRemove) throws IndexOutOfBoundsException {
        preMutate();
        list.remove(toRemove);
        postMutate();
    }

    @Override
    public final void append(T toAppend) {
        preMutate();
        list.add(toAppend);
        postMutate();
    }

    @Override
    public final void setAtIndex(int index, T toSet) {
        preMutate();
        list.set(index, toSet);
        postMutate();
    }

    @Override
    public final T getAtIndex(int index) throws IndexOutOfBoundsException {
        return list.get(index);
    }

    @Override
    public final int getSize() {
        return list.size();
    }
}
