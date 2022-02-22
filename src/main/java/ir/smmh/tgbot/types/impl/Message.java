package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Chat;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class Message extends ContentImpl implements ir.smmh.tgbot.types.Message {

    public Message(JSONObject wrapped) {
        super(wrapped);
    }

    @Override
    public int message_id() {
        return getInt("message_id");
    }

    @Override
    public @Nullable String text() {
        return getNullableString("text");
    }

    @Override
    public @Nullable User from() {
        return User.of(getNullableJSONObject("from"));
    }

    @Override
    public @NotNull Chat chat() {
        return Chat.of(getJSONObject("chat"));
    }
}
