package ir.smmh.util.impl;

import ir.smmh.util.Listeners;
import ir.smmh.util.ListenersImpl;
import ir.smmh.util.Mutable;
import ir.smmh.util.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViewImpl<T> implements View<T> {

    private final Listeners<OnExpireListener> onExpireListeners = new ListenersImpl<>();
    private T core;
    private boolean expired;

    public ViewImpl(T core) {
        this.core = core;
        if (core instanceof Mutable) {
            ((Mutable) core).getOnPostMutateListeners().add(this::expire);
        }
    }

    @Nullable
    @Override
    public T getCore() {
        return core;
    }

    @Override
    public void nullifyCore() {
        core = null;
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

    @Override
    public String toString() {
        return (expired ? "Expired v" : "V") + "iew on: " + core.getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return expired ? -1 : core.hashCode();
    }
}
