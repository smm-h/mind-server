package ir.smmh.tgbot.impl;

import ir.smmh.api.JSONAPIImpl;
import ir.smmh.tgbot.StandardAPIBot;

public class StandardAPIBotImpl extends APIBotImpl implements StandardAPIBot {
    public StandardAPIBotImpl(JSONAPIImpl api) {
        super(api);
    }

    @Override
    public final JSONAPIImpl getAPI() {
        return (JSONAPIImpl) super.getAPI();
    }
}
