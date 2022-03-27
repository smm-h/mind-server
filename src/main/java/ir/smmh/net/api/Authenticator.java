package ir.smmh.net.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Authenticator<U extends User, S extends Session<U>> {

    int NO_ERROR = StandardAPI.NO_ERROR;
    int AUTHENTICATION_FAILED = 100;
    int USERNAME_EMPTY = 101;
    int USERNAME_DOES_NOT_EXIST = 102;
    int USERNAME_ALREADY_EXISTS = 103;
    int USERNAME_INVALID = 104;
    int PASSWORD_EMPTY = 105;
    int PASSWORD_TOO_WEAK = 106;
    int PASSWORD_INCORRECT = 107;
    int TOKEN_EMPTY = 108;
    int USER_NOT_FOUND = 200;
    int SESSION_NOT_FOUND = 201;
    int SESSION_NOT_STRONG_ENOUGH = 202;

    void define(StandardAPI api);

    @NotNull String hashPassword(String password);

    int signUp(String username, String password);

    int signIn(String username, String password, String token);

    @Nullable U authenticate(JSONObject authentication) throws JSONException;

    @Nullable U findUser(String username);

    @NotNull S createSession(U user, String token, String sessionId);

    @NotNull JSONArray getSessions(U user);

    void addSession(U user, String token);

    int terminateSession(U user, S currentSession, String toTerminate);

    @NotNull U createUser(String username, String passwordHash);

    void addUser(U user);

    boolean doesUsernameExist(String username);

    boolean isPasswordStrongEnough(String username);

    boolean isUsernameInvalid(String username);

    boolean isSessionStronger(S current, S other);

    interface PasswordHashFunction {
        @NotNull String hash(String password);
    }
}
