package ir.smmh.net.server.impl;

import ir.smmh.net.server.RequestHandler;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

public class RequestHandlerImpl extends Thread implements RequestHandler {

    private final Socket socket;
    private final Function<? super @NotNull String, @NotNull String> requestProcessor;

    public RequestHandlerImpl(@NotNull Socket socket, Function<? super @NotNull String, @NotNull String> requestProcessor) {
        super();
        this.socket = socket;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public final @NotNull String request(@NotNull String request) {
        return requestProcessor.apply(request);
    }

    @Override
    public final void run() {
        try {
            try (DataInputStream req = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
                try (DataOutputStream res = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                    try {
                        String request = req.readUTF();
                        try {
                            String response = request(request);
                            try {
                                res.writeUTF(response);
                            } catch (IOException e) {
                                System.err.println("Failed to write response");
                            }
                        } catch (Throwable e) {
                            System.err.println("Failed to process request");
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to read request");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to communicate with the socket");
        }
    }
}
