package ir.smmh.common;

import org.jetbrains.annotations.NotNull;

public interface MutableAdapter<T> extends Mutable<T> {

    Mutable<T> getMutableAdapter();

    @Override
    default @NotNull T freeze() {
        return getMutableAdapter().freeze();
    }

    @Override
    default boolean isDirty() {
        return getMutableAdapter().isDirty();
    }

    @Override
    default void taint() {
        getMutableAdapter().taint();
    }

    @Override
    default void onClean() {
        getMutableAdapter().onClean();
    }
}
