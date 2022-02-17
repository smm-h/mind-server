package ir.smmh.tgbot;

import ir.smmh.mind.MindsAPI;
import ir.smmh.tgbot.impl.StandardAPITelegramBotImpl;

class APIBotMain {
    public static void main(String[] args) {
        new StandardAPITelegramBotImpl(new MindsAPI(), null).start(TelegramBotTokens.OntologueBot);
    }
}