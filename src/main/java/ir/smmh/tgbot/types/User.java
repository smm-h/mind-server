package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.UserImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface User extends JSONUtil.ReadOnlyJSON {

    @Contract("!null->!null")
    static User of(@Nullable JSONObject wrapped) {
        return UserImpl.of(wrapped);
    }

    long id();

    boolean is_bot();

    @NotNull String first_name();

    @Nullable String last_name();

    @Nullable String username();

    @Nullable String language_code();

    interface Myself extends User {
        @Override
        default boolean is_bot() {
            return true;
        }

        boolean can_join_groups();

        boolean can_read_all_group_messages();

        boolean supports_inline_queries();
    }
}
