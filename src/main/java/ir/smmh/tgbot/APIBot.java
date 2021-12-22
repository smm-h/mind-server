package ir.smmh.tgbot;

import ir.smmh.api.API;

public interface APIBot extends SimpleBot {
    API getAPI();

    @Override
    default void process(long chatId, String text, int messageId) {
        sendMessage(chatId, "<pre>" + getAPI().sendRequest(text) + "</pre>", messageId);
    }
}
