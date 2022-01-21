package ir.smmh.tgbot.impl;

public class EchoBot extends SimpleBotImpl {
    @Override
    public final void process(long chatId, String text, int messageId) {
        sendMessage(chatId, text, messageId);
    }
}
