package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Order;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedQueue<T> implements Order<T>, Mutable.WithListeners.Injected {

    private final Mutable.WithListeners injectedMutable = MutableImpl.blank();
    private final java.util.Queue<T> q = new ConcurrentLinkedQueue<>();

    @Override
    public int getSize() {
        return q.size();
    }

    @Override
    public @Nullable T poll() {
        if (q.isEmpty()) {
            return null;
        } else {
            preMutate();
            T data = q.poll();
            postMutate();
            return data;
        }
    }

    @Override
    public @Nullable T peek() {
        return q.peek();
    }

    @Override
    public boolean canEnter() {
        return true;
    }

    @Override
    public <S extends T> void enter(@NotNull S toEnter) {
        preMutate();
        q.add(toEnter);
        postMutate();
    }

    @Override
    public void clear() {
        if (!q.isEmpty()) {
            preMutate();
            q.clear();
            postMutate();
        }
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }
}
