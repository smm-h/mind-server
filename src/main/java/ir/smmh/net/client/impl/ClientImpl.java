package ir.smmh.net.client.impl;

import ir.smmh.net.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientImpl implements Client {

    private final int port;
    private final String hostAddress;
    private final DataInputStream input;
    private final DataOutputStream output;

    public ClientImpl(int port, String hostAddress) {
        this.port = port;
        try {
            this.hostAddress = hostAddress;
            Socket socket = new Socket(hostAddress, port);
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            throw new RuntimeException("Could not find the host");
        } catch (IOException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public final int getPort() {
        return port;
    }

    @Override
    public final String getHostAddress() {
        return hostAddress;
    }

    @Override
    public @Nullable String getUpdates() {
        try {
            return input.readUTF();
        } catch (IOException e) {
            System.err.println("Could not read updates");
        }
        return null;
    }

    @Override
    public @Nullable String sendRequest(@NotNull String request) {
        try {
            output.writeUTF(request);
            try {
                return input.readUTF();
            } catch (IOException e) {
                System.err.println("Could not read response");
            }
        } catch (IOException e) {
            System.err.println("Could not write request");
        }
        return null;
    }
}
