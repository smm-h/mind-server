package ir.smmh.net.server.impl;

import ir.smmh.net.server.SocketHandler;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

public class SocketHandlerImpl implements SocketHandler {

    private final Function<String, String> requestProcessor;

    public SocketHandlerImpl(@NotNull Function<String, String> requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void handle(Socket socket) {
        try {
            DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            try {
                String request = input.readUTF();
                try {
                    String response = requestProcessor.apply(request);
                    try {
                        output.writeUTF(response);
                        output.flush();
                        output.close();
                        input.close();
                    } catch (IOException e) {
                        System.err.println("Failed to send response");
                    }
                } catch (Throwable e) {
                    System.err.println("Failed to process request");
                }
            } catch (IOException e) {
                System.err.println("Failed to receive request");
            }
        } catch (IOException e) {
            System.err.println("Failed to communicate with the socket");
            e.printStackTrace();
        }
    }
}
