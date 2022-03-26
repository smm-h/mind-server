package ir.smmh.mpg.lobby;

import ir.smmh.net.api.User;
import org.jetbrains.annotations.Nullable;

public interface Player extends User {
    long getJoinedOn();

    long getLastOnlineOn();

    @Nullable Room getRoom();

    void setRoom(@Nullable Room room);
}
