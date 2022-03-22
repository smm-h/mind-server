package ir.smmh.net.client.impl;

import ir.smmh.net.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientImpl implements Client {

    private final int port;
    private final String hostAddress;
    private final DataInputStream updateStream;

    public ClientImpl(int port, String hostAddress) {
        this.port = port;
        this.hostAddress = hostAddress;
        try {
            Socket socket = new Socket(hostAddress, port);
            this.updateStream = new DataInputStream(socket.getInputStream());
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
            return updateStream.readUTF();
        } catch (EOFException e) {
            return null;
        } catch (IOException e) {
            System.err.println("Could not read updates");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public @NotNull String sendRequest(@NotNull String request) {
        try (Socket s = new Socket(hostAddress, port)) {
            try (DataInputStream i = new DataInputStream(s.getInputStream())) {
                try (DataOutputStream o = new DataOutputStream(s.getOutputStream())) {
                    try {
                        o.writeUTF(request);
                        try {
                            return i.readUTF();
                        } catch (IOException e) {
                            throw new RuntimeException("Could not read response");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Could not write request");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not establish connection to send request");
        }
    }
}
