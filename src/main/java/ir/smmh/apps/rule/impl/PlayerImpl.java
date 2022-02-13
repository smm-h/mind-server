package ir.smmh.apps.rule.impl;

import ir.smmh.apps.rule.Player;
import ir.smmh.apps.rule.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerImpl implements Player {
    private final String id;
    private final String joinedOn;
    private World world;
    private String lastOnlineOn;
    private final String passwordHash;

    public PlayerImpl(String id, String passwordHash) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.joinedOn = "joinedOn";
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getJoinedOn() {
        return joinedOn;
    }

    @Override
    public @NotNull String getLastOnlineOn() {
        return lastOnlineOn;
    }

    @Override
    public @Nullable World getWorld() {
        return world;
    }

    @Override
    public @NotNull String getPasswordHash() {
        return passwordHash;
    }
}
