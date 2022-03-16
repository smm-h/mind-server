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
    @Nullable
    public final String sendRequest(@NotNull String request) {
        try (Socket s = new Socket(hostAddress, port)) {
            try (DataInputStream i = new DataInputStream(s.getInputStream())) {
                try (DataOutputStream o = new DataOutputStream(s.getOutputStream())) {
                    try {
                        o.writeUTF(request);
                        try {
                            return i.readUTF();
                        } catch (IOException e) {
                            System.err.println("Could not read response");
                        }
                    } catch (IOException e) {
                        System.err.println("Could not write request");
                    }
                }
            }
        } catch (UnknownHostException u) {
            System.err.println("Could not find the host");
        } catch (IOException e) {
            System.err.println("Connection failed");
        }
        return null;
    }
}
