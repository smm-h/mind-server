package ir.smmh.net.server;

import java.net.Socket;

public interface SocketHandler {
    void handle(Socket socket);
}
