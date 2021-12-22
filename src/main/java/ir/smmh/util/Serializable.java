package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Serializable {
    @NotNull
    String serialize();

    interface JSON {
        @NotNull
        JSONObject serializeJSON();

        @NotNull
        default String serialize() {
            return serializeJSON().toString();
        }
    }
}
