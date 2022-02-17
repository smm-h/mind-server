package ir.smmh.tgbot;

import ir.smmh.api.API;

public interface APITelegramBot extends SimpleTelegramBot {
    API getAPI();

    @Override
    default void process(long chatId, String text, int messageId) {
        sendMessage(chatId, "<pre>" + getAPI().sendRequest(text) + "</pre>", messageId);
    }
}
