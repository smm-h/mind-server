package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface JSONUtil {

    static @Nullable JSONObject parse(@NotNull String string) {
        try {
            return new JSONObject(new JSONTokener(string));
        } catch (JSONException e) {
            return null;
        }
    }

    static <C extends Collection<Boolean>> @NotNull C arrayOfBooleans(@NotNull final JSONObject object, @NotNull final String key, @NotNull C destination) {
        return arrayOfBooleans(object, key, destination, FunctionalUtil::itself);
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfBooleans(@NotNull final JSONObject object, @NotNull final String key, @NotNull C destination, @NotNull Function<Boolean, T> convertor) {
        addFromBooleans(destination::add, object, key, convertor);
        return destination;
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfNumbers(@NotNull final JSONObject object, @NotNull final String key, @NotNull C destination, @NotNull Function<Number, T> convertor) {
        addFromNumbers(destination::add, object, key, convertor);
        return destination;
    }

    static <C extends Collection<String>> @NotNull C arrayOfStrings(@NotNull final JSONObject object, @NotNull final String key, @NotNull C destination) {
        return arrayOfStrings(object, key, destination, FunctionalUtil::itself);
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfStrings(@NotNull final JSONObject object, @NotNull final String key, @NotNull C destination, @NotNull Function<String, T> convertor) {
        addFromStrings(destination::add, object, key, convertor);
        return destination;
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfObjects(@NotNull final JSONObject object, @NotNull final String key, @NotNull C destination, @NotNull Function<JSONObject, T> convertor) {
        addFromJSONObjects(destination::add, object, key, convertor);
        return destination;
    }

    static <T> void addFromBooleans(@NotNull Consumer<T> add, @NotNull final JSONObject object, @NotNull final String key, @NotNull Function<Boolean, T> convertor) {
        if (object.has(key)) {
            final JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getBoolean(i)));
        }
    }

    static <T> void addFromNumbers(@NotNull Consumer<T> add, @NotNull final JSONObject object, @NotNull final String key, @NotNull Function<Number, T> convertor) {
        if (object.has(key)) {
            final JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getNumber(i)));
        }
    }

    static <T> void addFromStrings(@NotNull Consumer<T> add, @NotNull final JSONObject object, @NotNull final String key, @NotNull Function<String, T> convertor) {
        if (object.has(key)) {
            final JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getString(i)));
        }
    }

    static <T> void addFromJSONObjects(@NotNull Consumer<T> add, @NotNull final JSONObject object, @NotNull final String key, @NotNull Function<JSONObject, T> convertor) {
        if (object.has(key)) {
            final JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getJSONObject(i)));
        }
    }
}
