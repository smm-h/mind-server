package ir.smmh.net.client;

import org.json.JSONObject;

import java.net.Socket;

public interface StandardClient extends Client {
    JSONObject send(String method) throws NotOkException;

    JSONObject send(String method, JSONObject parameters) throws NotOkException;

    Socket reconnect() throws NotOkException;
}
