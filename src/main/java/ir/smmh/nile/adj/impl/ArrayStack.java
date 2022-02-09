package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Order;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fixed-size contiguous stack of non-null elements
 *
 * @param <T> type of data
 */
public class ArrayStack<T> implements Order<T>, Mutable.WithListeners.Injected {

    private final Mutable.WithListeners injectedMutable = MutableImpl.blank();
    private final T[] array;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        this.array = (T[]) new Object[capacity];
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public synchronized @Nullable T poll() {
        if (size > 0) {
            preMutate();
            T data = array[--size];
            postMutate();
            return data;
        } else {
            return null;
        }
    }

    @Override
    public @Nullable T peek() {
        if (size > 0) {
            return array[size - 1];
        } else {
            return null;
        }
    }

    @Override
    public boolean canEnter() {
        return size < array.length;
    }

    @Override
    public synchronized <S extends T> void enter(@NotNull S toEnter) {
        if (canEnter()) {
            preMutate();
            array[size++] = toEnter;
            postMutate();
        }
    }

    @Override
    public void clear() {
        preMutate();
        size = 0;
        postMutate();
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }
}
