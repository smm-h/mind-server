package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.User;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class UserImpl extends JSONUtil.JSONWrapper implements User {
    private UserImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Contract("!null->!null")
    public static User of(@Nullable JSONObject object) {
        if (object == null) return null;
        return new UserImpl(object);
    }

    @Contract("!null->!null")
    public static Myself myself(@Nullable JSONObject object) {
        if (object == null) return null;
        return new MyselfImpl(object);
    }

    @Override
    public long id() {
        return wrapped.getLong("id");
    }

    @Override
    public boolean is_bot() {
        return wrapped.getBoolean("is_bot");
    }

    @Override
    public @NotNull String first_name() {
        return wrapped.getString("first_name");
    }

    @Override
    public @Nullable String last_name() {
        return wrapped.optString("last_name", null);
    }

    @Override
    public @Nullable String username() {
        return wrapped.optString("username", null);
    }

    @Override
    public @Nullable String language_code() {
        return wrapped.optString("language_code", null);
    }

    public static class MyselfImpl extends UserImpl implements User.Myself {

        private MyselfImpl(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public boolean can_join_groups() {
            return wrapped.getBoolean("can_join_groups");
        }

        @Override
        public boolean can_read_all_group_messages() {
            return wrapped.getBoolean("can_read_all_group_messages");
        }

        @Override
        public boolean supports_inline_queries() {
            return wrapped.getBoolean("supports_inline_queries");
        }
    }
}
