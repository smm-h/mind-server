package ir.smmh.tgbot.impl;

import ir.smmh.api.StandardAPI;
import ir.smmh.tgbot.StandardAPIBot;

public class StandardAPIBotImpl extends APIBotImpl implements StandardAPIBot {
    public StandardAPIBotImpl(StandardAPI api) {
        super(api);
    }

    @Override
    public StandardAPI getAPI() {
        return (StandardAPI) super.getAPI();
    }
}
