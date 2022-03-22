package ir.smmh.net.server;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface Server {
    /**
     * Start listening for incoming connections on a
     * certain port, until stop() is called.
     *
     * @param port Port number to listen on
     */
    void start(int port);

    /**
     * Stop listening for new connections.
     */
    void stop();
}
