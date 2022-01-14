package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

/**
 * A {@code View} on any object is a light-weight read-only layer on it
 * that allows configurable access to its data. For example a view on a
 * collection may offer a partial and/or out-of-order access to its elements.
 * The general contract with views is that they never write to the core
 * object and they only ever read from it. If the core object is mutable,
 * the view is {@link #expire}d when it mutates. View expiration is
 * irreversible.
 */
@ParametersAreNonnullByDefault
public interface View<T> {

    Predicate<View<?>> EXPIRED = View::isExpired;

    @Nullable T getCore();

    void nullifyCore();

    boolean isExpired();

    default void expire() {
        setExpired();
        T core = getCore();
        if (core instanceof Mutable) {
            ((Mutable) core).getOnTaintListeners().remove(this::expire);
        }
        for (OnExpireListener listener : getOnExpireListeners()) {
            listener.onExpire();
        }
        nullifyCore();
    }

    /**
     * A mere setter with no side-effects (sets to true)
     */
    void setExpired();

    @NotNull Listeners<OnExpireListener> getOnExpireListeners();

    interface OnExpireListener {
        void onExpire();
    }

    interface Injected<T> extends View<T> {

        @NotNull View<T> getInjectedView();

        @Override
        default @Nullable T getCore() {
            return getInjectedView().getCore();
        }

        @Override
        default boolean isExpired() {
            return getInjectedView().isExpired();
        }

        @Override
        default void setExpired() {
            getInjectedView().setExpired();
        }

        @Override
        default void nullifyCore() {
            getInjectedView().nullifyCore();
        }

        @Override
        default @NotNull Listeners<OnExpireListener> getOnExpireListeners() {
            return getInjectedView().getOnExpireListeners();
        }
    }
}
