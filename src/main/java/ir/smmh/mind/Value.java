package ir.smmh.mind;

import ir.smmh.mind.impl.NumberValue;
import ir.smmh.mind.impl.StringValue;
import ir.smmh.util.Lookup;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Value {
    static Value of(JSONObject object, Lookup<Idea> lookup) {
        final java.lang.String name = object.getString("~");
        switch (name) {
            case "number":
                return new NumberValue(object.getNumber("value"));
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
    }

    @NotNull JSONObject serialize();

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
        default @NotNull JSONObject serialize() {
            return new JSONObject("{\"~\": \"number\", \"value\": \"" + JSONObject.numberToString(getValue()) + "\"}");
        }
    }

    interface String extends Primitive<java.lang.String> {
        default @NotNull JSONObject serialize() {
            return new JSONObject("{\"~\": \"string\", \"value\": \"" + JSONObject.quote(getValue()) + "\"}");
        }
    }
}
