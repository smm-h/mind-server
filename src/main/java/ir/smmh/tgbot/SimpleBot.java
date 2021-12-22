package ir.smmh.tgbot;

import org.jetbrains.annotations.Nullable;

public interface SimpleBot extends Bot {

    void process(long chatId, String text, int messageId);

    void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId);
}
