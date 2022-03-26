package ir.smmh.net.client.impl;

import ir.smmh.net.client.Client;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientImpl implements Client {

    private final int port;
    private final String hostAddress;

    public ClientImpl(int port, String hostAddress) {
        this.port = port;
        this.hostAddress = hostAddress;
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
    public @NotNull String sendRequest(@NotNull String request) {
        try (Socket s = connect()) {
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
@Override
    public Socket connect() throws IOException {
        return new Socket(hostAddress, port);
    }
}
