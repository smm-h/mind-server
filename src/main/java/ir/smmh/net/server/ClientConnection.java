package ir.smmh.net.server;

import java.io.IOException;

public interface ClientConnection {

    void close();

    boolean isClosed();

    void send(String data) throws IOException;

    String receive() throws IOException;
}
