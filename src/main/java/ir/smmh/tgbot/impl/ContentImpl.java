package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.Chat;
import ir.smmh.tgbot.TelegramBot.Update;
import ir.smmh.tgbot.TelegramBot.User;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ContentImpl extends JSONUtil.JSONWrapper implements Update.Content {

    public ContentImpl(JSONObject wrapper) {
        super(wrapper);
    }

    public static class Message extends ContentImpl implements Update.Content.Message {

        public Message(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public int message_id() {
            return wrapped.getInt("message_id");
        }

        @Override
        public @Nullable String text() {
            return wrapped.optString("text", null);
        }

        @Override
        public @Nullable User from() {
            return UserImpl.of(wrapped.optJSONObject("from", null));
        }

        @Override
        public @NotNull Chat chat() {
            return Chat.of(wrapped.getJSONObject("chat"));
        }
    }

    public static class InlineQuery extends ContentImpl implements Update.Content.InlineQuery {

        public InlineQuery(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public int id() {
            return wrapped.getInt("id");
        }

        @Override
        public @NotNull String query() {
            return wrapped.getString("query");
        }

        @Override
        public @NotNull User from() {
            return UserImpl.of(wrapped.getJSONObject("from"));
        }
    }
}
