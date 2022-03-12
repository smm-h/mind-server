package ir.smmh.tgbot;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SingleSequence;
import ir.smmh.tgbot.types.InlineQueryResult;
import ir.smmh.tgbot.types.Message;
import ir.smmh.tgbot.types.Update;
import ir.smmh.tgbot.types.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

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

    @Nullable Message sendMessage(long chatId, String text, @Nullable Integer replyToMessageId);

    @Nullable Message sendPhoto(long chatId, File file, String caption, @Nullable Integer replyToMessageId) throws FileNotFoundException;

    default JSONObject answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results) {
        return answerInlineQuery(inline_query_id, results, false);
    }

    default JSONObject answerInlineQuery(String inline_query_id, InlineQueryResult result) {
        return answerInlineQuery(inline_query_id, new SingleSequence<>(result));
    }

    JSONObject answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results, boolean is_personal);
}