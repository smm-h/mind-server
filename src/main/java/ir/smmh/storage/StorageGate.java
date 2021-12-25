package ir.smmh.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This kind of storage is a bridge between RAM and a permanent storage.
 *
 * @param <T> The specific type of permanently store-able objects contained
 *            in this storage
 */
public interface StorageGate<T extends Stored> {

    @NotNull T createBlank(String identifier);

    @Nullable T deserialize(String identifier, String serialization);

    @NotNull
    Storage getStorage();

    default boolean exists(@Nullable String id) {
        return id != null && (existsInMemory(id) || existsOnDisk(id));
    }

    boolean existsOnDisk(@NotNull String id);

    boolean existsInMemory(@NotNull String id);

    @Nullable
    default T find(@Nullable String id) {
        if (id == null) return null;
        if (!existsInMemory(id)) {
            T object = findOnDisk(id);
            if (object != null) {
                addToMemory(object);
            }
        }
        return findInMemory(id);
    }

    void addToMemory(@NotNull T object);

    @Nullable
    T findInMemory(@NotNull String id);

    @Nullable
    T findOnDisk(@NotNull String id);
}