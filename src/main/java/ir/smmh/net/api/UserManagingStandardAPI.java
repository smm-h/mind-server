package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserManagingStandardAPI<U extends User, S extends Session<U>> extends StandardAPI {
    @NotNull JSONObject processAuthenticatedMethod(Method.AuthenticatedMethod<?> uncheckedMethod, @Nullable JSONObject authentication, JSONObject parameters);

    @NotNull String hashPassword(String password);

    int signUp(String username, String password);

    int signIn(String username, String password, String token);

    @Nullable U authenticate(JSONObject authentication);

    @Nullable U findUser(String username);

    @NotNull S createSession(U user, String token, String sessionId);

    @NotNull JSONArray getSessions(U user);

    void addSession(U user, String token);

    int terminateSession(U user, S currentSession, String toTerminate);

    void createUser(String username, String passwordHash);

    boolean doesUsernameExist(String username);

    boolean isPasswordStrongEnough(String username);

    boolean isUsernameInvalid(String username);

    boolean isSessionStronger(S current, S other);

    interface PasswordHashFunction {
        @NotNull String hash(String password);
    }
}
