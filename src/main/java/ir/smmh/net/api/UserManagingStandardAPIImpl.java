package ir.smmh.net.api;

import ir.smmh.net.server.StandardServer;
import ir.smmh.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public abstract class UserManagingStandardAPIImpl<U extends User, S extends Session<U>> extends StandardAPIImpl implements UserManagingStandardAPI<U, S> {

    private final @NotNull PasswordHashFunction hash = createMessageDigest();
    /**
     * A map of usernames, mapped to a map of session IDs, mapped to a session
     */
    private final Map<String, Map<String, S>> sessions = new HashMap<>();

    @Override
    public void defineAll(StandardServer<?> server) {
        super.defineAll(server);
        defineError(AUTHENTICATION_FAILED, "Authentication failed");
        defineError(USERNAME_EMPTY, "The username cannot be empty");
        defineError(USERNAME_DOES_NOT_EXIST, "The entered username does not match any accounts");
        defineError(USERNAME_ALREADY_EXISTS, "The entered username already exists");
        defineError(USERNAME_INVALID, "The entered username is not a valid username");
        defineError(PASSWORD_EMPTY, "The password cannot be empty");
        defineError(PASSWORD_TOO_WEAK, "The password is not secure enough");
        defineError(PASSWORD_INCORRECT, "The password you entered was incorrect");
        defineError(TOKEN_EMPTY, "The token is invalid");
        defineError(USER_NOT_FOUND, "User not found");
        defineError(SESSION_NOT_FOUND, "Session not found; it may been terminated or expired");
        defineError(SESSION_NOT_STRONG_ENOUGH, "Session could not terminate stronger session");
    }

    @SuppressWarnings("unchecked")
    public final @NotNull JSONObject processAuthenticatedMethod(Method.AuthenticatedMethod<?> uncheckedMethod, @Nullable JSONObject authentication, JSONObject parameters) {
        Method.AuthenticatedMethod<U> method;
        try {
            method = (Method.AuthenticatedMethod<U>) uncheckedMethod;
        } catch (ClassCastException e) {
            System.err.println("Failed to cast an unchecked authenticated method");
            return notOk(BUG);
        }
        @Nullable U user = authentication == null ? null : authenticate(authentication);
        if (method.isAuthenticationRequired()) {
            return user == null ? notOk(AUTHENTICATION_FAILED) : method.process(user, parameters);
        } else {
            //noinspection ConstantConditions
            return method.process(user, parameters);
        }
    }

    @NotNull
    private PasswordHashFunction createMessageDigest() {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            return password -> new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("FAILED TO GET SHA-256; SERVER OPERATING WITH NO PASSWORD HASH");
            return password -> password;
        }
    }

    @Override
    public @NotNull String hashPassword(String password) {
        return hash.hash(password);
    }

    @Override
    public int signUp(String caseInsensitiveUsername, String password) {

        String username = caseInsensitiveUsername.toLowerCase();

        if (username.isEmpty() || username.isBlank())
            return USERNAME_EMPTY;

        if (password.isEmpty() || password.isBlank())
            return PASSWORD_EMPTY;

        if (isUsernameInvalid(username))
            return USERNAME_INVALID;

        if (doesUsernameExist(username))
            return USERNAME_ALREADY_EXISTS;

        if (!isPasswordStrongEnough(password))
            return PASSWORD_TOO_WEAK;

        createUser(username, hashPassword(password));
        return NO_ERROR;
    }

    @Override
    public int signIn(String caseInsensitiveUsername, String password, String token) {

        String username = caseInsensitiveUsername.toLowerCase();

        if (username.isEmpty() || username.isBlank())
            return USERNAME_EMPTY;

        if (password.isEmpty() || password.isBlank())
            return PASSWORD_EMPTY;

        if (token.isEmpty() || token.isBlank())
            return TOKEN_EMPTY;

        if (isUsernameInvalid(username))
            return USERNAME_INVALID;

        if (!doesUsernameExist(username))
            return USERNAME_DOES_NOT_EXIST;

        final U user = findUser(username);
        if (user == null)
            return USER_NOT_FOUND;

        if (!hashPassword(password).equals(user.getPasswordHash()))
            return PASSWORD_INCORRECT;

        addSession(user, token);
        return NO_ERROR;
    }

    @Override
    public void addSession(U user, String token) {
        Map<String, S> mySessions = sessions.computeIfAbsent(user.getUsername(), s -> new HashMap<>());
        String id = null;
        while (id == null || mySessions.containsKey(id)) {
            id = RandomUtil.generateRandomHex(8);
        }
        mySessions.put(id, createSession(user, token, id));
    }

    @Override
    public @NotNull JSONArray getSessions(U user) {
        JSONArray array = new JSONArray();
        Map<String, S> mySessions = sessions.computeIfAbsent(user.getUsername(), s -> new HashMap<>());
        for (String key : mySessions.keySet()) {
            array.put(key);
        }
        return array;
    }

    @Override
    public int terminateSession(U user, S currentSession, String toTerminate) {
        Map<String, S> mySessions = sessions.computeIfAbsent(user.getUsername(), s -> new HashMap<>());
        if (mySessions.containsKey(toTerminate)) {
            if (isSessionStronger(currentSession, mySessions.get(toTerminate))) {
                mySessions.remove(toTerminate);
                return NO_ERROR;
            } else {
                return SESSION_NOT_STRONG_ENOUGH;
            }
        } else {
            return SESSION_NOT_FOUND;
        }
    }

    @Override
    public @Nullable U authenticate(JSONObject authentication) {
        String username = authentication.getString("username");
        String token = authentication.getString("token");
        Map<String, S> tokens = sessions.get(username);
        if (tokens != null) {
            for (S session : tokens.values()) {
                if (session.getToken().equals(token)) {
                    return findUser(username);
                }
            }
        }
        return null;
    }
}
