package ir.smmh.util.impl;

import ir.smmh.util.Listeners;
import ir.smmh.util.ListenersImpl;
import ir.smmh.util.Mutable;
import ir.smmh.util.View;
import org.jetbrains.annotations.NotNull;

public class ViewImpl<T> implements View<T> {

    private final Listeners<OnExpireListener> onExpireListeners = new ListenersImpl<>();
    private boolean expired;
    protected final T core;

    public ViewImpl(T core) {
        this.core = core;
        if (core instanceof Mutable) {
            ((Mutable) core).getOnTaintListeners().add(this::expire);
        }
    }

    @NotNull
    @Override
    public T getCore() {
        return core;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public void setExpired() {
        this.expired = true;
    }

    @Override
    public @NotNull Listeners<OnExpireListener> getOnExpireListeners() {
        return onExpireListeners;
    }
}
