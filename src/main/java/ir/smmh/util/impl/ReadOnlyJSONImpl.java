package ir.smmh.util.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.ReadOnlyJSON;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

public class ReadOnlyJSONImpl implements ReadOnlyJSON {
    private final JSONObject wrapped;

    public ReadOnlyJSONImpl(JSONObject wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public @NotNull JSONObject toJSONObject() {
        return wrapped;
    }

    @Override
    public boolean has(String key) {
        return wrapped.has(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return wrapped.getBoolean(key);
    }

    @Override
    public int getInteger(String key) {
        return wrapped.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return wrapped.getLong(key);
    }

    @Override
    public @Nullable Boolean getNullableBoolean(String key) {
        return wrapped.has(key) ? wrapped.getBoolean(key) : null;
    }

    @Override
    public @Nullable Integer getNullableInteger(String key) {
        return wrapped.has(key) ? wrapped.getInt(key) : null;
    }

    @Override
    public @Nullable Long getNullableLong(String key) {
        return wrapped.has(key) ? wrapped.getLong(key) : null;
    }

    @Override
    public float getFloat(String key) {
        return wrapped.getFloat(key);
    }

    @Override
    public @Nullable Float getNullableFloat(String key) {
        return wrapped.has(key) ? wrapped.getFloat(key) : null;
    }

    @Override
    public @NotNull String getString(String key) {
        return wrapped.getString(key);
    }

    @Override
    public @NotNull JSONObject getJSONObject(String key) {
        return wrapped.getJSONObject(key);
    }

    @Override
    public @Nullable JSONObject getNullableJSONObject(String key) {
        return wrapped.optJSONObject(key);
    }

    @Override
    public @Nullable String getNullableString(String key) {
        return wrapped.optString(key, null);
    }

    @Override
    public @NotNull <T> Sequential<T> getSequential(String key, Function<Object, T> convertor) {
        if (wrapped.has(key)) {
            JSONArray array = wrapped.getJSONArray(key);
            Sequential.Mutable.VariableSize<T> sequence = new SequentialImpl<>(array.length());
            for (Object object : JSONUtil.iterateOverArray(array)) {
                sequence.add(convertor.apply(object));
            }
            return sequence;
        } else {
            throw new JSONException("key does not exist: " + key);
        }
    }

    @Override
    public @Nullable <T> Sequential<T> getNullableSequential(String key, Function<Object, T> convertor) {
        return has(key) ? getSequential(key, convertor) : null;
    }

    @Override
    public @NotNull <T> Sequential<Sequential<T>> get2DSequential(String key, Function<Object, T> convertor) {
        if (wrapped.has(key)) {
            JSONArray outerArray = wrapped.getJSONArray(key);
            Sequential.Mutable.VariableSize<Sequential<T>> outerSequence = new SequentialImpl<>(outerArray.length());
            for (Object object : outerArray) {
                JSONArray innerArray = (JSONArray) object;
                Sequential.Mutable.VariableSize<T> innerSequence = new SequentialImpl<>(innerArray.length());
                outerSequence.append(innerSequence);
                for (Object innerObject : innerArray) {
                    innerSequence.add(convertor.apply(innerObject));
                }
            }
            return outerSequence;
        } else {
            throw new JSONException("key does not exist: " + key);
        }
    }

    @Override
    public @Nullable <T> Sequential<Sequential<T>> getNullable2DSequential(String key, Function<Object, T> convertor) {
        return has(key) ? get2DSequential(key, convertor) : null;
    }

    @Override
    public String toString() {
        return wrapped.toString(2);
    }
}
