package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Method {
    @FunctionalInterface
    interface Plain extends Method {
        @NotNull JSONObject process(@NotNull JSONObject parameters);
    }

    @FunctionalInterface
    interface AuthenticatedMethod<U> extends Method {
        @NotNull JSONObject process(@NotNull U user, @NotNull JSONObject parameters);

        default boolean isAuthenticationRequired() {
            return true;
        }
    }

    @FunctionalInterface
    interface OptionallyAuthenticatedMethod<U> extends AuthenticatedMethod<U> {
        @Override
        default boolean isAuthenticationRequired() {
            return false;
        }
    }
}
