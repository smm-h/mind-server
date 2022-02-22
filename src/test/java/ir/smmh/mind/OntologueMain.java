package ir.smmh.mind;

import sensitive.TelegramBotTokens;

class OntologueMain {
    public static void main(String[] args) {
        new Ontologue().start(TelegramBotTokens.OntologueBot);
    }
}