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
    public static Chat of(@Nullable JSONObject object) {
        if (object == null) return null;
        switch (object.getString("type")) {
            case "private":
                return new Private(object);
            case "group":
                return new Group(object);
            case "supergroup":
                return new Supergroup(object);
            case "channel":
                return new Channel(object);
            default:
                return new ChatImpl(object);
        }
    }

    @Override
    public long id() {
        return id;
    }

    public static class Private extends ChatImpl implements Chat.Private {

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

    public static class Group extends ChatImpl implements Chat.Group {

        private Group(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return wrapped.getString("title");
        }
    }

    public static class Supergroup extends ChatImpl implements Chat.Supergroup {

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

    public static class Channel extends ChatImpl implements Chat.Channel {

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
