package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SequentialImpl<T> extends Sequential.AbstractMutableSequential<T> implements Sequential.Mutable.VariableSize<T>, ir.smmh.util.Mutable.Injected {

    private final List<T> list = new ArrayList<>();

    public SequentialImpl() {
    }

    public SequentialImpl(Collection<T> collection) {
        list.addAll(collection);
    }

    public SequentialImpl(Iterable<T> iterable) {
        for (T element : iterable) {
            list.add(element);
        }
    }

    @Override
    public void removeIndexFrom(int toRemove) throws IndexOutOfBoundsException {
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
    public void setAtIndex(int index, @NotNull T toSet) {
        preMutate();
        list.set(index, toSet);
        postMutate();
    }

    @Override
    public T getAtIndex(int index) throws IndexOutOfBoundsException {
        return list.get(index);
    }

    @Override
    public int getSize() {
        return list.size();
    }
}
