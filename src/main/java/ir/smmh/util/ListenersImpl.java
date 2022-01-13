package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@ParametersAreNonnullByDefault
public class ListenersImpl<L> implements Listeners<L>{

    // remembers the order of insertion
    private final Set<L> set = new LinkedHashSet<>();

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public void add(L listener) {
        set.add(listener);
    }

    @Override
    public void remove(L listener) {
        set.remove(listener);
    }

    @Override
    public @NotNull Iterator<L> iterator() {
        return set.iterator();
    }
}
