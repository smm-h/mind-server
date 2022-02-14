package ir.smmh.apps.echobot;

import ir.smmh.tgbot.impl.SimpleBotImpl;
import org.jetbrains.annotations.NotNull;

public class EchoBot extends SimpleBotImpl {
    @Override
    public final void process(long chatId, @NotNull String text, int messageId) {
        sendMessage(chatId, text, messageId);
    }
}
