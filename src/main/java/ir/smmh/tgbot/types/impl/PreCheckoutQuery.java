package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class PreCheckoutQuery extends ContentImpl implements ir.smmh.tgbot.types.PreCheckoutQuery {
    public PreCheckoutQuery(JSONObject wrapped) {
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
    public @NotNull String currency() {
        return getString("currency");
    }

    @Override
    public int total_amount() {
        return 0;
    }

    @Override
    public @NotNull String invoice_payload() {
        return getString("invoice_payload");
    }

    @Override
    public @Nullable String shipping_option_id() {
        return getNullableString("shipping_option_id");
    }

    @Override
    public @Nullable JSONObject order_info() {
        return getNullableJSONObject("order_info");
    }
}
