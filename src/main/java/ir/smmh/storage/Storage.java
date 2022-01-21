package ir.smmh.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Storage {

    boolean exists(@NotNull String id);

    @Nullable String read(@NotNull String id);

    boolean write(@NotNull String id, @NotNull String contents);

    boolean delete(@NotNull String id);

    void deleteAll();
}
