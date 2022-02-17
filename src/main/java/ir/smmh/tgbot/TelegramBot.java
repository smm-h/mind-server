package ir.smmh.tgbot;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface TelegramBot {

    Predicate<TelegramBot> RUNNING = TelegramBot::isRunning;

    void start(String withToken);

    void stop();

    boolean isRunning();
}