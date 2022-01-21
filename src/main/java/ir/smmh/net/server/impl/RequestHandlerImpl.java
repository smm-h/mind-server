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
        DataInputStream req;
        DataOutputStream res;
        try {
            req = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            res = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("Failed to communicate with the socket");
            return;
        }
        String request;
        try {
            request = req.readUTF();
            System.out.println(request);
            String response;
            try {
                response = request(request);
                System.out.println(response);
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
        try {
            res.close();
            req.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close socket");
        }
    }
}
