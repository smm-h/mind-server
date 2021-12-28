package ir.smmh.storage.impl;

import ir.smmh.storage.Storage;
import ir.smmh.storage.StorageGate;
import ir.smmh.storage.Stored;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class StorageGateImpl<T extends Stored> implements StorageGate<T> {

    private final Map<String, T> memory = new HashMap<>();

    private final Storage storage;

    public StorageGateImpl(Storage storage) {
        this.storage = storage;
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
        return deserialize(id, getStorage().read(id));
    }

    @Override
    public void addToMemory(@NotNull T object) {
        memory.put(object.getName(), object);
    }

    @Override
    public void clearMemory() {
        memory.clear();
    }

    @Override
    public void clearDisk() {
        getStorage().deleteAll();
    }

    @Override
    public @NotNull Storage getStorage() {
        return storage;
    }
}
