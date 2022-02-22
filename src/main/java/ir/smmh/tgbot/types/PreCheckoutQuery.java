package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * This object contains information about an incoming pre-checkout query.
 */
public interface PreCheckoutQuery extends UpdateContent {
    /**
     * Unique query identifier
     */
    @NotNull String id();

    /**
     * User who sent the query
     */
    @NotNull User from();

    /**
     * Three-letter ISO 4217 currency code
     */
    @NotNull String currency();

    /**
     * Total price in the smallest units of the currency (integer,
     * not float/double). For example, for a price of US$ 1.45 pass
     * amount = 145. See the exp parameter in currencies.json, it
     * shows the number of digits past the decimal point for each
     * currency (2 for the majority of currencies).
     */
    int total_amount();

    /**
     * Bot specified invoice payload
     */
    @NotNull String invoice_payload();

    /**
     * Identifier of the shipping option chosen by the user
     */
    @Nullable String shipping_option_id();

    /**
     * Order info provided by the user
     */
    @Nullable JSONObject order_info(); // TODO OrderInfo
}
