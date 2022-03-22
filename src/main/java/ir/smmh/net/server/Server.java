package ir.smmh.net.server;

import ir.smmh.net.api.API;
import org.jetbrains.annotations.NotNull;

public interface Server {

    @NotNull API getAPI();

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
