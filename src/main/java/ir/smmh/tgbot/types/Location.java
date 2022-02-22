package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.LocationImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface Location extends JSONUtil.ReadOnlyJSON {

    @Contract("!null->!null")
    static Location of(@Nullable JSONObject wrapped) {
        return LocationImpl.of(wrapped);
    }

    float longitude();

    float latitude();

    /**
     * @return The radius of uncertainty for the location, measured in
     * meters; 0-1500
     */
    @Nullable Float horizontal_accuracy();

    interface Live extends Location {
        /**
         * @return Time relative to the message sending date, during which
         * the location can be updated; in seconds. For active live
         * locations only.
         */
        @Nullable Integer live_period();

        /**
         * @return The direction in which user is moving, in degrees; 1-360.
         * For active live locations only.
         */
        @Nullable Integer heading();

        /**
         * @return Maximum distance for proximity alerts about approaching
         * another chat member, in meters. For sent live locations only.
         */
        @Nullable Integer proximity_alert_radius();
    }
}
