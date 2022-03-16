package ir.smmh.tgbot.impl;

import ir.smmh.net.api.API;
import ir.smmh.tgbot.APITelegramBot;
import org.jetbrains.annotations.Nullable;

public class APITelegramBotImpl extends TelegramBotImpl implements APITelegramBot {

    private final API api;

    public APITelegramBotImpl(API api, @Nullable String parseMode) {
        super(parseMode);
        this.api = api;
    }

    @Override
    public API getAPI() {
        return api;
    }
}
