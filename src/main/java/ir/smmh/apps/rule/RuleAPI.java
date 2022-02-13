package ir.smmh.apps.rule;

import ir.smmh.api.Method;
import ir.smmh.api.UserManagingJSONAPIImpl;
import ir.smmh.apps.rule.impl.PlayerImpl;
import ir.smmh.apps.rule.impl.Session;
import ir.smmh.apps.rule.impl.SimulationImpl;
import ir.smmh.net.server.impl.ServerImpl;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class RuleAPI extends UserManagingJSONAPIImpl<Player, Session> {

    private static RuleAPI instance;
    private final Map.SingleValue.Mutable<String, Player> players = new MapImpl.SingleValue.Mutable<>();
    private final Simulation simulation = new SimulationImpl();

    private RuleAPI() {
        defineMethod("sign_up",
                (Method.Plain) p -> respond(signUp(p.getString("username"), p.getString("password"))));
        defineMethod("sign_in",
                (Method.Plain) p -> respond(signIn(p.getString("username"), p.getString("password"), p.getString("token"))));
        defineMethod("list_users",
                (Method.Plain) p -> respond(new JSONObject().put("players", JSONUtil.toArray(players.overKeys()))));
    }

    public static void main(String[] args) {
        new ServerImpl(RuleAPI.getInstance()).start(7000);
    }

    public static RuleAPI getInstance() {
        return instance == null ? (instance = new RuleAPI()) : instance;
    }

    @Override
    public @Nullable Player findUser(String username) {
        return players.getAtPlace(username);
    }

    @Override
    public @NotNull Session createSession(Player player, String token, String sessionId) {
        return new Session(player, token, sessionId);
    }

    @Override
    public void createUser(String username, String passwordHash) {
        players.setAtPlace(username, new PlayerImpl(username, passwordHash));
    }

    @Override
    public boolean doesUsernameExist(String username) {
        return players.containsPlace(username);
    }

    @Override
    public boolean isPasswordStrongEnough(String username) {
        return true;
    }

    @Override
    public boolean isUsernameInvalid(String username) {
        return false;
    }

    @Override
    public boolean isSessionStronger(Session current, Session other) {
        return true;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
