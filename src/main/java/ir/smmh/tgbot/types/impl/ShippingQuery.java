package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ShippingQuery extends ContentImpl implements ir.smmh.tgbot.types.ShippingQuery {
    public ShippingQuery(JSONObject wrapped) {
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
    public @NotNull String invoice_payload() {
        return getString("invoice_payload");
    }

    @Override
    public @NotNull JSONObject shipping_address() {
        return getJSONObject("shipping_address");
    }
}
