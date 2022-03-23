package ir.smmh.mpg.rule;

import ir.smmh.mpg.lobby.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface Simulation {

    @NotNull Iterable<String> getWorlds();

    @Nullable World findWorldById(String worldId);

    @NotNull World createWorld(JSONObject input) throws GameException;

    void deleteWorld(String worldId) throws GameException;

    @NotNull Iterable<String> getPlayerIds();

    @Nullable Player findPlayerById(String playerId);

    @NotNull Player playerConnect(JSONObject input) throws GameException;

    void playerDisconnect(Player player);
}
