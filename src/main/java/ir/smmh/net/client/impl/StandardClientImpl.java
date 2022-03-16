package ir.smmh.net.client.impl;

import ir.smmh.net.client.StandardClient;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class StandardClientImpl extends ClientImpl implements StandardClient {
    public StandardClientImpl(int port, String hostAddress) {
        super(port, hostAddress);
    }

    @Override
    public JSONObject send(String method, Object... parameters) {
        JSONObject p = new JSONObject();
        for (int i = 0; i < parameters.length; i += 2) {
            p.put((String) parameters[i], parameters[i + 1]);
        }
        return send(method, p);
    }

    @Override
    public JSONObject send(String method, JSONObject parameters) {
        String responseString = sendRequest("{\"method\":" + method + ",\"parameters\":" +
                reviseParameters(parameters) + "}");
        if (responseString == null) {
            System.err.println("No response");
            return null;
        } else {
            JSONObject response = JSONUtil.parse(responseString);
            if (response.getBoolean("ok")) {
                return response.has("results")
                        ? response.getJSONObject("results")
                        : null;
            } else {
                throw new RuntimeException(response.has("error_description")
                        ? response.getString("error_description")
                        : "Server error");
            }
        }
    }

    @Override
    public @NotNull JSONObject reviseParameters(JSONObject parameters) {
        return parameters;
    }
}
