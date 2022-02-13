package ir.smmh.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A JSON API is an API whose requests are JSON maps that contain 'method',
 * 'parameters', and sometimes 'authentication' keys, and whose responses are
 * JSON maps that contain the 'error_code', 'description', and sometimes
 * 'results' keys.
 */
@ParametersAreNonnullByDefault
public interface JSONAPI extends API {
    void defineMethod(String name, Method method);

    int defineError(String description);

    @NotNull JSONObject respond(int errorCode);

    @NotNull JSONObject respond(int errorCode, Throwable thrown);

    @NotNull JSONObject respond(JSONObject results);

    @Override
    default @NotNull String process(String request) {
        return processJSON(request).toString();
    }

    @NotNull JSONObject processJSON(String request);
}
