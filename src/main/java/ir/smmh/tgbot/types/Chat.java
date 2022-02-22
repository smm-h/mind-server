package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.ChatImpl;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface Chat extends JSONUtil.ReadOnlyJSON {

    @Contract("!null->!null")
    static Chat of(@Nullable JSONObject wrapped) {
        return ChatImpl.of(wrapped);
    }

    long id();

    interface Private extends Chat {
        @Nullable String username();

        @Nullable String first_name();

        @Nullable String last_name();
    }

    interface Group extends Chat {
        @NotNull String title();
    }

    interface Supergroup extends Chat {
        @NotNull String title();

        @Nullable String username();
    }

    interface Channel extends Chat {
        @NotNull String title();

        @Nullable String username();
    }
}
