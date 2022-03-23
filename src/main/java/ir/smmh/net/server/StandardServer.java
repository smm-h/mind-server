package ir.smmh.net.server;

import ir.smmh.net.api.StandardAPI;
import org.jetbrains.annotations.NotNull;

/**
 * A standard server is a server that uses a standard API instead of any API
 */
public interface StandardServer extends Server {

    @Override
    @NotNull StandardAPI getAPI();
}
