package ir.smmh.tgbot;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.util.HTMLWriter;
import ir.smmh.util.MarkupFragment;
import ir.smmh.util.MarkupWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class TelegramBotHTMLWriter extends HTMLWriter implements TelegramBotMarkupWriter {

    @Override
    public String getParseMode() {
        return "HTML";
    }

    @Override
    public MarkupFragment spoiler(MarkupFragment fragment) {
        return tag(fragment, "tg-spoiler");
    }

    @Override
    public @NotNull Document createDocument() {
        return new TelegramBotHTMLDocument();
    }

    @Override
    public @NotNull List createList(boolean ordered) {
        return TelegramBotMarkupWriter.super.createList(ordered);
    }

    private class TelegramBotHTMLSection extends AbstractSection {

        @Override
        public @NotNull Section sectionBegin(MarkupFragment title) {
            depth++;
            if (depth < 2) write("\n");
            write(bold(title));
            write("\n\n");
            return this;
        }

        @Override
        public @NotNull Section sectionEnd() {
            depth--;
            return this;
        }

        @Override
        public @NotNull Section writeParagraph(MarkupFragment fragment) {
            write(fragment);
            write("\n\n");
            return this;
        }

        @Override
        public @NotNull Section writeCodeBlock(String codeString) {
            write(tag(codeString, "pre"));
            write("\n\n");
            return this;
        }

        @Override
        public @NotNull Section writeList(List list, @Nullable MarkupFragment caption) {
            if (caption != null) {
                write(caption);
                write("\n");
            }
            write(list.build());
            write("\n\n");
            return this;
        }
    }

    private class TelegramBotHTMLDocument extends TelegramBotHTMLSection implements MarkupWriter.Document {
        @Override
        public @NotNull
        Code toCode() {
            return new CodeImpl(new JSONObject()
                    .put("parse_mode", getParseMode())
                    .put("text", build().getData())
                    .toString(), "json");
        }
    }
}
