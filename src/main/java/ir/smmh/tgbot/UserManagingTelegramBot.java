package ir.smmh.tgbot;

import org.jetbrains.annotations.NotNull;

public interface UserManagingTelegramBot<U extends UserManagingTelegramBot.UserData> extends SimpleTelegramBot {
    @NotNull U getUser(long chatId);

    @NotNull U createUser(long chatId);

    interface UserData {
        long getChatId();
    }

}
