package ir.smmh.tgbot;

import ir.smmh.api.API;
import ir.smmh.tgbot.types.Message;

public interface APITelegramBot extends TelegramBot {
    default void handleViaAPI(Message message) {
        String text = message.text();
        if (text != null)
            sendMessage(message.chat().id(), getAPI().sendRequest(text), message.message_id());
        // getMarkupWriter().code
    }

    API getAPI();
}
