package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.User;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class UserImpl extends JSONUtil.ReadOnlyJSONImpl implements User {
    private UserImpl(JSONObject wrapped) {
        super(wrapped);
    }

    @Contract("!null->!null")
    public static User of(@Nullable JSONObject wrapped) {
        if (wrapped == null) return null;
        return new UserImpl(wrapped);
    }

    @Contract("!null->!null")
    public static Myself myself(@Nullable JSONObject object) {
        if (object == null) return null;
        return new MyselfImpl(object);
    }

    @Override
    public long id() {
        return getLong("id");
    }

    @Override
    public boolean is_bot() {
        return getBoolean("is_bot");
    }

    @Override
    public @NotNull String first_name() {
        return getString("first_name");
    }

    @Override
    public @Nullable String last_name() {
        return getNullableString("last_name");
    }

    @Override
    public @Nullable String username() {
        return getString("username");
    }

    @Override
    public @Nullable String language_code() {
        return getString("language_code");
    }

    private static class MyselfImpl extends UserImpl implements User.Myself {

        private MyselfImpl(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public boolean can_join_groups() {
            return getBoolean("can_join_groups");
        }

        @Override
        public boolean can_read_all_group_messages() {
            return getBoolean("can_read_all_group_messages");
        }

        @Override
        public boolean supports_inline_queries() {
            return getBoolean("supports_inline_queries");
        }
    }
}
