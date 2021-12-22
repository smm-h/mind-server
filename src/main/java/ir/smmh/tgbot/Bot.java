package ir.smmh.tgbot;

public interface Bot {

    void start(String token);

    void stop();

    boolean isRunning();

}
