package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * This object contains information about an incoming shipping query.
 */
public interface ShippingQuery extends UpdateContent {
    /**
     * Unique query identifier
     */
    @NotNull String id();

    /**
     * User who sent the query
     */
    @NotNull User from();

    /**
     * Bot specified invoice payload
     */
    @NotNull String invoice_payload();

    /**
     * User specified shipping address
     */
    @NotNull JSONObject shipping_address(); // TODO ShippingAddress
}
