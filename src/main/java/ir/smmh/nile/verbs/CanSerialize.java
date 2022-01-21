package ir.smmh.nile.verbs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface CanSerialize {
    @NotNull String serialize();

    interface JSON extends CanSerialize {
        @NotNull JSONObject serializeJSON();

        @Override
        @NotNull
        default String serialize() {
            return serializeJSON().toString();
        }
    }
}
