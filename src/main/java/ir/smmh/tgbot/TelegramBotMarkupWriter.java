package ir.smmh.tgbot;

import ir.smmh.util.MarkupFragment;
import ir.smmh.util.MarkupWriter;

public interface TelegramBotMarkupWriter extends MarkupWriter {

    String getParseMode();

    MarkupFragment spoiler(MarkupFragment fragment);

    default MarkupFragment linkUser(MarkupFragment fragment, String username) {
        return link(fragment, String.format("tg://user?id=<%s>", username));
    }

}


