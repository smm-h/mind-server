package ir.smmh.net;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

public interface API {
    @NotNull
    String request(@NotNull String request);

    interface JSON extends API {

        @Override
        default @NotNull String request(@NotNull String request) {
            return requestJSON(new JSONObject(new JSONTokener(request))).toString();
        }

        @NotNull
        JSONObject requestJSON(@NotNull JSONObject request);
    }
}
