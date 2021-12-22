package ir.smmh.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StorageImpl<T extends PermanentlyStored> implements Storage<T> {

    private final Map<String, T> memory = new HashMap<>();

    private final PermanentStorage fs;
    private final Function<@Nullable String, @Nullable T> deserializer;

    public StorageImpl(PermanentStorage fs, Function<@Nullable String, @Nullable T> deserializer) {
        this.fs = fs;
        this.deserializer = deserializer;
    }

    @Override
    public boolean existsInMemory(@NotNull String id) {
        return memory.containsKey(id);
    }

    @Override
    public @Nullable T findInMemory(@NotNull String id) {
        return memory.get(id);
    }

    @Override
    public boolean existsOnDisk(@NotNull String id) {
        return getFileSystem().exists(id);
    }

    @Nullable
    @Override
    public T findOnDisk(@NotNull String id) {
        return deserializer.apply(getFileSystem().read(id));
    }

    @Override
    public void addToMemory(@NotNull T object) {
        memory.put(object.getIdentifier(), object);
    }

    @Override
    public @NotNull PermanentStorage getFileSystem() {
        return fs;
    }
}
