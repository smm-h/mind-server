package ir.smmh.apps.echobot;

import ir.smmh.tgbot.impl.TelegramBotImpl;
import ir.smmh.tgbot.types.Update;

public class EchoTelegramBot extends TelegramBotImpl {
    public EchoTelegramBot() {
        super(null);
        addHandler((Update.Handler.message) message -> {
            int messageId = message.message_id();
            String text = message.text();
            if (text != null) {
                long chatId = message.from().id();
                System.out.println("@" + chatId + " #" + messageId + ": " + text);
                try {
                    sendMessage(chatId, text, messageId);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
