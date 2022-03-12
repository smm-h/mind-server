package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Location;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class InlineQueryImpl extends ContentImpl implements ir.smmh.tgbot.types.InlineQuery {

    public InlineQueryImpl(JSONObject wrapped) {
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
    public @NotNull String query() {
        return getString("query");
    }

    @Override
    public @NotNull String offset() {
        return getString("offset");
    }

    @Override
    public @Nullable String chat_type() {
        return getNullableString("chat_type");
    }

    @Override
    public @Nullable Location location() {
        return Location.of(getNullableJSONObject("location"));
    }
}
