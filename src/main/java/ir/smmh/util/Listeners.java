package ir.smmh.util;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Inject your objects with one or more {@code Listeners} so that others can
 * subscribe to listen (or unsubscribe from listening) to different aspects
 * of their functionality.
 *
 * @param <L> Type of listeners
 */
@ParametersAreNonnullByDefault
public interface Listeners<L> extends Iterable<L> {
    void add(L listener);

    void addDisposable(L listener);

    void remove(L listener);

    void clear();

    int getSize();
}
