package ir.smmh.net.server.impl;

import ir.smmh.net.server.ClientConnection;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;

public class ClientConnectionImpl implements ClientConnection {
    private final Socket socket;
    private final DataInputStream i;
    private final DataOutputStream o;
    private boolean manuallyClosed = false;

    public ClientConnectionImpl(@NotNull Socket socket) throws IOException {
        this.socket = socket;
        i = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        o = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public void close() {
        if (!manuallyClosed) {
            manuallyClosed = true;
            try {
                i.close();
            } catch (IOException ignored) {
            }
            try {
                o.close();
            } catch (IOException ignored) {
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public boolean isClosed() {
        return manuallyClosed || socket.isClosed();
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
}
