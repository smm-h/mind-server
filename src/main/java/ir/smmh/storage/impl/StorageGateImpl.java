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

    protected StorageGateImpl(Storage storage) {
        super();
        this.storage = storage;
    }

    @Override
    public final boolean existsInMemory(@NotNull String id) {
        return memory.containsKey(id);
    }

    @Override
    public final @Nullable T findInMemory(@NotNull String id) {
        return memory.get(id);
    }

    @Override
    public final boolean existsOnDisk(@NotNull String id) {
        return getStorage().exists(id);
    }

    @Nullable
    @Override
    public final T findOnDisk(@NotNull String id) {
        return deserialize(id, getStorage().read(id));
    }

    @Override
    public final void addToMemory(@NotNull T object) {
        memory.put(object.getName(), object);
    }

    @Override
    public final void clearMemory() {
        memory.clear();
    }

    @Override
    public final void clearDisk() {
        getStorage().deleteAll();
    }

    @Override
    public final @NotNull Storage getStorage() {
        return storage;
    }
}
