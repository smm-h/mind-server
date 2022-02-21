package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.Location;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class LocationImpl extends JSONUtil.JSONWrapper implements Location {

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
        return wrapped.getFloat("longitude");
    }

    @Override
    public float latitude() {
        return wrapped.getFloat("latitude");
    }

    @Override
    public @Nullable Float horizontal_accuracy() {
        return wrapped.has("horizontal_accuracy") ? wrapped.getFloat("horizontal_accuracy") : null;
    }

    private static class Live extends LocationImpl implements Location.Live {

        private Live(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @Nullable Integer live_period() {
            return wrapped.has("live_period") ? wrapped.getInt("live_period") : null;
        }

        @Override
        public @Nullable Integer heading() {
            return wrapped.has("heading") ? wrapped.getInt("heading") : null;
        }

        @Override
        public @Nullable Integer proximity_alert_radius() {
            return wrapped.has("proximity_alert_radius") ? wrapped.getInt("proximity_alert_radius") : null;
        }
    }
}
