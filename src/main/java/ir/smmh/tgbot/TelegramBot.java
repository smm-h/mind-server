package ir.smmh.tgbot;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SingleSequence;
import ir.smmh.tgbot.types.InlineQueryResult;
import ir.smmh.tgbot.types.Update;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface TelegramBot {

//    TelegramBotMarkupWriter getMarkupWriter();

    Predicate<TelegramBot> RUNNING = TelegramBot::isRunning;

    void start(String withToken);

    void stop();

    boolean isRunning();

    void addHandler(Update.Handler<?> handler);

    @NotNull User.Myself getMe() throws MethodFailedException;

    void sendMessage(long chatId, String text, @Nullable Integer replyToMessageId);

    void sendPhoto(long chatId, File file, String caption, @Nullable Integer replyToMessageId) throws FileNotFoundException;

    default void answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results) {
        answerInlineQuery(inline_query_id, results, false);
    }

    default void answerInlineQuery(String inline_query_id, InlineQueryResult result) {
        answerInlineQuery(inline_query_id, new SingleSequence<>(result), false);
    }

    void answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results, boolean is_personal);
}