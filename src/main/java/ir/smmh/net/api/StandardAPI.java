package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An standard API is an API whose requests are JSON maps that contain 'method',
 * 'parameters', and sometimes 'authentication' keys, and whose responses are
 * JSON maps that contain the keys 'ok', 'description' if not ok, and possibly
 * 'results' if ok.
 */
@ParametersAreNonnullByDefault
public interface StandardAPI extends API {
    void defineMethod(String name, Method method);

    int defineError(String description);

    @NotNull JSONObject notOk(String errorDescription);

    @NotNull JSONObject notOk(int errorCode);

    @NotNull JSONObject notOk(int errorCode, Throwable thrown);

    @NotNull JSONObject ok();

    @NotNull JSONObject ok(JSONObject results);

    @NotNull JSONObject ok(String key, Object value);

    @Override
    default @NotNull String process(String request) {
        return processJSON(request).toString();
    }

    @NotNull JSONObject processJSON(String request);
}
