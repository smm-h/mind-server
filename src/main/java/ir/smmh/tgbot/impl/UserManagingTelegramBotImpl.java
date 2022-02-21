package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.UserData;
import ir.smmh.tgbot.UserManagingTelegramBot;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class UserManagingTelegramBotImpl<U extends UserData> extends TelegramBotImpl implements UserManagingTelegramBot<U> {

    private final Map.SingleValue.Mutable<Long, U> users = new MapImpl.SingleValue.Mutable<>();

    public UserManagingTelegramBotImpl(@Nullable String parseMode) {
        super(parseMode);
    }

    @Override
    public @NotNull U getUser(long chatId) {
        U user = users.getAtPlace(chatId);
        if (user == null) {
            U newUser = createUser(chatId);
            users.setAtPlace(chatId, newUser);
            return newUser;
        } else {
            return user;
        }
    }
}
