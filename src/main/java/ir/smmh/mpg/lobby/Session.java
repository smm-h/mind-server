package ir.smmh.mpg.lobby;

import org.jetbrains.annotations.NotNull;

public class Session implements ir.smmh.net.api.Session<Player> {

    private final @NotNull Player player;
    private final @NotNull String token;
    private final @NotNull String sessionId;
    private final long createdOn;

    public Session(Player player, String token, String sessionId) {
        this.player = player;
        this.token = token;
        this.sessionId = sessionId;
        this.createdOn = -1; // TODO now
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
