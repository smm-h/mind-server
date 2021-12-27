package ir.smmh.storage.impl;

import ir.smmh.storage.Storage;
import ir.smmh.storage.StorageGate;
import ir.smmh.storage.Stored;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class StorageGateImpl<T extends Stored> implements StorageGate<T> {

    private final Map<String, T> memory = new HashMap<>();

    private final Storage storage;
    private final Function<@Nullable String, @Nullable T> deserializer;

    public StorageGateImpl(Storage storage, Function<@Nullable String, @Nullable T> deserializer) {
        this.storage = storage;
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
        return getStorage().exists(id);
    }

    @Nullable
    @Override
    public T findOnDisk(@NotNull String id) {
        return deserializer.apply(getStorage().read(id));
    }

    @Override
    public void addToMemory(@NotNull T object) {
        memory.put(object.getName(), object);
    }

    @Override
    public @NotNull Storage getStorage() {
        return storage;
    }
}
