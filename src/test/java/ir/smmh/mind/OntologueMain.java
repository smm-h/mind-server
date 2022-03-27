package ir.smmh.mind;

import ir.smmh.apps.ontologue.Ontologue;
import sensitive.TelegramBotTokens;

class OntologueMain {
    public static void main(String[] args) {
        new Ontologue().start(TelegramBotTokens.OntologueBot);
    }
}