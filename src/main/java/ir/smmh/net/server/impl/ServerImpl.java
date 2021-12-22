package ir.smmh.net.server.impl;

import ir.smmh.api.API;
import ir.smmh.net.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerImpl implements Server {

    private final API api;
    private final int defaultPort;
    private boolean running;

    public ServerImpl(API api, int defaultPort) {
        this.api = api;
        this.defaultPort = defaultPort;
    }

    @Override
    public API getAPI() {
        return api;
    }

    @Override
    public int getDefaultPort() {
        return defaultPort;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void start(int port) {
        System.out.println("Server started...");
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            running = true;
            while (running)
                new RequestHandlerImpl(serverSocket.accept(), getAPI()::sendRequest).start();
        } catch (IOException e) {
            System.err.println("Failed to keep the server running");
        }
        System.out.println("Server shutting down...");
    }

}
