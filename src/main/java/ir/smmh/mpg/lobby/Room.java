package ir.smmh.mpg.lobby;

import ir.smmh.net.api.StandardAPI;
import ir.smmh.net.server.ClientConnection;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanContain;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface Room extends StandardAPI, CanContain<Player> {

    int ALREADY_IN_THIS_ROOM = 407;
    int ALREADY_IN_ANOTHER_ROOM = 408;
    int WAS_NOT_IN_ROOM = 409;
    int WRONG_NUMBER_OF_PLAYERS = 500;
    int ROOM_LOCKED = 499;
    int GAME_IN_PROGRESS = 502;

    @NotNull Sequential<Map.Entry<Player, ClientConnection>> getMembers();

    int getPort();

    /**
     * Tries to add a player to the list of players in a room
     *
     * @param player Player to add
     * @return Error code
     */
    int enter(Player player, ClientConnection connection);

    int leave(Player player);

    int start();

    void lock();

    void unlock();

    void broadcast(String data);
}
