package ir.smmh.mpg.rule.impl;

import ir.smmh.mpg.lobby.LobbyAPI;
import ir.smmh.mpg.lobby.Player;
import ir.smmh.mpg.rule.GameException;
import ir.smmh.mpg.rule.Simulation;
import ir.smmh.mpg.rule.World;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class SimulationImpl implements Simulation {

    private final Map.SingleValue.Mutable<String, World> worlds = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Player> players = new MapImpl.SingleValue.Mutable<>();

    @Override
    public @NotNull Iterable<String> getWorlds() {
        return worlds.overKeys();
    }

    @Override
    public @Nullable World findWorldById(String worldId) {
        return worlds.getAtPlace(worldId);
    }

    @Override
    public @NotNull World createWorld(JSONObject input) throws GameException {
        final String id;
        final int width, height;
        try {
            id = input.getString("id");
            width = input.getInt("width");
            height = input.getInt("height");
        } catch (JSONException e) {
            throw new GameException(e);
        }
        try {
            World world = new WorldImpl(id, width, height, null);
            worlds.setAtPlace(id, world);
            return world;
        } catch (IllegalArgumentException e) {
            throw new GameException(e);
        }
    }

    @Override
    public void deleteWorld(String worldId) throws GameException {
        if (worlds.containsPlace(worldId)) {
            worlds.removeAtPlace(worldId);
        } else {
            throw new GameException("no such world: " + worldId);
        }
    }

    @Override
    public @NotNull Iterable<String> getPlayerIds() {
        return players.overKeys();
    }

    @Override
    public @Nullable Player findPlayerById(String playerId) {
        return players.getAtPlace(playerId);
    }

    @Override
    public @NotNull Player playerConnect(JSONObject input) throws GameException {
        final String id;
        final String worldId;
        try {
            id = input.getString("id");
            worldId = input.getString("worldId");
        } catch (JSONException e) {
            throw new GameException(e);
        }
        World world = findWorldById(worldId);
        if (world == null)
            throw new GameException("world not found: " + worldId);
        Player player = LobbyAPI.getInstance().findUser(id);
        if (player == null)
            throw new GameException("player not found: " + id);
        players.setAtPlace(player.getUsername(), player);
        return player;
    }

    @Override
    public void playerDisconnect(Player player) {
        players.removeAtPlace(player.getUsername());
    }
}
