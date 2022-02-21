package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.TelegramBot;
import ir.smmh.util.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public abstract class InlineQueryResultImpl extends JSONUtil.ReadOnlyJSONImpl implements TelegramBot.InlineQueryResult {

    private final @NotNull String id;

    protected InlineQueryResultImpl(@NotNull JSONObject wrapped, @NotNull String id) {
        super(wrapped);
        this.id = id;
    }

    @Override
    public @NotNull String id() {
        return id;
    }
}
