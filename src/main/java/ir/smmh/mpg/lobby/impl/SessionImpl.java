package ir.smmh.mpg.lobby.impl;

import ir.smmh.mpg.lobby.Player;
import ir.smmh.mpg.lobby.Session;
import ir.smmh.util.TimeUtil;
import org.jetbrains.annotations.NotNull;

public class SessionImpl implements ir.smmh.net.api.Session<Player>, Session {

    private final @NotNull Player player;
    private final @NotNull String token;
    private final @NotNull String sessionId;
    private final long createdOn;

    public SessionImpl(Player player, String token, String sessionId) {
        this.player = player;
        this.token = token;
        this.sessionId = sessionId;
        this.createdOn = TimeUtil.now();
    }

    @Override
    public @NotNull Player getUser() {
        return player;
    }

    @Override
    public long getCreatedOn() {
        return createdOn;
    }

    @Override
    public @NotNull String getToken() {
        return token;
    }

    @NotNull
    @Override
    public String getSessionId() {
        return sessionId;
    }
}
