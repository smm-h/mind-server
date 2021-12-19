package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Value {
    @NotNull JSONObject serialize();

    interface Number extends Value {

        java.lang.Number getValue();

        default @NotNull JSONObject serialize() {
            return new JSONObject("{\"~\": \"number\", \"value\": \"" + JSONObject.numberToString(getValue()) + "\"}");
        }
    }

    interface String extends Value {

        java.lang.String getValue();

        default @NotNull JSONObject serialize() {
            return new JSONObject("{\"~\": \"string\", \"value\": \"" + JSONObject.quote(getValue()) + "\"}");
        }
    }
}
