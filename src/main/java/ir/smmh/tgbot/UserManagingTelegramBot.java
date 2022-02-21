package ir.smmh.tgbot;

import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;

public interface UserManagingTelegramBot<U extends UserData> extends TelegramBot {
    @NotNull U getUser(long chatId);

    @NotNull U createUser(long chatId);

    @Override
    default void answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results) {
        answerInlineQuery(inline_query_id, results, true);
    }
}
