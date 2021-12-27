package ir.smmh.util.impl;

import ir.smmh.util.Mutable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class MutableHashSet<T> extends HashSet<T> implements Mutable.Set<T>, ir.smmh.util.Mutable.Injected {
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl();

    public MutableHashSet() {
        super();
    }

    public MutableHashSet(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public boolean add(T t) {
        if (super.add(t)) {
            taint();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            taint();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        if (!isEmpty()) taint();
        super.clear();
    }
}
