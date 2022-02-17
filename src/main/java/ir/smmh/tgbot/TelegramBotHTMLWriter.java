package ir.smmh.tgbot;

import ir.smmh.util.HTMLWriter;
import ir.smmh.util.MarkupFragment;

public class TelegramBotHTMLWriter extends HTMLWriter implements TelegramBotMarkupWriter {

    @Override
    public String getParseMode() {
        return "HTML";
    }

    @Override
    public MarkupFragment spoiler(MarkupFragment fragment) {
        return tag(fragment, "tg-spoiler");
    }
}
