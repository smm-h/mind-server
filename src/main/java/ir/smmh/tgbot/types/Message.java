package ir.smmh.tgbot.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Message extends UpdateContent {
    int message_id();

    @Nullable String text();

    @Nullable User from();

    @NotNull Chat chat();
}
