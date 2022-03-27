package ir.smmh.util;

import ir.smmh.nile.verbs.CanAddTo;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@ParametersAreNonnullByDefault
@SuppressWarnings({"ThrowsRuntimeException", "ClassWithTooManyMethods", "unused"})
public interface JSONUtil {

    static @NotNull JSONObject parse(String string) throws JSONException {
        return new JSONObject(new JSONTokener(string));
    }

    static <C extends CanAddTo<Boolean>> @NotNull C arrayOfBooleans(JSONObject object, String key, C canAddTo) throws JSONException {
        return arrayOfBooleans(object, key, canAddTo, FunctionalUtil::itself);
    }

    static <T, C extends CanAddTo<T>> @NotNull C arrayOfBooleans(JSONObject object, String key, C canAddTo, Function<Boolean, T> convertor) throws JSONException {
        addFromBooleans(canAddTo::add, object, key, convertor);
        return canAddTo;
    }

    static <T, C extends CanAddTo<T>> @NotNull C arrayOfNumbers(JSONObject object, String key, C canAddTo, Function<Number, T> convertor) throws JSONException {
        addFromNumbers(canAddTo::add, object, key, convertor);
        return canAddTo;
    }

    static <C extends CanAddTo<String>> @NotNull C arrayOfStrings(JSONObject object, String key, C canAddTo) throws JSONException {
        return arrayOfStrings(object, key, canAddTo, FunctionalUtil::itself);
    }

    static <T, C extends CanAddTo<T>> @NotNull C arrayOfStrings(JSONObject object, String key, C canAddTo, Function<String, T> convertor) throws JSONException {
        addFromStrings(canAddTo::add, object, key, convertor);
        return canAddTo;
    }

    static <T, C extends CanAddTo<T>> @NotNull C arrayOfJSONObjects(JSONObject object, String key, C canAddTo, Function<JSONObject, T> convertor) throws JSONException {
        addFromJSONObjects(canAddTo::add, object, key, convertor);
        return canAddTo;
    }

    static <T, C extends CanAddTo<T>> @NotNull C arrayOfObjects(JSONObject object, String key, C canAddTo, Function<Object, T> convertor) throws JSONException {
        addFromObjects(canAddTo::add, object, key, convertor);
        return canAddTo;
    }

    static <C extends Collection<Boolean>> @NotNull C arrayOfBooleans(JSONObject object, String key, C destination) throws JSONException {
        return arrayOfBooleans(object, key, destination, FunctionalUtil::itself);
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfBooleans(JSONObject object, String key, C destination, Function<Boolean, T> convertor) throws JSONException {
        addFromBooleans(destination::add, object, key, convertor);
        return destination;
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfNumbers(JSONObject object, String key, C destination, Function<Number, T> convertor) throws JSONException {
        addFromNumbers(destination::add, object, key, convertor);
        return destination;
    }

    static <C extends Collection<String>> @NotNull C arrayOfStrings(JSONObject object, String key, C destination) throws JSONException {
        return arrayOfStrings(object, key, destination, FunctionalUtil::itself);
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfStrings(JSONObject object, String key, C destination, Function<String, T> convertor) throws JSONException {
        addFromStrings(destination::add, object, key, convertor);
        return destination;
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfJSONObjects(JSONObject object, String key, C destination, Function<JSONObject, T> convertor) throws JSONException {
        addFromJSONObjects(destination::add, object, key, convertor);
        return destination;
    }

    static <T, C extends Collection<T>> @NotNull C arrayOfObjects(JSONObject object, String key, C destination, Function<Object, T> convertor) throws JSONException {
        addFromObjects(destination::add, object, key, convertor);
        return destination;
    }

    static <T> void addFromBooleans(Consumer<? super T> add, JSONObject object, String key, Function<? super Boolean, T> convertor) throws JSONException {
        if (object.has(key)) {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getBoolean(i)));
        }
    }

    static <T> void addFromNumbers(Consumer<? super T> add, JSONObject object, String key, Function<? super Number, T> convertor) throws JSONException {
        if (object.has(key)) {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getDouble(i)));
        }
    }

    static <T> void addFromStrings(Consumer<? super T> add, JSONObject object, String key, Function<? super String, T> convertor) throws JSONException {
        if (object.has(key)) {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getString(i)));
        }
    }

    static <T> void addFromJSONObjects(Consumer<? super T> add, JSONObject object, String key, Function<? super JSONObject, T> convertor) throws JSONException {
        if (object.has(key)) {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.getJSONObject(i)));
        }
    }

    static <T> void addFromObjects(Consumer<? super T> add, JSONObject object, String key, Function<? super Object, T> convertor) throws JSONException {
        if (object.has(key)) {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                add.accept(convertor.apply(array.get(i)));
        }
    }

    static <T> JSONArray toArrayOfBoolean(Iterable<T> iterable, Predicate<? super T> function) {
        JSONArray array = new JSONArray();
        for (T item : iterable) {
            array.put(function.test(item));
        }
        return array;
    }

    static <T> JSONArray toArrayOfInt(Iterable<T> iterable, ToIntFunction<? super T> function) {
        JSONArray array = new JSONArray();
        for (T item : iterable) {
            array.put(function.applyAsInt(item));
        }
        return array;
    }

    static <T> JSONArray toArrayOfString(Iterable<T> iterable, FunctionalUtil.ToStringFunction<? super T> function) {
        JSONArray array = new JSONArray();
        for (T item : iterable) {
            array.put(function.applyAsString(item));
        }
        return array;
    }

    static <T> JSONArray toArray(Iterable<T> iterable) {
        JSONArray array = new JSONArray();
        for (T item : iterable) {
            array.put(item);
        }
        return array;
    }

    static JSONObject map(String key, Object value) {
        JSONObject object = new JSONObject();
        try {
            object.put(key, value);
        } catch (JSONException ignored) {
        }
        return object;
    }

    static JSONObject map(String key1, Object value1, String key2, Object value2) {
        JSONObject object = new JSONObject();
        try {
            object.put(key1, value1);
            object.put(key2, value2);
        } catch (JSONException ignored) {
        }
        return object;
    }

    static JSONObject map(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        JSONObject object = new JSONObject();
        try {
            object.put(key1, value1);
            object.put(key2, value2);
            object.put(key3, value3);
        } catch (JSONException ignored) {
        }
        return object;
    }

    static JSONObject map(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4) {
        JSONObject object = new JSONObject();
        try {
            object.put(key1, value1);
            object.put(key2, value2);
            object.put(key3, value3);
            object.put(key4, value4);
        } catch (JSONException ignored) {
        }
        return object;
    }

    static JSONObject mapN(Object... parameters) {
        JSONObject p = new JSONObject();
        try {
            for (int i = 0; i < parameters.length; i += 2) {
                p.put((String) parameters[i], parameters[i + 1]);
            }
        } catch (JSONException ignored) {
        }
        return p;
    }

    static Iterable<Object> iterateOverArray(JSONArray array) {
        return () -> new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length();
            }

            @Override
            public Object next() {
                try {
                    return array.get(i++);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

}
