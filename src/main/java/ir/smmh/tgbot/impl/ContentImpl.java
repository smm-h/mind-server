package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot.Chat;
import ir.smmh.tgbot.TelegramBot.Location;
import ir.smmh.tgbot.TelegramBot.Update;
import ir.smmh.tgbot.TelegramBot.User;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ContentImpl extends JSONUtil.ReadOnlyJSONImpl implements Update.Content {

    public ContentImpl(JSONObject wrapper) {
        super(wrapper);
    }

    public static class Message extends ContentImpl implements Update.Content.Message {

        public Message(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public int message_id() {
            return getInt("message_id");
        }

        @Override
        public @Nullable String text() {
            return getNullableString("text");
        }

        @Override
        public @Nullable User from() {
            return User.of(getNullableJSONObject("from"));
        }

        @Override
        public @NotNull Chat chat() {
            return Chat.of(getJSONObject("chat"));
        }
    }

    public static class InlineQuery extends ContentImpl implements Update.Content.InlineQuery {

        public InlineQuery(JSONObject wrapped) {
            super(wrapped);
        }

        @Override
        public @NotNull String id() {
            return getString("id");
        }

        @Override
        public @NotNull User from() {
            return User.of(getJSONObject("from"));
        }

        @Override
        public @NotNull String query() {
            return getString("query");
        }

        @Override
        public @NotNull String offset() {
            return getString("offset");
        }

        @Override
        public @Nullable String chat_type() {
            return getNullableString("chat_type");
        }

        @Override
        public @Nullable Location location() {
            return Location.of(getNullableJSONObject("location"));
        }
    }
}
