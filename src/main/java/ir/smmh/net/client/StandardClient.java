package ir.smmh.net.client;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface StandardClient extends Client {

    JSONObject send(String method, Object... parameters);

    JSONObject send(String method, JSONObject parameters);

    // override this to add common stuff like authentication to your parameters
    @NotNull JSONObject reviseParameters(JSONObject parameters);
}
