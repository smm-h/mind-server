package ir.smmh.net.server.impl;

import ir.smmh.net.api.StandardAPI;
import ir.smmh.net.server.StandardServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class StandardServerImpl extends ServerImpl implements StandardServer {

    public StandardServerImpl(StandardAPI api, int port) throws IOException {
        super(api, port);
    }

    public StandardServerImpl(StandardAPI api) throws IOException {
        super(api);
    }

    @Override
    public @NotNull StandardAPI getAPI() {
        return (StandardAPI) super.getAPI();
    }
}
