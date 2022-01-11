package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.StaticProperty;
import ir.smmh.mind.Value;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class StaticPropertyImpl implements StaticProperty, Serializable.JSON {

    private final Idea origin;
    private final String name;
    private final String type;
    private final Value value;

    public StaticPropertyImpl(Idea origin, String name, String type, Value value) {
        this.origin = origin;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public StaticPropertyImpl(Idea origin, JSONObject object) {
        this(origin, object.getString("name"), object.getString("type"), Value.of(object.getJSONObject("value"), origin.getMind()::findIdeaByName));
    }

    @Override
    public @NotNull JSONObject serializeJSON() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("type", type);
        object.put("value", value.serializeJSON());
        return object;
    }

    @Override
    public Idea getOrigin() {
        return origin;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
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
    public int hashCode() {
        return name.hashCode();
    }
}
