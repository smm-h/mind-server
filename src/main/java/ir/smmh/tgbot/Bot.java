package ir.smmh.tgbot;

import java.util.function.Predicate;

public interface Bot {

    Predicate<Bot> RUNNING = Bot::isRunning;

    void start(String withToken);

    void stop();

    boolean isRunning();
}
