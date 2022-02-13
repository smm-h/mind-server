package ir.smmh.apps.echobot;

import ir.smmh.tgbot.impl.SimpleBotImpl;

public class EchoBot extends SimpleBotImpl {
    @Override
    public final void process(long chatId, String text, int messageId) {
        sendMessage(chatId, text, messageId);
    }
}
