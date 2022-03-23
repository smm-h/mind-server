package ir.smmh.net.server.impl;

import ir.smmh.net.api.StandardAPI;
import ir.smmh.net.server.StandardServer;
import org.jetbrains.annotations.NotNull;

public class StandardServerImpl extends ServerImpl implements StandardServer {

    public StandardServerImpl(StandardAPI api) {
        super(api);
    }

    @Override
    public @NotNull StandardAPI getAPI() {
        return (StandardAPI) super.getAPI();
    }
}
