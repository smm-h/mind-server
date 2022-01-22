package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Order;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class Priority<T> implements Mutable.WithListeners.Injected, Order<T> {
    private final Mutable.WithListeners injectedMutable = MutableImpl.blank();
    private final PriorityQueue<T> queue;

    public Priority(ToIntFunction<? super T> priority) {
        this.queue = new PriorityQueue<>(Comparator.comparingInt(priority));
    }

    public Priority(ToDoubleFunction<? super T> priority) {
        this.queue = new PriorityQueue<>(Comparator.comparingDouble(priority));
    }

    @Override
    public int getSize() {
        return queue.size();
    }

    @Override
    public @Nullable T peek() {
        return queue.peek();
    }

    @Override
    public @Nullable T poll() {
        if (!isEmpty()) {
            preMutate();
            T data = queue.poll();
            postMutate();
            return data;
        } else {
            return null;
        }
    }

    @Override
    public boolean canEnter() {
        return true;
    }

    @Override
    public <S extends T> void enter(@NotNull S toEnter) {
        preMutate();
        queue.add(toEnter);
        postMutate();
    }

    @Override
    public void clear() {
        preMutate();
        queue.clear();
        postMutate();
    }

    @Override
    public @NotNull Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }
}
