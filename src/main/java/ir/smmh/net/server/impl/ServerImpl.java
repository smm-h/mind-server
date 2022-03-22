package ir.smmh.net.server.impl;

import ir.smmh.net.api.API;
import ir.smmh.net.server.Server;
import ir.smmh.net.server.SocketHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public class ServerImpl implements Server {

    private final API api;
    private final Supplier<SocketHandler> connectionHandlerSupplier;
    private boolean running;

    public ServerImpl(API api) {
        this.api = api;
        this.connectionHandlerSupplier = () -> new SocketHandlerImpl(api::sendRequest);
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
                SocketHandler socketHandler = connectionHandlerSupplier.get();
                new Thread(() -> socketHandler.handle(socket)).start();
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
}