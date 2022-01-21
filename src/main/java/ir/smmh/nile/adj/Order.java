package ir.smmh.nile.adj;

import ir.smmh.nile.verbs.CanClear;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Order<T> extends Multitude, CanClear {

    @Nullable T peek();

    @Nullable T poll();

    boolean canEnter();

    <S extends T> void enter(@NotNull S toEnter);
}
