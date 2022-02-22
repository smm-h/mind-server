package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.ChatMemberImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface ChatMember extends JSONUtil.ReadOnlyJSON {
    @Contract("!null->!null")
    static ChatMember of(@Nullable JSONObject wrapped) {
        return ChatMemberImpl.of(wrapped);
    }

    @NotNull User user();

    /**
     * Represents a chat member that owns the chat and has all administrator
     * privileges.
     */
    interface Owner extends ChatMember {
        /**
         * True, if the user's presence in the chat is hidden
         */
        boolean is_anonymous();

        /**
         * Custom title for this user
         */
        @Nullable String custom_title();
    }

    /**
     * Represents a chat member that has some additional privileges.
     */
    interface Administrator extends ChatMember {
        /**
         * True, if the bot is allowed to edit administrator privileges of
         * that user
         */
        boolean can_be_edited();

        /**
         * True, if the user's presence in the chat is hidden
         */
        boolean is_anonymous();

        /**
         * True, if the administrator can access the chat event log, chat
         * statistics, message statistics in channels, see channel members,
         * see anonymous administrators in supergroups and ignore slow mode.
         * Implied by any other administrator privilege
         */
        boolean can_manage_chat();

        /**
         * True, if the administrator can delete messages of other users
         */
        boolean can_delete_messages();

        /**
         * True, if the administrator can manage voice chats
         */
        boolean can_manage_voice_chats();

        /**
         * True, if the administrator can restrict, ban or unban chat members
         */
        boolean can_restrict_members();

        /**
         * True, if the administrator can add new administrators with a subset
         * of their own privileges or demote administrators that he has promoted,
         * directly or indirectly (promoted by administrators that were appointed
         * by the user)
         */
        boolean can_promote_members();

        /**
         * True, if the user is allowed to change the chat title, photo and
         * other settings
         */
        boolean can_change_info();

        /**
         * True, if the user is allowed to invite new users to the chat
         */
        boolean can_invite_users();

        /**
         * True, if the administrator can post in the channel; channels only
         */
        @Nullable Boolean can_post_messages();

        /**
         * True, if the administrator can edit messages of other users and
         * can pin messages; channels only
         */
        @Nullable Boolean can_edit_messages();

        /**
         * True, if the user is allowed to pin messages; groups and
         * supergroups only
         */
        @Nullable Boolean can_pin_messages();

        /**
         * Custom title for this user
         */
        @Nullable String custom_title();
    }

    /**
     * Represents a chat member that has no additional privileges or
     * restrictions.
     */
    interface Member extends ChatMember {
    }

    /**
     * Represents a chat member that is under certain restrictions in the
     * chat. Supergroups only.
     */
    interface Restricted extends ChatMember {
        /**
         * True, if the user is a member of the chat at the moment of the
         * request
         */
        boolean is_member();

        /**
         * True, if the user is allowed to change the chat title, photo and
         * other settings
         */
        boolean can_change_info();

        /**
         * True, if the user is allowed to invite new users to the chat
         */
        boolean can_invite_users();

        /**
         * True, if the user is allowed to pin messages
         */
        boolean can_pin_messages();

        /**
         * True, if the user is allowed to send text messages, contacts,
         * locations and venues
         */
        boolean can_send_messages();

        /**
         * True, if the user is allowed to send audios, documents, photos,
         * videos, video notes and voice notes
         */
        boolean can_send_media_messages();

        /**
         * True, if the user is allowed to send polls
         */
        boolean can_send_polls();

        /**
         * True, if the user is allowed to send animations, games, stickers
         * and use inline bots
         */
        boolean can_send_other_messages();

        /**
         * True, if the user is allowed to add web page previews to their messages
         */
        boolean can_add_web_page_previews();

        /**
         * Date when restrictions will be lifted for this user; unix time.
         * If 0, then the user is restricted forever
         */
        int until_date();
    }

    /**
     * Represents a chat member that isn't currently a member of the chat,
     * but may join it themselves.
     */
    interface Left extends ChatMember {
    }

    /**
     * Represents a chat member that was banned in the chat and can't return
     * to the chat or view chat messages.
     */
    interface Banned extends ChatMember {
        /**
         * Date when restrictions will be lifted for this user; unix time.
         * If 0, then the user is banned forever
         */
        int until_date();
    }
}
