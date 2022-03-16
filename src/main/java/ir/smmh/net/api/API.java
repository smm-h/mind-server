package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;

/**
 * Any object that can processJSON a given non-null string called a request in some
 * arbitrary way and return a non-null string as a response, without throwing any
 * errors or exceptions, is an API.
 */
public interface API {
    @NotNull
    default String sendRequest(@NotNull String request) {
        try {
            return process(request);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "";
        }
    }

    @NotNull String process(@NotNull String request);
}
