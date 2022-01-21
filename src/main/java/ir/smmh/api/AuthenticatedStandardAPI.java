package ir.smmh.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * A standard API is an API whose requests are JSON maps that contain the
 * 'method', 'parameters', and optionally 'authentication' keys, and whose
 * responses are JSON maps that contain the 'error_code', 'description', and
 * optionally 'results' keys.
 *
 * @param <A> An API-specific authenticated type, usually a log-in session
 */
public abstract class AuthenticatedStandardAPI<A> extends StandardAPI {

    public final int AUTHENTICATION_FAILED = defineError(12, "Authentication failed");

    public abstract A authenticate(JSONObject authentication);

    @SuppressWarnings("unchecked")
    public final @NotNull JSONObject processAuthenticatedMethod(@NotNull Method.AuthenticatedMethod<?> uncheckedMethod, @Nullable JSONObject authentication, @NotNull JSONObject parameters) {
        Method.AuthenticatedMethod<A> method;
        try {
            method = (Method.AuthenticatedMethod<A>) uncheckedMethod;
        } catch (ClassCastException e) {
            System.err.println("Failed to cast an unchecked authenticated method");
            return respond(BUG);
        }
        @Nullable A authenticated = authenticate(authentication);
        if (method instanceof Method.AuthenticationRequired) {
            Method.AuthenticationRequired<A> required = (Method.AuthenticationRequired<A>) method;
            if (authenticated == null)
                return respond(AUTHENTICATION_FAILED);
            return required.process(authenticated, parameters);
        } else if (method instanceof Method.AuthenticationOptional) {
            Method.AuthenticationOptional<A> optional = (Method.AuthenticationOptional<A>) method;
            return optional.process(authenticated, parameters);
        } else {
            System.err.println("You must not extend/implement Method.Authenticated directly");
            return respond(BUG);
        }
    }
}