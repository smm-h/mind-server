package ir.smmh.mind;

import ir.smmh.mind.impl.NumberValue;
import ir.smmh.mind.impl.StringValue;
import ir.smmh.nile.verbs.CanSerialize;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

public interface Value extends CanSerialize.JSON {
    static Value of(JSONObject object, Function<java.lang.String, ? extends Idea> lookup) {
        try {
            java.lang.String name = object.getString("~");
            switch (name) {
                case "number":
                    return new NumberValue(object.getDouble("value"));
                case "string":
                    return new StringValue(object.getString("value"));
                default:
                    Idea idea = lookup.apply(name);
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
        @Override
        default @NotNull JSONObject serializeJSON() throws JSONException {
            try {
                return new JSONObject("{\"~\": \"number\", \"value\": \"" + JSONObject.numberToString(getValue()) + "\"}");
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }
    }

    interface String extends Primitive<java.lang.String> {
        @Override
        default @NotNull JSONObject serializeJSON() throws JSONException {
            try {
                return new JSONObject("{\"~\": \"string\", \"value\": \"" + JSONObject.quote(getValue()) + "\"}");
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }
    }
}
