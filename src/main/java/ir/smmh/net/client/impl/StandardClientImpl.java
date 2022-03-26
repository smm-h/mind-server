package ir.smmh.net.client.impl;

import ir.smmh.net.client.NotOkException;
import ir.smmh.net.client.StandardClient;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StandardClientImpl extends ClientImpl implements StandardClient {
    private final String authentication;

    public StandardClientImpl(int port, String hostAddress) {
        this(port, hostAddress, null);
    }

    public StandardClientImpl(int port, String hostAddress, @Nullable JSONObject authentication) {
        super(port, hostAddress);
        this.authentication = authentication == null ? "" : (",\"authentication\":" + authentication);
    }

    @Override
    public JSONObject send(String method) throws NotOkException {
        return send(method, "{}");
    }

    @Override
    public JSONObject send(String method, JSONObject parameters) throws NotOkException {
        return send(method, parameters.toString());
    }

    @Override
    public Socket reconnect() throws NotOkException {
        try {
            Socket socket = connect();
            DataInputStream i = new DataInputStream(socket.getInputStream());
            DataOutputStream o = new DataOutputStream(socket.getOutputStream());
            try {
                o.writeUTF(makeRequestString("reconnect", "{}"));
                try {
                    JSONObject response = JSONUtil.parse(i.readUTF());
                    if (response.getBoolean("ok")) {
                        return socket;
                    } else {
                        throw new NotOkException(response.has("error_description")
                                ? response.getString("error_description")
                                : "Server error");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Could not read response");
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not write request");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not establish connection to send request");
        }
    }

    private String makeRequestString(String method, String parameters) {
        return "{\"method\":\"" + method + "\"" + authentication + ",\"parameters\":" + parameters + "}";
    }

    private JSONObject send(String method, String parameters) throws NotOkException {
        String responseString = sendRequest(makeRequestString(method, parameters));
        JSONObject response = JSONUtil.parse(responseString);
        if (response.getBoolean("ok")) {
            return response.has("results")
                    ? response.getJSONObject("results")
                    : null;
        } else {
            throw new NotOkException(response.has("error_description")
                    ? response.getString("error_description")
                    : "Server error");
        }
    }

}
