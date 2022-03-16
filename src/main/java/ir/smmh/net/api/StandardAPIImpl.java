package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"NonConstantFieldWithUpperCaseName", "UseOfSystemOutOrSystemErr"})
@ParametersAreNonnullByDefault
public class StandardAPIImpl implements StandardAPI {

    private final Map<String, Method> methods = new HashMap<>(16);
    private final Map<Integer, String> errorCodes = new HashMap<>(16);
    private int lastErrorCode = 0;
    public final int BUG = defineError("Internal bug encountered");
    public final int NO_ERROR = defineError("Successful");
    public final int COULD_NOT_PARSE_REQUEST = defineError("Missing keys or bad values in request");
    public final int METHOD_NOT_FOUND = defineError("Method not found");
    public final int UNEXPECTED_ERROR = defineError("Unexpected error occurred");

    @Override
    public final void defineMethod(String name, Method method) {
        if (methods.containsKey(name)) {
            throw new IllegalArgumentException("method name already exists: " + name);
        } else {
            methods.put(name, method);
        }
    }

    @Override
    public final int defineError(String description) {
        int code = lastErrorCode++;
        errorCodes.put(code, description);
        return code;
    }

    @Override
    public final @NotNull JSONObject notOk(String errorDescription) {
        return makeResponse(-1, errorDescription, null, null);
    }

    @Override
    public final @NotNull JSONObject notOk(int errorCode) {
        return makeResponse(errorCode, errorCodes.get(errorCode), null, null);
    }

    @Override
    public final @NotNull JSONObject notOk(int errorCode, Throwable thrown) {
        return makeResponse(errorCode, errorCodes.get(errorCode), thrown, null);
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
                return notOk(METHOD_NOT_FOUND);
            } else {
                @NotNull JSONObject parameters;
                try {
                    parameters = request.getJSONObject("parameters");
                } catch (JSONException e) {
                    return notOk(COULD_NOT_PARSE_REQUEST, e);
                }
                if (method instanceof Method.AuthenticatedMethod) {
                    @SuppressWarnings("rawtypes") Method.AuthenticatedMethod am = (Method.AuthenticatedMethod) method;
                    if (this instanceof UserManagingStandardAPIImpl) {
                        UserManagingStandardAPIImpl<?, ?> me = ((UserManagingStandardAPIImpl<?, ?>) this);
                        @Nullable JSONObject authentication;
                        try {
                            authentication = request.optJSONObject("authentication", null);
                        } catch (JSONException e) {
                            return notOk(COULD_NOT_PARSE_REQUEST, e);
                        }
                        return me.processAuthenticatedMethod(am, authentication, parameters);
                    } else {
                        System.err.println("You can have authenticated methods only within an authenticated API");
                        return notOk(BUG);
                    }
                } else if (method instanceof Method.Plain) {
                    return ((Method.Plain) method).process(parameters);
                } else {
                    System.err.println("You must not extend/implement Method directly");
                    return notOk(BUG);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return notOk(UNEXPECTED_ERROR, throwable);
        }
    }
}
