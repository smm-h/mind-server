package ir.smmh.tgbot.impl;

import ir.smmh.net.api.StandardAPIImpl;
import ir.smmh.tgbot.StandardAPITelegramBot;
import org.jetbrains.annotations.Nullable;

public class StandardAPITelegramBotImpl extends APITelegramBotImpl implements StandardAPITelegramBot {
    public StandardAPITelegramBotImpl(StandardAPIImpl api, @Nullable String parseMode) {
        super(api, parseMode);
    }

    @Override
    public final StandardAPIImpl getAPI() {
        return (StandardAPIImpl) super.getAPI();
    }
}
