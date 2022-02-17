package ir.smmh.tgbot;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;

public interface SimpleTelegramBot extends TelegramBot {

    void process(long chatId, String text, int messageId);

    void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId);

    void sendPhoto(long chatId, File file, String caption, @Nullable Integer replyToMessageId) throws FileNotFoundException;

}
