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
        object.put("type", type.getName());
        Set<Property> properties = type.getAllProperties();
        if (properties != null) {
            for (Property property : properties) {
                object.put(property.getName(), get(property));
            }
        }
        return object;
    }

    boolean has(Property property);

    void set(Property property, int value);

    int get(Property property);

    boolean is(Idea idea);

    void set(Idea idea, Instance instance);

    @Nullable
    Instance get(Idea idea);
}
