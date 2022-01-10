package ir.smmh.tgbot;

import ir.smmh.util.jile.Quality;

public interface Bot {

    Quality<Bot> RUNNING = Bot::isRunning;

    void start(String token);

    void stop();

    boolean isRunning();
}
