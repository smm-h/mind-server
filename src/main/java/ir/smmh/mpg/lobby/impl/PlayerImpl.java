package ir.smmh.mpg.lobby.impl;

import ir.smmh.mpg.lobby.Player;
import ir.smmh.mpg.lobby.Room;
import ir.smmh.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerImpl implements Player {
    private final String username;
    private final String passwordHash;
    private final long joinedOn;
    private long lastOnlineOn;
    private Room room;

    public PlayerImpl(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        joinedOn = TimeUtil.now();
        lastOnlineOn = joinedOn;
    }

    @Override
    public void isOnlineNow() {
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

    @Override
    public long getJoinedOn() {
        return joinedOn;
    }

    @Override
    public long getLastOnlineOn() {
        return lastOnlineOn;
    }

    @Override
    public @Nullable Room getRoom() {
        return room;
    }

    /**
     * This should only be called from either Room::enter or Room::leave
     *
     * @param room Pass null for leaving
     */
    @Override
    public void setRoom(@Nullable Room room) {
        if (this.room != null && room != null) {
            if (this.room == room)
                throw new RuntimeException("Player already in this room");
            else throw new RuntimeException("Player already in another room");
        }
        this.room = room;
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
