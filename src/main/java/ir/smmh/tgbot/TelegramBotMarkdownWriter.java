package ir.smmh.tgbot;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.util.MarkupFragment;
import ir.smmh.util.MarkupWriter;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Set;

public class TelegramBotMarkdownWriter implements TelegramBotMarkupWriter {
    private static final Set<Character>
            PLAIN = StringUtil.characterSet("\\_*[]()~`>#+-=|{}.!"),
            CODE_ONLY = StringUtil.characterSet("\\`"),
            URL_ONLY = StringUtil.characterSet("\\)");

    protected static String escape(String string, Set<Character> set) {
        int originalLength = string.length();
        int extraLength = 0;
        for (int i = 0; i < originalLength; i++) {
            if (set.contains(string.charAt(i))) {
                extraLength++;
            }
        }
        if (extraLength == 0) {
            return string;
        } else {
            StringBuilder b = new StringBuilder(originalLength + extraLength);
            for (int i = 0; i < originalLength; i++) {
                char c = string.charAt(i);
                if (set.contains(c)) {
                    b.append('\\');
                }
                b.append(c);
            }
            return b.toString();
        }
    }

    public static MarkupFragment wrap(MarkupFragment fragment, String wrapper) {
        return new MarkupFragment(wrapper + fragment.getData() + wrapper);
    }

    @Override
    public String getParseMode() {
        return "MarkdownV2";
    }

    @Override
    public @NotNull Document createDocument() {
        return new MarkdownDocument();
    }

    @Override
    public MarkupFragment plain(String text) {
        return new MarkupFragment(escape(text, PLAIN));
    }

    @Override
    public MarkupFragment bold(MarkupFragment fragment) {
        return wrap(fragment, "*");
    }

    @Override
    public MarkupFragment italic(MarkupFragment fragment) {
        // return wrap(fragment, "_"); elegant but incorrect
        return new MarkupFragment("_" + fragment.getData() + "_\r");
    }

    @Override
    public MarkupFragment underline(MarkupFragment fragment) {
        return wrap(fragment, "__");
    }

    @Override
    public MarkupFragment strike(MarkupFragment fragment) {
        return wrap(fragment, "~");
    }

    @Override
    public MarkupFragment code(String text) {
        return wrap(new MarkupFragment(escape(text, CODE_ONLY)), "`");
    }

    @Override
    public MarkupFragment link(MarkupFragment fragment, String url) {
        return new MarkupFragment("[" + fragment.getData() + "](" + escape(url, URL_ONLY) + ")");
    }

    @Override
    public MarkupFragment spoiler(MarkupFragment fragment) {
        return wrap(fragment, "||");
    }

    private class MarkdownDocument extends MarkdownSection implements MarkupWriter.Document {

        @Override
        public @NotNull Code toCode() {
            return new CodeImpl(new JSONObject()
                    .put("parse_mode", getParseMode())
                    .put("text", build().getData())
                    .toString(), "json");
        }
    }

    private class MarkdownSection extends AbstractSection {

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
            write("```\n");
            write(escape(codeString, CODE_ONLY));
            write("\n```\n\n");
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
}
