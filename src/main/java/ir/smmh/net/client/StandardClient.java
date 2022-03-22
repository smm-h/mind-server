package ir.smmh.net.client;

import org.json.JSONObject;

public interface StandardClient extends Client {
    JSONObject send(String method);

    JSONObject send(String method, JSONObject parameters);
}
