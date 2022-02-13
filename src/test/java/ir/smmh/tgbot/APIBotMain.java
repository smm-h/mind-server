package ir.smmh.tgbot;

import ir.smmh.mind.MindsAPI;
import ir.smmh.tgbot.impl.StandardAPIBotImpl;

class APIBotMain {
    public static void main(String[] args) {
        new StandardAPIBotImpl(new MindsAPI()).start(Bots.ontologue);
    }
}