package ir.smmh.util;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.function.Function;

public interface ReadOnlyJSON {
    @NotNull JSONObject toJSONObject();

    boolean has(String key);

    boolean getBoolean(String key);

    int getInteger(String key);

    long getLong(String key);

    float getFloat(String key);

    @Nullable Boolean getNullableBoolean(String key);

    @Nullable Integer getNullableInteger(String key);

    @Nullable Long getNullableLong(String key);

    @Nullable Float getNullableFloat(String key);

    @NotNull String getString(String key);

    @NotNull JSONObject getJSONObject(String key);

    @Nullable JSONObject getNullableJSONObject(String key);

    @Nullable String getNullableString(String key);

    @NotNull <T> Sequential<T> getSequential(String key, Function<Object, T> convertor);

    @Nullable <T> Sequential<T> getNullableSequential(String key, Function<Object, T> convertor);

    @NotNull <T> Sequential<Sequential<T>> get2DSequential(String key, Function<Object, T> convertor);

    @Nullable <T> Sequential<Sequential<T>> getNullable2DSequential(String key, Function<Object, T> convertor);
}
