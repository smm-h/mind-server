package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface JSONUtil {
    @NotNull
    static Set<String> getStringSet() {
        return new LinkedHashSet<>();
    }

    @NotNull
    static Set<String> getStringSet(@NotNull final JSONObject object, @NotNull final String key) {
        final Set<String> set = getStringSet();
        if (object.has(key)) {
            final JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                set.add(array.getString(i));
        }
        return set;
    }

    @NotNull
    static List<String> getStringList() {
        return new ArrayList<>();
    }

    @NotNull
    static List<String> getStringList(@NotNull final JSONObject object, @NotNull final String key) {
        final List<String> list = getStringList();
        if (object.has(key)) {
            final JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++)
                list.add(array.getString(i));
        }
        return list;
    }
}
