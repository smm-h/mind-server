package ir.smmh.net.server;

import ir.smmh.api.API;

public interface Server {

    API getAPI();

    void start(int port);

    void stop();
}
