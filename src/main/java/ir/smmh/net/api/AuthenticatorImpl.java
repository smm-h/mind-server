package ir.smmh.net.api;

import ir.smmh.util.JSONUtil;
import ir.smmh.util.RandomUtil;
import ir.smmh.util.StringUtil;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public abstract class AuthenticatorImpl<U extends User, S extends Session<U>> implements Authenticator<U, S> {

    private final @NotNull PasswordHashFunction hash = createMessageDigest();
    /**
     * A map of usernames, mapped to a map of session IDs, mapped to a session
     */
    private final Map<String, Map<String, S>> sessions = new HashMap<>();
    private final ir.smmh.util.Map.SingleValue.Mutable<String, U> users = new MapImpl.SingleValue.Mutable<>();

    @Override
    public void define(StandardAPI api) {
        api.defineError(AUTHENTICATION_FAILED, "Authentication failed");
        api.defineError(USERNAME_EMPTY, "The username cannot be empty");
        api.defineError(USERNAME_DOES_NOT_EXIST, "The entered username does not match any accounts");
        api.defineError(USERNAME_ALREADY_EXISTS, "The entered username already exists");
        api.defineError(USERNAME_INVALID, "The entered username is not a valid username");
        api.defineError(PASSWORD_EMPTY, "The password cannot be empty");
        api.defineError(PASSWORD_TOO_WEAK, "The password is not secure enough");
        api.defineError(PASSWORD_INCORRECT, "The password you entered was incorrect");
        api.defineError(TOKEN_EMPTY, "The token is invalid");
        api.defineError(USER_NOT_FOUND, "User not found");
        api.defineError(SESSION_NOT_FOUND, "Session not found; it may been terminated or expired");
        api.defineError(SESSION_NOT_STRONG_ENOUGH, "Session could not terminate stronger session");
        api.defineMethod("sign_up", (Method.Plain) p -> api.errorCode(signUp(p.getString("username"), p.getString("password"))));
        api.defineMethod("sign_in", (Method.Plain) p -> api.errorCode(signIn(p.getString("username"), p.getString("password"), p.getString("token"))));
        api.defineMethod("list_users", (Method.Plain) p -> api.ok(new JSONObject().put("users", JSONUtil.toArray(users.overKeys()))));
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
    public @Nullable U findUser(String username) {
        return users.getAtPlace(username);
    }

    @Override
    public boolean doesUsernameExist(String username) {
        return users.containsPlace(username);
    }

    @Override
    public int signUp(String caseInsensitiveUsername, String password) {

        String username = caseInsensitiveUsername.toLowerCase();

        if (username.isEmpty() || StringUtil.isBlank(username))
            return USERNAME_EMPTY;

        if (password.isEmpty() || StringUtil.isBlank(password))
            return PASSWORD_EMPTY;

        if (isUsernameInvalid(username))
            return USERNAME_INVALID;

        if (doesUsernameExist(username))
            return USERNAME_ALREADY_EXISTS;

        if (!isPasswordStrongEnough(password))
            return PASSWORD_TOO_WEAK;

        addUser(createUser(username, hashPassword(password)));
        return NO_ERROR;
    }

    @Override
    public int signIn(String caseInsensitiveUsername, String password, String token) {

        String username = caseInsensitiveUsername.toLowerCase();

        if (username.isEmpty() || StringUtil.isBlank(username))
            return USERNAME_EMPTY;

        if (password.isEmpty() || StringUtil.isBlank(password))
            return PASSWORD_EMPTY;

        if (token.isEmpty() || StringUtil.isBlank(token))
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
    public void addUser(U user) {
        users.setAtPlace(user.getUsername(), user);
    }

    @Override
    public @Nullable U authenticate(JSONObject authentication) throws JSONException {
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
