package ir.smmh.tgbot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;

public interface SimpleBot extends Bot {

    void process(long chatId, String text, int messageId);

    void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId);

    void sendPhoto(long chatId, File file, String text, @Nullable Integer replyToMessageId) throws FileNotFoundException;

    @NotNull Markup getMarkupMode();
}
