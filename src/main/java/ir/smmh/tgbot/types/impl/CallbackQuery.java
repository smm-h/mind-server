package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class CallbackQuery extends ContentImpl implements ir.smmh.tgbot.types.CallbackQuery {
    public CallbackQuery(JSONObject wrapped) {
        super(wrapped);
    }

    @Override
    public @NotNull String id() {
        return getString("id");
    }

    @Override
    public @NotNull User from() {
        return User.of(getJSONObject("from"));
    }

    @Override
    public @Nullable JSONObject message() {
        return getNullableJSONObject("message");
    }

    @Override
    public @Nullable String inline_message_id() {
        return getNullableString("inline_message_id");
    }

    @Override
    public @NotNull String chat_instance() {
        return getString("chat_instance");
    }

    @Override
    public @Nullable String data() {
        return getNullableString("data");
    }

    @Override
    public @Nullable String game_short_name() {
        return getNullableString("game_short_name");
    }
}
