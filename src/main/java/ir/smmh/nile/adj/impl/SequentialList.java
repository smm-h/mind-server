package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SequentialList<T> extends Sequential.AbstractSequential<T> implements Sequential.Mutable<T>, ir.smmh.util.Mutable.Injected {

    private final List<T> list;
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

    public SequentialList(List<T> list) {
        this.list = list;
    }

    @Override
    public void removeIndexFrom(int toRemove) {
        list.remove(toRemove);
    }

    @Override
    public void append(T toAppend) {
        list.add(toAppend);
    }

    @Override
    public void add(T toAdd) {
        Mutable.super.add(toAdd);
    }

    @Override
    public void set(int index, T toSet) {
        list.set(index, toSet);
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
