package ir.smmh.mind.impl;

import ir.smmh.mind.Mind;
import ir.smmh.storage.impl.StorageGateImpl;
import ir.smmh.storage.impl.StorageImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MindStorageGate extends StorageGateImpl<Mind.Mutable> {

    public MindStorageGate(@NotNull String root) {
        super(StorageImpl.of(root));
    }

    @Override
    public final @NotNull Mind.Mutable createBlank(String identifier) {
        return MutableMindImpl.createBlank(identifier, null);
    }

    @Override
    public final @Nullable Mind.Mutable deserialize(String identifier, String serialization) {
        return MutableMindImpl.parse(identifier, serialization);
    }
}
