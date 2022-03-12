package ir.smmh.tgbot;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tgbot.types.InlineQueryResult;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface UserManagingTelegramBot<U extends UserData> extends TelegramBot {
    @NotNull U getUser(long chatId);

    @NotNull U createUser(long chatId);

    @Override
    default JSONObject answerInlineQuery(String inline_query_id, Sequential<InlineQueryResult> results) {
        return answerInlineQuery(inline_query_id, results, true);
    }
}
