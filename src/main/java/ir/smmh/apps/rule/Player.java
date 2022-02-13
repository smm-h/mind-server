package ir.smmh.apps.rule;

import ir.smmh.api.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Player extends User {

    @Override
    default @NotNull String getUsername() {
        return getId();
    }

    @NotNull String getId();

    @NotNull String getJoinedOn();

    @NotNull String getLastOnlineOn();

    @Nullable World getWorld();

    default void disconnect() {
        RuleAPI.getInstance().getSimulation().playerDisconnect(this);
    }
}
