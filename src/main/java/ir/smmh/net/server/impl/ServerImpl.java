package ir.smmh.net.server.impl;

import ir.smmh.net.api.API;
import ir.smmh.net.server.ClientConnection;
import ir.smmh.net.server.ClientConnectionThread;
import ir.smmh.net.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl implements Server {

    private final API api;
    private boolean running;

    public ServerImpl(API api) {
        this.api = api;
    }

    @Override
    public @NotNull API getAPI() {
        return api;
    }

    @Override
    public final void start(int port) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            running = true;
            while (running) {
                Socket socket = serverSocket.accept();
                try {
                    new ClientConnectionThreadImpl(new ClientConnectionImpl(socket)).start();
                } catch (IOException e) {
                    System.err.println("Failed to communicate with the socket");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to listen for incoming connections");
        }
        System.out.println("Server stopped.");
    }

    @Override
    public final void stop() {
        System.out.println("Stopping the server...");
        running = false;
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