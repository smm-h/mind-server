package ir.smmh.net.server.impl;

import ir.smmh.net.api.API;
import ir.smmh.net.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ServerImpl implements Server {

    private final API api;
    private boolean running;
    private final Set<Socket> sockets = Collections.synchronizedSet(new HashSet<>());

    public ServerImpl(API api) {
        super();
        this.api = api;
    }

    @Override
    public final API getAPI() {
        return api;
    }

    @Override
    public final void start(int port) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            running = true;
            while (running) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                new Connection(socket, getAPI()::sendRequest).start();
            }
        } catch (IOException e) {
            System.err.println("Failed to keep the server running");
        }
        System.out.println("Server stopped...");
    }

    @Override
    public final void stop() {
        running = false;
    }

    @Override
    public void broadcast(String data) {
        // iteration over synchronized collections must be within
        // manually synchronized blocks as per its documentation
        int failures = 0;
        synchronized (sockets) {
            for (Socket socket : sockets) {
                if (socket.isClosed()) {
                    sockets.remove(socket);
                } else {
                    try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                        output.writeUTF(data);
                        output.flush();
                    } catch (IOException e) {
                        failures++;
                    }
                }
            }
        }
        if (failures > 0)
            System.err.println("Failed to send data to " + failures + " client(s) during broadcast");
    }

    private static class Connection extends Thread {

        private final Socket socket;
        private final Function<? super @NotNull String, @NotNull String> requestProcessor;

        public Connection(@NotNull Socket socket, Function<? super @NotNull String, @NotNull String> requestProcessor) {
            this.socket = socket;
            this.requestProcessor = requestProcessor;
        }

        @Override
        public final void run() {
            try {
                try (DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
                    try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                        try {
                            String request = input.readUTF();
                            try {
                                String response = requestProcessor.apply(request);
                                try {
                                    output.writeUTF(response);
                                    output.flush();
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
            } catch (IOException e) {
                System.err.println("Failed to communicate with the socket");
            }
        }
    }
}
