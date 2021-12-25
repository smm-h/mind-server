package ir.smmh.mind.impl;

import ir.smmh.mind.Mind;
import ir.smmh.storage.Storage;
import ir.smmh.storage.impl.StorageGateImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.function.Function;

public class MindStorageGate extends StorageGateImpl<Mind.Mutable> {

    public MindStorageGate(Storage ps, Function<@Nullable String, Mind.@Nullable Mutable> deserializer) {
        super(ps, deserializer);
    }

    @Override
    public @NotNull Mind.Mutable createBlank(String identifier) {
        return new MutableMindImpl(identifier, null);
    }

    @Override
    public @Nullable Mind.Mutable deserialize(String identifier, String serialization) {
        JSONObject object = JSONUtil.parse(serialization);
        return object == null ? null : new MutableMindImpl(object);
    }
}
