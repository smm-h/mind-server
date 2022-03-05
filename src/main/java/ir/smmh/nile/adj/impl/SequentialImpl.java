package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SequentialImpl<T> extends Sequential.AbstractMutableSequential<T> implements Sequential.Mutable.VariableSize<T> {

    @Override
    public Sequential.Mutable.VariableSize<T> clone(boolean deepIfPossible) {
        return new SequentialImpl<>(asList());
    }

    private final List<T> list;

    public SequentialImpl(int initialCapacity) {
        super();
        list = new ArrayList<>(initialCapacity);
    }

    public SequentialImpl() {
        this(10);
    }

    public SequentialImpl(Collection<? extends T> collection) {
        this(collection.size());
        list.addAll(collection);
    }

    public SequentialImpl(Iterable<? extends T> iterable, int initialCapacity) {
        this(initialCapacity);
        for (T element : iterable) {
            list.add(element);
        }
    }

    public SequentialImpl(Iterable<? extends T> iterable) {
        this(iterable, 20);
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

    @Override
    public void clear() {
        preMutate();
        list.clear();
        postMutate();
    }

    @Override
    public void prepend(T toPrepend) {
        preMutate();
        list.add(0, toPrepend);
        postMutate();
    }
}
