package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public interface Method {
    @FunctionalInterface
    interface Plain extends Method {
        @NotNull JSONObject process(@NotNull JSONObject parameters) throws JSONException;
    }

    @FunctionalInterface
    interface Authenticated<U> extends Method {
        @NotNull JSONObject process(@NotNull U user, @NotNull JSONObject parameters) throws JSONException;

        default boolean isAuthenticationRequired() {
            return true;
        }

        @FunctionalInterface
        interface Optionally<U> extends Authenticated<U> {
            @Override
            default boolean isAuthenticationRequired() {
                return false;
            }
        }
    }
}
