package ir.smmh.net.server;

import org.jetbrains.annotations.NotNull;

public interface RequestHandler extends Runnable {
    @NotNull String request(final @NotNull String request);
}
