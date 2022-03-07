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
 * object and they only ever read from it. If the core object is mutable, and
 * it mutates, the view is {@link #expire}d and cannot be undone.
 */
@ParametersAreNonnullByDefault
public interface View<T> {

    Predicate<View<?>> EXPIRED = View::isExpired;

    @Nullable T getCore();

    void nullifyCore();

    boolean isExpired();

    default void expire() {
        setExpired();
        for (FunctionalUtil.OnEventListener listener : getOnExpireListeners()) {
            listener.onEvent();
        }
        nullifyCore();
    }

    /**
     * A mere setter with no side-effects (sets to true)
     */
    void setExpired();

    @NotNull Listeners<FunctionalUtil.OnEventListener> getOnExpireListeners();

    @FunctionalInterface
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
        default @NotNull Listeners<FunctionalUtil.OnEventListener> getOnExpireListeners() {
            return getInjectedView().getOnExpireListeners();
        }
    }
}
