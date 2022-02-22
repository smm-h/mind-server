package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Chat;
import ir.smmh.tgbot.types.ChatInviteLink;
import ir.smmh.tgbot.types.ChatMember;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChatMemberUpdated extends ContentImpl implements ir.smmh.tgbot.types.ChatMemberUpdated {
    public ChatMemberUpdated(JSONObject wrapped) {
        super(wrapped);
    }

    @Override
    public @NotNull Chat chat() {
        return Chat.of(getJSONObject("chat"));
    }

    @Override
    public @NotNull User from() {
        return User.of(getJSONObject("from"));
    }

    @Override
    public int date() {
        return getInt("date");
    }

    @Override
    public @NotNull ChatMember old_chat_member() {
        return ChatMember.of(getJSONObject("old_chat_member"));
    }

    @Override
    public @NotNull ChatMember new_chat_member() {
        return ChatMember.of(getJSONObject("new_chat_member"));
    }

    @Override
    public @Nullable ChatInviteLink invite_link() {
        return ChatInviteLink.of(getNullableJSONObject("invite_link"));
    }
}
