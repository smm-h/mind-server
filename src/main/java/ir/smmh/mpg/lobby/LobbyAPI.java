package ir.smmh.mpg.lobby;


import ir.smmh.net.api.Method;
import ir.smmh.net.api.UserManagingStandardAPIImpl;
import ir.smmh.net.server.ClientConnectionThread;
import ir.smmh.net.server.impl.StandardServerImpl;
import ir.smmh.util.Map;
import ir.smmh.util.RandomUtil;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import static ir.smmh.util.JSONUtil.map;

public class LobbyAPI extends UserManagingStandardAPIImpl<Player, Session> {

    private static LobbyAPI instance;
    private final Map.SingleValue.Mutable<String, GameStarter> gameStarters = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Game> games = new MapImpl.SingleValue.Mutable<>();
    private LobbyAPI() {
        defineMethod("host", (Method.Authenticated<Player>) (player, parameters) -> {

            if (games.getSize() > 20) return notOk("Server too busy");

            // find game starter
            String gameName = parameters.getString("game_name");
            GameStarter gameStarter = gameStarters.getAtPlace(gameName);
            if (gameStarter == null) return notOk("No such game");

            // start a new game
            JSONObject gameSettings = parameters.getJSONObject("game_settings");
            Game game = gameStarter.startNew(gameSettings);
            if (game == null) return notOk("Inappropriate settings");

            // return game id
            int tries = 0;
            String gameId = null;
            while (gameId == null || games.containsPlace(gameId)) {
                gameId = RandomUtil.generateRandomHex(16);
                tries++;
                if (tries >= 3) {
                    return notOk("Please try again later");
                }
            }
            games.setAtPlace(gameId, game);
            player.setConnection(((ClientConnectionThread) Thread.currentThread()).getConnection());
            return ok(map("game_id", gameId));
        });
        defineMethod("join", (Method.Authenticated<Player>) (player, parameters) -> {

            // find and join a game
            String gameId = parameters.getString("game_id");
            Game game = games.getAtPlace(gameId);
            if (game == null) return notOk("No such game");

            player.setConnection(((ClientConnectionThread) Thread.currentThread()).getConnection());
            return maybeOk(game.join(player));
        });
    }

    public static void main(String[] args) {
        new StandardServerImpl(LobbyAPI.getInstance()).start(7001);
    }

    public static LobbyAPI getInstance() {
        return instance == null ? (instance = new LobbyAPI()) : instance;
    }

    @Override
    public @NotNull Session createSession(Player user, String token, String sessionId) {
        return new Session(user, token, sessionId);
    }

    @Override
    public @Nullable Player createUser(String username, String passwordHash) {
        return new Player(username, passwordHash);
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
}
