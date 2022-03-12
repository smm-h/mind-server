package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Chat;
import ir.smmh.tgbot.types.Message;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class MessageImpl extends ContentImpl implements Message {

    private MessageImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Contract("!null -> !null")
    public static Message of(@Nullable JSONObject wrapped) {
        if (wrapped == null) return null;
        return new MessageImpl(wrapped);
    }

    @Override
    public int message_id() {
        return getInteger("message_id");
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
