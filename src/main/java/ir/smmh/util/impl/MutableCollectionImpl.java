package ir.smmh.util.impl;

import ir.smmh.util.Mutable;
import ir.smmh.util.MutableCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public final class MutableCollectionImpl<T> implements MutableCollection<T>, Mutable.WithListeners.Injected {
    private final Collection<T> collection;
    private final Mutable.WithListeners injectedMutable = MutableImpl.blank();

    private MutableCollectionImpl(Collection<T> collection) {
        super();
        this.collection = collection;
    }

    public static <T> MutableCollection<T> of(@NotNull Collection<T> collection) {
        return new MutableCollectionImpl<>(collection);
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public void add(T toAdd) {
        preMutate();
        if (collection.add(toAdd)) {
            postMutate();
        }
    }

    @Override
    public void removeElementFrom(T toRemove) {
        preMutate();
        if (collection.remove(toRemove)) {
            postMutate();
        }
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            preMutate();
            collection.clear();
            postMutate();
        }
    }

    @Override
    public int getSize() {
        return collection.size();
    }

    @Override
    public boolean contains(T toCheck) {
        return collection.contains(toCheck);
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }
}
