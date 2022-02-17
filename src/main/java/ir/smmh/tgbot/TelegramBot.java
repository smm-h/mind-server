package ir.smmh.tgbot;

import java.util.function.Predicate;

public interface TelegramBot {

    Predicate<TelegramBot> RUNNING = TelegramBot::isRunning;

    void start(String withToken);

    void stop();

    boolean isRunning();
}