package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.Chat;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChatImpl extends JSONUtil.JSONWrapper implements Chat {
    private final long id;

    private ChatImpl(JSONObject wrapped) {
        super(wrapped);
        this.id = wrapped.getLong("id");
    }

    @Contract("!null->!null")
    public static Chat of(@Nullable JSONObject wrapped) {
        if (wrapped == null) return null;
        switch (wrapped.getString("type")) {
            case "private":
                return new Private(wrapped);
            case "group":
                return new Group(wrapped);
            case "supergroup":
                return new Supergroup(wrapped);
            case "channel":
                return new Channel(wrapped);
            default:
                return new ChatImpl(wrapped);
        }
    }

    @Override
    public long id() {
        return id;
    }

    private static class Private extends ChatImpl implements Chat.Private {

        private Private(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @Nullable String username() {
            return wrapped.getString("username");
        }

        @Override
        public @Nullable String first_name() {
            return wrapped.getString("first_name");
        }

        @Override
        public @Nullable String last_name() {
            return wrapped.getString("last_name");
        }
    }

    private static class Group extends ChatImpl implements Chat.Group {

        private Group(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return wrapped.getString("title");
        }
    }

    private static class Supergroup extends ChatImpl implements Chat.Supergroup {

        private Supergroup(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return wrapped.getString("title");
        }

        @Override
        public @Nullable String username() {
            return wrapped.getString("username");
        }
    }

    private static class Channel extends ChatImpl implements Chat.Channel {

        private Channel(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return wrapped.getString("title");
        }

        @Override
        public @Nullable String username() {
            return wrapped.getString("username");
        }
    }
}
