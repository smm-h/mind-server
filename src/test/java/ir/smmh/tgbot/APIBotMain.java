package ir.smmh.tgbot;

import ir.smmh.mind.MindsAPI;
import ir.smmh.tgbot.impl.StandardAPITelegramBotImpl;
import ir.smmh.tgbot.types.Update;
import sensitive.TelegramBotTokens;

class APIBotMain {
    public static void main(String[] args) {
        StandardAPITelegramBot bot = new StandardAPITelegramBotImpl(new MindsAPI(), null);
        bot.addHandler((Update.Handler.message) bot::handleViaStandardAPI);
        bot.start(TelegramBotTokens.OntologueBot);
    }
}