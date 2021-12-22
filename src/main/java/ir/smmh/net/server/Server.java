package ir.smmh.net.server;

import ir.smmh.api.API;

public interface Server {

    API getAPI();

    int getDefaultPort();

    default void start() {
        start(getDefaultPort());
    }

    void start(int port);

    void stop();
}
