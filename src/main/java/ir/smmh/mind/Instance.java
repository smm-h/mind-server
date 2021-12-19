package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Set;

public interface Instance {
    @NotNull
    Idea getType();

    @NotNull
    default JSONObject serialize() {
        final JSONObject object = new JSONObject();
        Idea type = getType();
        object.put("~", type.getName());
        Set<Property<?>> properties = type.getAllProperties();
        if (properties != null) {
            for (Property<?> property : properties) {
                object.put(property.getName(), get(property));
            }
        }
        return object;
    }

    <T> boolean has(Property<T> property);

    <T> void set(Property<T> property, Object value);

    @Nullable
    <T> T get(Property<T> property);

    boolean is(Idea idea);

    void setLink(Idea idea, Instance instance);

    @Nullable
    Instance getLink(Idea idea);
}
