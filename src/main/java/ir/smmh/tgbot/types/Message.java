package ir.smmh.tgbot.types;

import ir.smmh.tgbot.types.impl.MessageImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface Message extends UpdateContent {

    @Contract("!null -> !null")
    static Message of(@Nullable JSONObject wrapped) {
        return MessageImpl.of(wrapped);
    }

    int message_id();

    @Nullable String text();

    @Nullable User from();

    @NotNull Chat chat();
}
