package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;

public interface User {

    void isOnlineNow();

    @NotNull String getUsername();

    @NotNull String getPasswordHash();
}
