package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Value {
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
