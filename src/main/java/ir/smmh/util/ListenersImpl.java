package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class ListenersImpl<L> implements Listeners<L> {

    private final List<L> listeners = new ArrayList<>(8);
    private final Collection<L> disposables = new LinkedHashSet<>(8);

    private ListenersImpl() {
        super();
    }

    public static <L> Listeners<L> blank() {
        return new ListenersImpl<>();
    }

    @Override
    public int getSize() {
        return listeners.size();
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    @Override
    public void add(L listener) {
        listeners.add(listener);
    }

    @Override
    public void addDisposable(L listener) {
        add(listener);
        disposables.add(listener);
    }

    @Override
    public void remove(L listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings("ReturnOfInnerClass")
    @Override
    public @NotNull Iterator<L> iterator() {
        return new Iterator<>() {
            private int index;

            @Override
            public boolean hasNext() {
                return index < listeners.size();
            }

            @Override
            public L next() {
                L listener = listeners.get(index);
                if (disposables.contains(listener)) {
                    listeners.remove(index);
                } else {
                    index++;
                }
                return listener;
            }
        };
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "Listeners: {", "}");
        for (L listener : listeners) {
            joiner.add(Integer.toString(listener.hashCode()));
        }
        return joiner.toString();
    }
}
