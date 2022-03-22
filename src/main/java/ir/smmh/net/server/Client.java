package ir.smmh.net.server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Client {

    @NotNull String getUniqueId();

    boolean isClosed();

    void send(String data) throws IOException;

    String receive() throws IOException;
}
