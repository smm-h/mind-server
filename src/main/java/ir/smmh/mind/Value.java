package ir.smmh.mind;

import ir.smmh.mind.impl.NumberValue;
import ir.smmh.mind.impl.StringValue;
import ir.smmh.util.Lookup;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public interface Value extends Serializable.JSON {
    static Value of(JSONObject object, Lookup<Idea> lookup) {
        try {
            final java.lang.String name = object.getString("~");
            switch (name) {
                case "number":
                    return new NumberValue(object.getDouble("value"));
                case "string":
                    return new StringValue(object.getString("value"));
                default:
                    final Idea idea = lookup.find(name);
                    if (idea == null) {
                        System.err.println("no such idea: " + name);
                        return null;
                    } else {
                        return idea.deserialize(object);
                    }
            }
        } catch (JSONException e) {
            return null;
        }
    }

    default boolean isPrimitive() {
        return false;
    }

    interface Primitive<T> extends Value {
        T getValue();

        @Override
        default boolean isPrimitive() {
            return true;
        }
    }

    interface Number extends Primitive<java.lang.Number> {
        default @NotNull JSONObject serializeJSON() throws JSONException {
            return new JSONObject("{\"~\": \"number\", \"value\": \"" + JSONObject.numberToString(getValue()) + "\"}");
        }
    }

    interface String extends Primitive<java.lang.String> {
        default @NotNull JSONObject serializeJSON() throws JSONException {
            return new JSONObject("{\"~\": \"string\", \"value\": \"" + JSONObject.quote(getValue()) + "\"}");
        }
    }
}
