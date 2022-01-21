package ir.smmh.tgbot;

import ir.smmh.mind.MindsAPI;
import ir.smmh.tgbot.impl.StandardAPIBotImpl;

class APIBotMain {

    public static void main(String[] args) {

        Bot bot = new StandardAPIBotImpl(new MindsAPI());

        bot.start("5069967929:AAFz5Wr5UfGwraPDQuKafouHnE6mLEATnQQ");

    }
}