package ir.smmh.tgbot;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;

public interface UserManagingTelegramBot<U extends UserManagingTelegramBot.UserData> extends TelegramBot {
    @NotNull U getUser(long chatId);

    @NotNull U createUser(long chatId);

    interface UserData {
        long getChatId();
    }

    @Override
    default void answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results) {
        answerInlineQuery(inline_query_id, results, true);
    }
}
