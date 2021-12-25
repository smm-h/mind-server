package ir.smmh.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Storage {

    boolean exists(@NotNull final String id);

    @Nullable
    String read(@NotNull final String id);

    boolean write(@NotNull final String id, @NotNull final String contents);

    boolean delete(@NotNull final String id);
}
