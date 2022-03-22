package ir.smmh.net.api;

import ir.smmh.net.server.StandardServer;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An standard API is an API whose requests are JSON maps that contain 'method',
 * 'parameters', and sometimes 'authentication' keys, and whose responses are
 * JSON maps that contain the keys 'ok', 'description' if not ok, and possibly
 * 'results' if ok. Standard APIs are also designed to work exclusively with
 * standard servers.
 *
 * @see StandardServer
 */
@ParametersAreNonnullByDefault
public interface StandardAPI extends API {
    int NO_ERROR = 0;
    int COULD_NOT_PARSE_REQUEST = 1;
    int METHOD_NOT_FOUND = 2;
    int UNEXPECTED_ERROR = 3;
    int BUG = 4;

    void defineAll(StandardServer<?> server);

    void defineMethod(String name, Method method);

    void defineError(int errorCode, String description);

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
