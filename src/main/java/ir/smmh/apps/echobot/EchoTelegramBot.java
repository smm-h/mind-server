package ir.smmh.apps.echobot;

import ir.smmh.tgbot.impl.SimpleTelegramBotImpl;
import org.jetbrains.annotations.NotNull;

public class EchoTelegramBot extends SimpleTelegramBotImpl {
    public EchoTelegramBot() {
        super(null);
    }

    @Override
    public final void process(long chatId, @NotNull String text, int messageId) {
        sendMessage(chatId, text, messageId);
    }
}
