package ir.smmh.net.server;

import ir.smmh.net.api.API;
import org.jetbrains.annotations.NotNull;

public interface Server {

    @NotNull API getAPI();

    /**
     * Start listening for incoming connections on a
     * certain port, until stop() is called.
     */
    void start();

    int getPort();

    /**
     * Stop listening for new connections.
     */
    void stop();
}
