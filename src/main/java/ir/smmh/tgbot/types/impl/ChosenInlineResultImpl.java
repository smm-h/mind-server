package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.ChosenInlineResult;
import ir.smmh.tgbot.types.Location;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChosenInlineResultImpl extends ContentImpl implements ChosenInlineResult {
    public ChosenInlineResultImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Override
    public @NotNull String result_id() {
        return getString("result_id");
    }

    @Override
    public @NotNull User from() {
        return User.of(getJSONObject("from"));
    }

    @Override
    public @Nullable Location location() {
        return Location.of(getNullableJSONObject("location"));
    }

    @Override
    public @Nullable String inline_message_id() {
        return getNullableString("inline_message_id");
    }

    @Override
    public @NotNull String query() {
        return getString("query");
    }
}
