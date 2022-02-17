package ir.smmh.tgbot;

import ir.smmh.apps.echobot.EchoTelegramBot;

public class EchoBotMain {
    public static void main(String[] args) {
        new EchoTelegramBot().start(TelegramBotTokens.r5bot);
    }
}
