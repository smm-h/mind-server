package ir.smmh.mpg.lobby.impl;

import ir.smmh.mpg.lobby.LobbyAPI;
import ir.smmh.mpg.lobby.Player;
import ir.smmh.mpg.lobby.Room;
import ir.smmh.net.api.Method;
import ir.smmh.net.api.StandardAPIImpl;
import ir.smmh.net.server.ClientConnection;
import ir.smmh.net.server.ClientConnectionThread;
import ir.smmh.net.server.Server;
import ir.smmh.net.server.impl.StandardServerImpl;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import static ir.smmh.util.JSONUtil.map;

public abstract class RoomImpl extends StandardAPIImpl implements Room {
    private final Player creator;
    private final Map<Player, ClientConnection> members = Collections.synchronizedMap(new HashMap<>());
    private final Server server;
    private boolean locked;

    public RoomImpl(Player creator) throws IOException {
        super(LobbyAPI.getInstance().getAuthenticator());
        this.creator = creator;
        defineMethod("reconnect", (Method.Authenticated<Player>) (user, parameters) -> {
            if (members.containsKey(user)) {
                ClientConnection prev = members.put(user, ((ClientConnectionThread) Thread.currentThread()).getConnection());
                if (prev != null) prev.close();
                return ok();
            } else {
                return notOk("You are not in this room");
            }
        });
        server = new StandardServerImpl(this);
        new Thread(server::start).start();
    }

    public @NotNull Sequential<Map.Entry<Player, ClientConnection>> getMembers() {
        var s = new SequentialImpl<Map.Entry<Player, ClientConnection>>();
        synchronized (members) {
            s.addAll(members.entrySet());
        }
        return s;
    }

    @Override
    public int getPort() {
        return server.getPort();
    }

    @Override
    public int enter(Player player, ClientConnection connection) {
        if (locked)
            return ROOM_LOCKED;
        Room playerRoom = player.getRoom();
        if (playerRoom == null) {
            player.setRoom(this);
            members.put(player, connection);
            return NO_ERROR;
        } else {
            if (playerRoom == this) {
                if (contains(player)) {
                    return ALREADY_IN_THIS_ROOM;
                } else {
                    return BUG;
                }
            } else {
                return ALREADY_IN_ANOTHER_ROOM;
            }
        }
    }

    @Override
    public int leave(Player player) {
        if (locked)
            return ROOM_LOCKED;
        Room playerRoom = player.getRoom();
        ClientConnection connection = members.remove(player);
        if (playerRoom == this) {
            player.setRoom(null);
            if (connection == null) {
                return BUG;
            } else {
                if (!connection.isClosed()) {
                    connection.close();
                }
                System.out.println(player.getUsername() + " left the room");
                return NO_ERROR;
            }
        } else {
            return WAS_NOT_IN_ROOM;
        }
    }

    @Override
    public void lock() {
        locked = true;
    }

    @Override
    public void unlock() {
        locked = false;
    }

    @Override
    public void broadcast(String data) {
        Set<Player> closed = new HashSet<>();
        int failures = 0;
        synchronized (members) {
            for (Player player : members.keySet()) {
                ClientConnection c = members.get(player);
                try {
                    c.send(data);
                } catch (IOException e) {
                    if (c.isClosed()) {
                        closed.add(player);
                    } else {
                        failures++;
                    }
                }
            }
            if (!closed.isEmpty()) {
                for (Player player : closed) {
                    leave(player);
                }
            }
        }
        if (failures > 0) {
            System.err.println("Failed to broadcast to: " + failures + " player(s)");
        }
        for (Player player : closed) {
            broadcast(map(
                    "update", "player_left",
                    "player_id", player.getUsername()
            ).toString());
        }
    }

    @Override
    public int getSize() {
        return members.size();
    }

    @Override
    public boolean contains(Player toCheck) {
        return members.containsKey(toCheck);
    }
}
