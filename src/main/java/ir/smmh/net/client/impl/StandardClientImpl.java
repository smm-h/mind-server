package ir.smmh.net.client.impl;

import ir.smmh.net.client.StandardClient;
import ir.smmh.util.JSONUtil;
import org.json.JSONObject;

public class StandardClientImpl extends ClientImpl implements StandardClient {
    public StandardClientImpl(int port, String hostAddress) {
        super(port, hostAddress);
    }

    @Override
    public JSONObject send(String method) {
        return send(method, "{}");
    }

    @Override
    public JSONObject send(String method, JSONObject parameters) {
        return send(method, parameters.toString());
    }

    private JSONObject send(String method, String parameters) {
        String responseString = sendRequest("{\"method\":" + method + ",\"parameters\":" + parameters + "}");
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
