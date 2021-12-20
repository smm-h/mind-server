package ir.smmh.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface Method {
    interface Plain extends Method {
        @NotNull JSONObject process(@NotNull JSONObject parameters);
    }

    interface AuthenticatedMethod<A> {
    }

    interface AuthenticationRequired<A> extends AuthenticatedMethod<A> {
        @NotNull JSONObject process(@NotNull A authenticated, @NotNull JSONObject parameters);
    }

    interface AuthenticationOptional<A> extends AuthenticatedMethod<A> {
        @NotNull JSONObject process(@Nullable A authenticated, @NotNull JSONObject parameters);
    }
}
