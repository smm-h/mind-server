package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Chat;
import ir.smmh.tgbot.types.ChatInviteLink;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChatJoinRequest extends ContentImpl implements ir.smmh.tgbot.types.ChatJoinRequest {
    public ChatJoinRequest(JSONObject wrapped) {
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
    public @Nullable String bio() {
        return getNullableString("bio");
    }

    @Override
    public @Nullable ChatInviteLink invite_link() {
        return ChatInviteLink.of(getNullableJSONObject("invite_link"));
    }
}
