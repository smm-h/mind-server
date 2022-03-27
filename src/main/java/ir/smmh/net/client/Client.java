package ir.smmh.net.client;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

@SuppressWarnings("SameReturnValue")
public interface Client {

    int getPort();

    String getHostAddress();

    Socket connect() throws IOException;

    @NotNull String sendRequest(@NotNull String request);
}
