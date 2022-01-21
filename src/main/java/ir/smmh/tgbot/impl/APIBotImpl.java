package ir.smmh.tgbot.impl;

import ir.smmh.api.API;
import ir.smmh.tgbot.APIBot;

public class APIBotImpl extends SimpleBotImpl implements APIBot {

    private final API api;

    public APIBotImpl(API api) {
        super();
        this.api = api;
    }

    @Override
    public API getAPI() {
        return api;
    }
}
