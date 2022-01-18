package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class ListenersImpl<L> implements Listeners<L>, Mutable.Injected {

    private final List<L> listeners = new ArrayList<>(); // new ConcurrentSkipListSet<>(Comparator.comparingInt(Object::hashCode)); // new LinkedHashSet<>(); //
    private final Set<L> disposables = new LinkedHashSet<>();

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

    @Override
    public @NotNull Iterator<L> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < listeners.size();
            }

            @Override
            public L next() {
                L listener = listeners.get(index);
                if (disposables.contains(listener))
                    return listeners.remove(index);
                index++;
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

    @Override
    public @NotNull Mutable getInjectedMutable() {
        //noinspection ConstantConditions
        return null;
    }
}
