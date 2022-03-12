package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.ChatMember;
import ir.smmh.tgbot.types.User;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChatMemberImpl extends JSONUtil.ReadOnlyJSONImpl implements ChatMember {

    public ChatMemberImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Contract("!null->!null")
    public static ChatMember of(@Nullable JSONObject wrapped) {
        if (wrapped == null) return null;
        switch (wrapped.getString("status")) {
            case "creator":
                return new Owner(wrapped);
            case "administrator":
                return new Administrator(wrapped);
            case "member":
                return new Member(wrapped);
            case "restricted":
                return new Restricted(wrapped);
            case "left":
                return new Left(wrapped);
            case "kicked":
                return new Banned(wrapped);
            default:
                return new ChatMemberImpl(wrapped);
        }
    }

    @Override
    public @NotNull User user() {
        return User.of(getJSONObject("user"));
    }

    private static class Owner extends ChatMemberImpl implements ChatMember.Owner {
        public Owner(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public boolean is_anonymous() {
            return getBoolean("is_anonymous");
        }

        @Override
        public @Nullable String custom_title() {
            return getNullableString("custom_title");
        }
    }

    private static class Administrator extends ChatMemberImpl implements ChatMember.Administrator {
        public Administrator(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public boolean can_be_edited() {
            return getBoolean("can_be_edited");
        }

        @Override
        public boolean is_anonymous() {
            return getBoolean("is_anonymous");
        }

        @Override
        public boolean can_manage_chat() {
            return getBoolean("can_manage_chat");
        }

        @Override
        public boolean can_delete_messages() {
            return getBoolean("can_delete_messages");
        }

        @Override
        public boolean can_manage_voice_chats() {
            return getBoolean("can_manage_voice_chats");
        }

        @Override
        public boolean can_restrict_members() {
            return getBoolean("can_restrict_members");
        }

        @Override
        public boolean can_promote_members() {
            return getBoolean("can_promote_members");
        }

        @Override
        public boolean can_change_info() {
            return getBoolean("can_change_info");
        }

        @Override
        public boolean can_invite_users() {
            return getBoolean("can_invite_users");
        }

        @Override
        public @Nullable Boolean can_post_messages() {
            return has("can_post_messages") ? getBoolean("can_post_messages") : null;
        }

        @Override
        public @Nullable Boolean can_edit_messages() {
            return has("can_edit_messages") ? getBoolean("can_edit_messages") : null;
        }

        @Override
        public @Nullable Boolean can_pin_messages() {
            return has("can_pin_messages") ? getBoolean("can_pin_messages") : null;
        }

        @Override
        public @Nullable String custom_title() {
            return getNullableString("custom_title");
        }
    }

    private static class Member extends ChatMemberImpl implements ChatMember.Member {

        public Member(JSONObject wrapped) {
            super(wrapped);
        }
    }

    private static class Restricted extends ChatMemberImpl implements ChatMember.Restricted {
        public Restricted(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public boolean is_member() {
            return getBoolean("is_member");
        }

        @Override
        public boolean can_change_info() {
            return getBoolean("can_change_info");
        }

        @Override
        public boolean can_invite_users() {
            return getBoolean("can_invite_users");
        }

        @Override
        public boolean can_pin_messages() {
            return getBoolean("can_pin_messages");
        }

        @Override
        public boolean can_send_messages() {
            return getBoolean("can_send_messages");
        }

        @Override
        public boolean can_send_media_messages() {
            return getBoolean("can_send_media_messages");
        }

        @Override
        public boolean can_send_polls() {
            return getBoolean("can_send_polls");
        }

        @Override
        public boolean can_send_other_messages() {
            return getBoolean("can_send_other_messages");
        }

        @Override
        public boolean can_add_web_page_previews() {
            return getBoolean("can_add_web_page_previews");
        }

        @Override
        public int until_date() {
            return getInteger("until_date");
        }
    }

    private static class Left extends ChatMemberImpl implements ChatMember.Left {
        public Left(JSONObject wrapped) {
            super(wrapped);
        }
    }

    private static class Banned extends ChatMemberImpl implements ChatMember.Banned {
        public Banned(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public int until_date() {
            return getInteger("until_date");
        }
    }
}
