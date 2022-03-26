package ir.smmh.net.server.impl;

import ir.smmh.net.api.API;
import ir.smmh.net.server.ClientConnection;
import ir.smmh.net.server.ClientConnectionThread;
import ir.smmh.net.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerImpl implements Server {

    private final API api;
    private final ServerSocket listeningSocket;
    private boolean running;

    public ServerImpl(API api) throws IOException {
        this(api, 0);
    }

    public ServerImpl(API api, int port) throws IOException {
        this.api = api;
        listeningSocket = new ServerSocket(port);
    }

    @Override
    public @NotNull API getAPI() {
        return api;
    }

    @Override
    public int getPort() {
        return listeningSocket.getLocalPort();
    }

    @Override
    public final void start() {
        System.out.println("Server started...");
        running = true;
        while (running) {
            try {
                new ClientConnectionThreadImpl(new ClientConnectionImpl(listeningSocket.accept())).start();
            } catch (IOException e) {
                System.err.println("Error while listening for incoming connections");
            }
        }
        System.out.println("Server stopped.");
    }

    @Override
    public final void stop() {
        System.out.println("Stopping the server...");
        running = false;
        try {
            listeningSocket.close();
        } catch (IOException ignored) {
        }
    }

    private class ClientConnectionThreadImpl extends Thread implements ClientConnectionThread {
        private final ClientConnection connection;
        private boolean connectionIsPrivate = true;

        public ClientConnectionThreadImpl(ClientConnection connection) {
            this.connection = connection;
        }

        @Override
        public ClientConnection getConnection() {
            connectionIsPrivate = false;
            return connection;
        }

        @Override
        public void run() {
            try {
                String request = connection.receive();
                try {
                    String response = api.sendRequest(request);
                    try {
                        connection.send(response);
                        if (connectionIsPrivate) {
                            connection.close();
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to send response");
                    }
                } catch (Throwable e) {
                    System.err.println("Failed to process request");
                }
            } catch (IOException e) {
                System.err.println("Failed to receive request");
            }
        }
    }
}