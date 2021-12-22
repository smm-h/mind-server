package ir.smmh.net.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Client {

    int getDefaultPort();

    @Nullable
    String connectToHost(@NotNull String address, int port, @NotNull String request);

    @Nullable
    default String connectToHost(@NotNull String address, @NotNull String request) {
        return connectToHost(address, getDefaultPort(), request);
    }

    @Nullable
    default String connectToLocalHost(@NotNull String request) {
        return connectToHost("127.0.0.1", request);
    }
}
