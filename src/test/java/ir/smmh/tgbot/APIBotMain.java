package ir.smmh.tgbot;

import ir.smmh.mind.MindsAPI;
import ir.smmh.tgbot.impl.StandardAPITelegramBotImpl;

class APIBotMain {
    public static void main(String[] args) {
        StandardAPITelegramBot bot = new StandardAPITelegramBotImpl(new MindsAPI(), null);
        bot.addHandler((TelegramBot.Update.Handler.message) bot::handleViaStandardAPI);
        bot.start(TelegramBotTokens.OntologueBot);
    }
}