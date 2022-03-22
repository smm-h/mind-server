package ir.smmh.net.server;

import java.io.IOException;

/**
 * A standard server is a server that A) uses a standard API instead of any API,
 * and B) can keep track of its clients in a thread-safe manner and broadcast
 * data to them whenever necessary. Disconnected clients (closed sockets) are
 * automatically removed, so you only need to worry about adding them.
 */
public interface StandardServer<C extends Client> extends Server {

    void addConnection(C connection);

    int getConnectionCount();

    void forEach(ConnectionAction<C> action);

    void broadcast(String data);

    @FunctionalInterface
    interface ConnectionAction<C extends Client> {
        void accept(C connection) throws IOException;
    }
}
