package ir.smmh.tgbot;

import org.jetbrains.annotations.NotNull;

public interface UserManagingTelegramBot<U extends UserManagingTelegramBot.UserData> extends SimpleTelegramBot {
    interface UserData {
        long getChatId();
    }

    @NotNull U getUser(long chatId);

    @NotNull U createUser(long chatId);

}
