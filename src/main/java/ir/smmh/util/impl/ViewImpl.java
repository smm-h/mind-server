package ir.smmh.util.impl;

import ir.smmh.nile.verbs.CanClone;
import ir.smmh.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViewImpl<T> implements View<T> {

    private final Listeners<FunctionalUtil.OnEventListener> onExpireListeners = new ListenersImpl<>();
    private T core;
    private boolean expired;

    public ViewImpl(T core) {
        this.core = core;
        if (core instanceof Mutable) {
            ((Mutable) core).getOnPreMutateListeners().add(this::onPreMutate);
        }
    }

    @SuppressWarnings("unchecked")
    private void onPreMutate() {
        if (core instanceof Mutable) {
            ((Mutable) core).getOnPreMutateListeners().remove(this::onPreMutate);
            try {
                if (core instanceof CanClone) {
                    core = ((CanClone<T>) core).clone(false);
                    return;
                }
            } catch (Throwable ignored) {
            }
            expire();
        }
    }

    @Nullable
    @Override
    public T getCore() {
        return core;
    }

    @Override
    public void nullifyCore() {
        this.core = null;
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
    public @NotNull Listeners<FunctionalUtil.OnEventListener> getOnExpireListeners() {
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
