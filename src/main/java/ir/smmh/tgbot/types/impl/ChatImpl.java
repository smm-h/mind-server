package ir.smmh.tgbot.types.impl;

import ir.smmh.tgbot.types.Chat;
import ir.smmh.util.impl.ReadOnlyJSONImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ChatImpl extends ReadOnlyJSONImpl implements Chat {
    private final long id;

    private ChatImpl(JSONObject wrapped) {
        super(wrapped);
        this.id = getLong("id");
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
            return getString("username");
        }

        @Override
        public @Nullable String first_name() {
            return getString("first_name");
        }

        @Override
        public @Nullable String last_name() {
            return getString("last_name");
        }
    }

    private static class Group extends ChatImpl implements Chat.Group {

        private Group(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return getString("title");
        }
    }

    private static class Supergroup extends ChatImpl implements Chat.Supergroup {

        private Supergroup(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return getString("title");
        }

        @Override
        public @Nullable String username() {
            return getString("username");
        }
    }

    private static class Channel extends ChatImpl implements Chat.Channel {

        private Channel(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String title() {
            return getString("title");
        }

        @Override
        public @Nullable String username() {
            return getString("username");
        }
    }
}
