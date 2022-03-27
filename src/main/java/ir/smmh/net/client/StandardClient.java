package ir.smmh.net.client;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;

public interface StandardClient extends Client {
    JSONObject send(String method) throws NotOkException, JSONException;

    JSONObject send(String method, JSONObject parameters) throws NotOkException, JSONException;

    Socket reconnect() throws NotOkException;
}
