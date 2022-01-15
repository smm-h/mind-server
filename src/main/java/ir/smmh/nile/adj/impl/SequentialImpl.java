package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SequentialImpl<T> extends Sequential.AbstractSequential<T> implements Sequential.Mutable<T>, ir.smmh.util.Mutable.Injected {

    private final List<T> list = new LinkedList<>();
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

    public SequentialImpl() {
    }

    public SequentialImpl(Collection<T> collection) {
        list.addAll(collection);
    }

    @Override
    public void removeIndexFrom(int toRemove) {
        preMutate();
        list.remove(toRemove);
        postMutate();
    }

    @Override
    public void append(T toAppend) {
        preMutate();
        list.add(toAppend);
        postMutate();
    }

    @Override
    public void add(T toAdd) {
        Mutable.super.add(toAdd);
    }

    @Override
    public void set(int index, T toSet) {
        preMutate();
        list.set(index, toSet);
        postMutate();
    }

    @Override
    public T getAt(int index) throws IndexOutOfBoundsException {
        return list.get(index);
    }

    @Override
    public int getLength() {
        return list.size();
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }
}
