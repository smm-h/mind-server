package ir.smmh.net.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("SameReturnValue")
public interface Client {

    int getPort();

    String getHostAddress();

    @Nullable String getUpdates();

    @Nullable String sendRequest(@NotNull String request);
}
