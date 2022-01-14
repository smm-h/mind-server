package ir.smmh.util;

import ir.smmh.nile.verbs.CanClear;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Inject your objects with one or more {@code Listeners} so that others can
 * subscribe to listen (or unsubscribe from listening) to different aspects
 * of their functionality.
 *
 * @param <L> Type of listeners
 */
@ParametersAreNonnullByDefault
public interface Listeners<L> extends CanClear, Iterable<L> {
    void add(L listener);
    void addDisposable(L listener);

    void remove(L listener);
}
