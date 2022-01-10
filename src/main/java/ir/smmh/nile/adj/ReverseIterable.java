package ir.smmh.nile.adj;

import org.jetbrains.annotations.NotNull;

public interface ReverseIterable<T> {
    @NotNull Iterable<T> inReverse();
}
