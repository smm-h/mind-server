package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This object represents changes in the status of a chat member.
 */
public interface ChatMemberUpdated extends UpdateContent {
    /**
     * Chat the user belongs to
     */
    @NotNull Chat chat();

    /**
     * Performer of the action, which resulted in the change
     */
    @NotNull User from();

    /**
     * Date the change was done in Unix time
     */
    int date();

    /**
     * Previous information about the chat member
     */
    @NotNull ChatMember old_chat_member();

    /**
     * New information about the chat member
     */
    @NotNull ChatMember new_chat_member();

    /**
     * Chat invite link, which was used by the user to join the chat;
     * for joining by invite link events only.
     */
    @Nullable ChatInviteLink invite_link();
}
