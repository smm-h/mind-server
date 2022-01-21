package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;
import ir.smmh.mind.Value;
import ir.smmh.nile.verbs.CanSerialize;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Supplier;

public class PropertyImpl implements Property, CanSerialize.JSON {

    private final Idea origin;
    private final String name;
    private final String type;
    private final Supplier<Value> defaultValue;

    public PropertyImpl(Idea origin, String name, String type, Supplier<Value> defaultValue) {
        super();
        this.origin = origin;
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public PropertyImpl(Idea origin, JSONObject object) {
        this(origin, object.getString("name"), object.getString("type"), () -> Value.of(object.getJSONObject("defaultValue"), origin.getMind()::findIdeaByName));
    }

    @Override
    public final @NotNull JSONObject serializeJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", name);
            object.put("type", type);
            object.put("defaultValue", defaultValue.get().serializeJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public final Idea getOrigin() {
        return origin;
    }

    @Override
    public final @NotNull String getName() {
        return name;
    }

    @Override
    public final String getType() {
        return type;
    }

    @Override
    public final Supplier<Value> getDefaultValue() {
        return defaultValue;
    }

    @Override
    public final String toString() {
        return name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PropertyImpl))
            return false;

        PropertyImpl property = (PropertyImpl) o;

        if (!origin.equals(property.origin))
            return false;
        if (!name.equals(property.name))
            return false;
        return type.equals(property.type);
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }
}
