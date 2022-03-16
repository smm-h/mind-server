package ir.smmh.net.client;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface StandardClient extends Client {

    JSONObject send(String method, Object... parameters);

    JSONObject send(String method, JSONObject parameters);

    @NotNull JSONObject reviseParameters(JSONObject parameters);
}
