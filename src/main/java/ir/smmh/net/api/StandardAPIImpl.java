package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class StandardAPIImpl implements StandardAPI {

    private final Map<String, Method> methods = new HashMap<>();
    private final Map<Integer, String> errorCodes = new HashMap<>();
    private final Authenticator<?, ?> authenticator;

    public StandardAPIImpl() {
        this(null);
    }

    @Override
    public @Nullable Authenticator<?, ?> getAuthenticator() {
        return authenticator;
    }

    public StandardAPIImpl(@Nullable Authenticator<?, ?> authenticator) {
        this.authenticator = authenticator;
        defineError(NO_ERROR, "Successful");
        defineError(COULD_NOT_PARSE_REQUEST, "Missing keys or bad values in request");
        defineError(METHOD_NOT_FOUND, "Method not found");
        defineError(UNEXPECTED_ERROR, "Unexpected error occurred");
        defineError(BUG, "Internal bug encountered");
        defineMethod("methods", (Method.Plain) parameters -> {
            JSONArray array = new JSONArray();
            for (String i : methods.keySet()) {
                array.put(i);
            }
            return ok("methods", array);
        });
        if (authenticator != null) authenticator.define(this);
    }

    @Override
    public final void defineMethod(String name, Method method) {
        if (methods.containsKey(name)) {
            throw new IllegalArgumentException("method name already exists: " + name);
        } else {
            methods.put(name, method);
        }
    }

    @Override
    public final void defineError(int errorCode, String description) {
        errorCodes.put(errorCode, description);
    }

    @Override
    public final @NotNull JSONObject notOk(String errorDescription) {
        return makeResponse(-1, errorDescription, null, null);
    }

    @Override
    public final @NotNull JSONObject notOk(int errorCode, Throwable throwable) {
        assert errorCode != NO_ERROR;
        return makeResponse(errorCode, errorCodes.get(errorCode), throwable, null);
    }

    @Override
    public final @NotNull JSONObject errorCode(int errorCode) {
        return makeResponse(errorCode, errorCodes.get(errorCode), null, null);
    }

    @Override
    public final @NotNull JSONObject ok() {
        return makeResponse(NO_ERROR, null, null, null);
    }

    @Override
    public final @NotNull JSONObject ok(JSONObject results) {
        return makeResponse(NO_ERROR, null, null, results);
    }

    @Override
    public final @NotNull JSONObject ok(String key, Object value) {
        return makeResponse(NO_ERROR, null, null, new JSONObject().put(key, value));
    }

    private @NotNull JSONObject makeResponse(int error_code, @Nullable String errorDescription, @Nullable Throwable thrown, @Nullable JSONObject results) {
        JSONObject response = new JSONObject();
        try {
            boolean ok = error_code == NO_ERROR;
            response.put("ok", ok);
            if (!ok) {
                if (error_code != -1) {
                    response.put("error_code", error_code);
                }
                if (errorDescription != null) {
                    response.put("error_description", errorDescription);
                }
            }
            if (thrown != null) {
                response.put("error_message", thrown.getMessage());
                //            response.put("error_stack_trace", Arrays.toString(thrown.getStackTrace()));
            }
            if (results != null) {
                response.put("results", results);
            }
        } catch (JSONException ignored) {
        }
        return response;
    }

    @Override
    public final @NotNull JSONObject processJSON(String request) {
        System.out.println("\n>>> " + request);
        JSONObject response;
        PrintStream log;
        try {
            response = processJSON(new JSONObject(new JSONTokener(request)));
            log = response.getBoolean("ok") ? System.out : System.err;
        } catch (JSONException e) {
            response = notOk(COULD_NOT_PARSE_REQUEST, e);
            log = System.err;
        }
        log.println("=== " + response);
        return response;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    private @NotNull JSONObject processJSON(JSONObject request) {
        try {
            String methodName;
            try {
                methodName = request.getString("method");
            } catch (JSONException e) {
                return notOk(COULD_NOT_PARSE_REQUEST, e);
            }
            Method method = methods.get(methodName);
            if (method == null) {
                return errorCode(METHOD_NOT_FOUND);
            } else {
                @NotNull JSONObject parameters;
                try {
                    parameters = request.getJSONObject("parameters");
                } catch (JSONException e) {
                    return notOk(COULD_NOT_PARSE_REQUEST, e);
                }
                if (method instanceof Method.Authenticated) {
                    Method.Authenticated am = (Method.Authenticated) method;
                    if (this.authenticator != null) {
                        @Nullable JSONObject authentication;
                        try {
                            authentication = request.optJSONObject("authentication", null);
                        } catch (JSONException e) {
                            return notOk(COULD_NOT_PARSE_REQUEST, e);
                        }
                        @Nullable var user = authentication == null ? null : authenticator.authenticate(authentication);
                        if (am.isAuthenticationRequired())
                            if (user == null) return errorCode(Authenticator.AUTHENTICATION_FAILED);
                        if (user != null) user.isOnlineNow();
                        return am.process(user, parameters);
                    } else {
                        System.err.println("You cannot have authenticated methods without an authenticator");
                        return errorCode(BUG);
                    }
                } else if (method instanceof Method.Plain) {
                    return ((Method.Plain) method).process(parameters);
                } else {
                    System.err.println("You must not extend/implement Method directly");
                    return errorCode(BUG);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return notOk(UNEXPECTED_ERROR, throwable);
        }
    }
}
