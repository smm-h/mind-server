package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a join request sent to a chat.
 */
public interface ChatJoinRequest extends UpdateContent {
    /**
     * Chat to which the request was sent
     */
    @NotNull Chat chat();

    /**
     * User that sent the join request
     */
    @NotNull User from();

    /**
     * Date the request was sent in Unix time
     */
    int date();

    /**
     * Bio of the user.
     */
    @Nullable String bio();

    /**
     * Chat invite link that was used by the user to send the join
     * request
     */
    @Nullable ChatInviteLink invite_link();
}
