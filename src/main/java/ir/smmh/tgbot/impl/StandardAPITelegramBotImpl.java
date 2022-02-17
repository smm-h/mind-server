package ir.smmh.tgbot.impl;

import ir.smmh.api.JSONAPIImpl;
import ir.smmh.tgbot.StandardAPITelegramBot;
import org.jetbrains.annotations.Nullable;

public class StandardAPITelegramBotImpl extends APITelegramBotImpl implements StandardAPITelegramBot {
    public StandardAPITelegramBotImpl(JSONAPIImpl api, @Nullable String parseMode) {
        super(api, parseMode);
    }

    @Override
    public final JSONAPIImpl getAPI() {
        return (JSONAPIImpl) super.getAPI();
    }
}
