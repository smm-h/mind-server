package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Location;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class LocationImpl extends JSONUtil.ReadOnlyJSONImpl implements Location {

    private LocationImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Contract("!null->!null")
    public static Location of(@Nullable JSONObject wrapped) {
        if (wrapped == null) return null;
        return wrapped.has("live_period") ||
                wrapped.has("heading") ||
                wrapped.has("proximity_alert_radius")
                ? new LocationImpl.Live(wrapped)
                : new LocationImpl(wrapped);
    }

    @Override
    public float longitude() {
        return getFloat("longitude");
    }

    @Override
    public float latitude() {
        return getFloat("latitude");
    }

    @Override
    public @Nullable Float horizontal_accuracy() {
        return getNullableFloat("horizontal_accuracy");
    }

    private static class Live extends LocationImpl implements Location.Live {

        private Live(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @Nullable Integer live_period() {
            return has("live_period") ? getInt("live_period") : null;
        }

        @Override
        public @Nullable Integer heading() {
            return has("heading") ? getInt("heading") : null;
        }

        @Override
        public @Nullable Integer proximity_alert_radius() {
            return has("proximity_alert_radius") ? getInt("proximity_alert_radius") : null;
        }
    }
}
