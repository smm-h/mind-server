package ir.smmh.util.impl;

import ir.smmh.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.ref.WeakReference;
import java.util.Objects;

@ParametersAreNonnullByDefault
public final class ViewImpl<T> implements View<T> {

    private final Listeners<FunctionalUtil.OnEventListener> onExpireListeners = ListenersImpl.blank();
    private final @NotNull WeakReference<T> core;
    private boolean expired;

    private ViewImpl(T core) {
        super();
        this.core = new WeakReference<>(core);
        if (core instanceof Mutable.WithListeners) {
            ((Mutable.WithListeners) core).getOnPostMutateListeners().addDisposable(this::expire);
        }
    }

    public static <T> View<T> of(T core) {
        return new ViewImpl<>(core);
    }

    @Nullable
    @Override
    public T getCore() {
        return core.get();
    }

    @Override
    public void nullifyCore() {
        core.clear();
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public void setExpired() {
        expired = true;
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
        return Objects.hashCode(core.get());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof View && Objects.equals(((View) obj).getCore(), core.get());
    }
}
