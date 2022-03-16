package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Session<U extends User> {
    @NotNull U getUser();

    long getCreatedOn();

    @NotNull String getToken();

    @NotNull String getSessionId();
}
