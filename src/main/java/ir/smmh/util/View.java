package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

/**
 * A {@code View} on any collection is a light-weight layer on it that
 * allows partial and/or out-of-order access to it. It is read-only and
 * immutable. If the core object it is viewing is mutable, it should be
 * {@link #expire}d when it mutates. View expiration is a one way process.
 */
@ParametersAreNonnullByDefault
public interface View<T> {

    Predicate<View<?>> EXPIRED = View::isExpired;

    @NotNull T getCore();

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
    }

    /**
     * A mere setter with no side-effects (sets to true)
     */
    void setExpired();

    @NotNull Listeners<OnExpireListener> getOnExpireListeners();

    interface OnExpireListener {
        void onExpire();
    }
}
