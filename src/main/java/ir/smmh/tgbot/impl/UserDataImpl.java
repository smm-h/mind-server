package ir.smmh.tgbot.impl;

import ir.smmh.tgbot.UserData;

public class UserDataImpl implements UserData {
    private final long chatId;

    public UserDataImpl(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public long getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "UserData@" + chatId;
    }
}
