package ir.smmh.net.server.impl;

import ir.smmh.net.server.Client;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;

public class ClientImpl implements Client {
    private final Socket socket;
    private final String uniqueId;
    private final DataInputStream i;
    private final DataOutputStream o;

    public ClientImpl(@NotNull Socket socket, @NotNull String uniqueId) throws IOException {
        this.socket = socket;
        this.uniqueId = uniqueId;
        i = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        o = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public @NotNull String getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void send(String data) throws IOException {
        o.writeUTF(data);
        o.flush(); // TODO is this necessary?
    }

    @Override
    public String receive() throws IOException {
        return i.readUTF();
    }

    @Override
    public String toString() {
        return "Connection@" + uniqueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client that = (Client) o;
        return uniqueId.equals(that.getUniqueId());
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }
}
