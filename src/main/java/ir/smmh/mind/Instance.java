package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public interface Instance extends Value {
    @NotNull Idea getType();

    @NotNull
    default JSONObject serializeJSON() {
        final JSONObject object = new JSONObject();
        try {
            Idea type = getType();
            object.put("~", type.getName());
            Set<Property> properties = type.getAllProperties();
            if (properties != null) {
                for (Property property : properties) {
                    Value value = get(property);
                    object.put(property.getName(), value == null ? JSONObject.NULL : value.serializeJSON());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    boolean has(Property property);

    void set(Property property, Value value);

    @Nullable Value get(Property property);

    boolean is(Idea idea);

    void setLink(Idea idea, Instance instance);

    @Nullable Instance getLink(Idea idea);
}
