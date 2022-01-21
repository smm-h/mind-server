package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.StaticProperty;
import ir.smmh.mind.Value;
import ir.smmh.nile.verbs.CanSerialize;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class StaticPropertyImpl implements StaticProperty, CanSerialize.JSON {

    private final Idea origin;
    private final String name;
    private final String type;
    private final Value value;

    public StaticPropertyImpl(Idea origin, String name, String type, Value value) {
        super();
        this.origin = origin;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public StaticPropertyImpl(Idea origin, JSONObject object) {
        this(origin, object.getString("name"), object.getString("type"), Value.of(object.getJSONObject("value"), origin.getMind()::findIdeaByName));
    }

    @Override
    public final @NotNull JSONObject serializeJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", name);
            object.put("type", type);
            object.put("value", value.serializeJSON());
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
    public final Value getValue() {
        return value;
    }

    @Override
    public final String toString() {
        return name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StaticPropertyImpl))
            return false;

        StaticPropertyImpl property = (StaticPropertyImpl) o;

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
