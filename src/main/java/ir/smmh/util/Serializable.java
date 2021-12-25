package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Serializable {
    @NotNull
    String serialize();

    interface JSON extends Serializable {
        @NotNull
        JSONObject serializeJSON();

        @Override
        @NotNull
        default String serialize() {
            return serializeJSON().toString();
        }
    }
}
