package ir.smmh.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A standard API is an API whose requests are JSON maps that contain the
 * 'method', 'parameters', and optionally 'authentication' keys, and whose
 * responses are JSON maps that contain the 'error_code', 'description', and
 * optionally 'results' keys.
 */
@SuppressWarnings({"NonConstantFieldWithUpperCaseName", "UseOfSystemOutOrSystemErr"})
public abstract class StandardAPI implements API {

    private final Map<String, Method> methods = new HashMap<>(16);
    private final Map<Integer, String> errorCodes = new HashMap<>(16);
    public final int BUG = defineError(-1, "Internal bug encountered");
    public final int NO_ERROR = defineError(0, "Successful");
    public final int COULD_NOT_PARSE_REQUEST = defineError(1, "Missing keys or bad values in request");
    public final int METHOD_NOT_FOUND = defineError(2, "Method not found");
    public final int UNEXPECTED_ERROR = defineError(99, "Unexpected error occurred");

    protected final void defineMethod(@NotNull String name, @NotNull Method method) {
        if (methods.containsKey(name)) {
            throw new IllegalArgumentException("method name already exists: " + name);
        } else {
            methods.put(name, method);
        }
    }

    // TODO defineError should not take an int
    protected final int defineError(int code, @NotNull String description) {
        if (errorCodes.containsKey(code)) {
            throw new IllegalArgumentException("error code already exists: " + code);
        } else {
            errorCodes.put(code, description);
        }
        return code;
    }

    @NotNull
    public final JSONObject respond(int errorCode) {
        return respond(errorCode, null, null);
    }

    @NotNull
    public final JSONObject respond(int errorCode, Throwable thrown) {
        return respond(errorCode, thrown, null);
    }

    @NotNull
    public final JSONObject respond(JSONObject results) {
        return respond(NO_ERROR, null, results);
    }

    @NotNull
    private JSONObject respond(int errorCode, Throwable thrown, JSONObject results) {
        JSONObject response = new JSONObject();
        try {
            response.put("error_code", errorCode);
            response.put("description", errorCodes.get(errorCode));
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
    public final @NotNull String process(@NotNull String request) {
        return processJSON(request).toString();
    }

    @NotNull
    public final JSONObject processJSON(@NotNull String request) {
        System.out.println("\n>>> " + request);
        JSONObject response;
        PrintStream log;
        try {
            response = processJSON(new JSONObject(new JSONTokener(request)));
            log = response.getInt("error_code") == 0 ? System.out : System.err;
        } catch (JSONException e) {
            response = respond(COULD_NOT_PARSE_REQUEST, e);
            log = System.err;
        }
        log.println("=== " + response);
        return response;
    }

    @NotNull
    private JSONObject processJSON(@NotNull JSONObject request) {
        try {
            String methodName;
            try {
                methodName = request.getString("method");
            } catch (JSONException e) {
                return respond(COULD_NOT_PARSE_REQUEST, e);
            }
            Method method = methods.get(methodName);
            if (method == null) {
                return respond(METHOD_NOT_FOUND);
            } else {
                @NotNull JSONObject parameters;
                try {
                    parameters = request.getJSONObject("parameters");
                } catch (JSONException e) {
                    return respond(COULD_NOT_PARSE_REQUEST, e);
                }
                if (method instanceof Method.AuthenticatedMethod) {
                    @SuppressWarnings("rawtypes") Method.AuthenticatedMethod am = (Method.AuthenticatedMethod) method;
                    if (this instanceof AuthenticatedStandardAPI) {
                        AuthenticatedStandardAPI<?> me = ((AuthenticatedStandardAPI<?>) this);
                        @Nullable JSONObject authentication;
                        try {
                            authentication = request.has("authentication") ? request.getJSONObject("authentication") : null;
                        } catch (JSONException e) {
                            return respond(COULD_NOT_PARSE_REQUEST, e);
                        }
                        return me.processAuthenticatedMethod(am, authentication, parameters);
                    } else {
                        System.err.println("You can have authenticated methods only within an authenticated API");
                        return respond(BUG);
                    }
                } else if (method instanceof Method.Plain) {
                    return ((Method.Plain) method).process(parameters);
                } else {
                    System.err.println("You must not extend/implement Method directly");
                    return respond(BUG);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return respond(UNEXPECTED_ERROR, throwable);
        }
    }
}
