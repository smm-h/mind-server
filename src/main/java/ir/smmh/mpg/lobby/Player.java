package ir.smmh.mpg.lobby;

import ir.smmh.net.api.User;
import ir.smmh.net.server.ClientConnection;
import ir.smmh.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Player implements User {
    private final String username;
    private final String passwordHash;
    private final long joinedOn;
    private long lastOnlineOn;
    private @Nullable ClientConnection connection;

    public Player(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        joinedOn = TimeUtil.now();
        lastOnlineOn = joinedOn;
    }

    public @Nullable ClientConnection getConnection() {
        return connection;
    }

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
        lastOnlineOn = TimeUtil.now();
    }

    @NotNull
    @Override
    public String getUsername() {
        return username;
    }

    public @NotNull String getPasswordHash() {
        return passwordHash;
    }

    public long getJoinedOn() {
        return joinedOn;
    }

    public long getLastOnlineOn() {
        return lastOnlineOn;
    }

    @Override
    public String toString() {
        return "Player@" + username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player that = (Player) o;
        return username.equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
