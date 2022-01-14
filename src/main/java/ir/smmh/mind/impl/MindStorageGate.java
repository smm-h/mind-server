package ir.smmh.mind.impl;

import ir.smmh.mind.Mind;
import ir.smmh.storage.impl.StorageGateImpl;
import ir.smmh.storage.impl.StorageImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

public class MindStorageGate extends StorageGateImpl<Mind.Mutable> {

    public MindStorageGate(@NotNull String root) {
        super(new StorageImpl(root));
    }

    @Override
    public @NotNull Mind.Mutable createBlank(String identifier) {
        return new MutableMindImpl(identifier, null);
    }

    @Override
    public @Nullable Mind.Mutable deserialize(String identifier, String serialization) {
        try {
            return new MutableMindImpl(JSONUtil.parse(serialization));
        } catch (JSONException e) {
            return null;
        }
    }
}
