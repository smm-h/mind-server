package ir.smmh.tgbot;

import ir.smmh.util.StringUtil;

import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface Bot {

    Predicate<Bot> RUNNING = Bot::isRunning;

    void start(String withToken);

    void stop();

    boolean isRunning();

    interface Markup {

        Markup HTML = new Markup() {

            @Override
            public String mode() {
                return "HTML";
            }

            @Override
            public String bold(String text) {
                return "<b>" + escape(text) + "</b>";
            }

            @Override
            public String italic(String text) {
                return "<i>" + escape(text) + "</i>";
            }

            @Override
            public String underline(String text) {
                return "<u>" + escape(text) + "</u>";
            }

            @Override
            public String strike(String text) {
                return "<s>" + escape(text) + "</s>";
            }

            @Override
            public String code(String text) {
                return "<code>" + escape(text) + "</code>";
            }

            @Override
            public String codeBlock(String text) {
                return "<pre>" + escape(text) + "</pre>";
            }

            @Override
            public String link(String text, String url) {
                return "<a href=\"" + escape(url) + "\">" + escape(text) + "</a>";
            }

            @Override
            public String spoiler(String text) {
                return "<tg-spoiler>" + escape(text) + "</tg-spoiler>";
            }

            private String escape(String string) {
                return string
                        .replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("&", "&amp;")
                        .replaceAll("\"", "&quot;");
            }
        };

        Set<Character>
                GENERAL = StringUtil.characterSet("\\_*[]()~`>#+-=|{}.!"),
                CODE_ONLY = StringUtil.characterSet("\\`"),
                URL_ONLY = StringUtil.characterSet("\\)");

        Markup Markdown = new Markup() {
            @Override
            public String mode() {
                return "MarkdownV2";
            }

            @Override
            public String bold(String text) {
                return "*" + escape(text) + "*";
            }

            @Override
            public String italic(String text) {
                return "_" + escape(text) + "_\r";
            }

            @Override
            public String underline(String text) {
                return "__" + escape(text) + "__";
            }

            @Override
            public String strike(String text) {
                return "~" + escape(text) + "~";
            }

            @Override
            public String code(String text) {
                return "`" + escape(text, CODE_ONLY) + "`";
            }

            @Override
            public String codeBlock(String text) {
                return "\n```\n" + escape(text, CODE_ONLY) + "\n```";
            }

            @Override
            public String link(String text, String url) {
                return "[" + escape(text) + "](" + escape(url, URL_ONLY) + ")";
            }

            @Override
            public String spoiler(String text) {
                return "||" + escape(text) + "||";
            }

            private String escape(String string) {
                return escape(string, GENERAL);
            }

            private String escape(String string, Set<Character> set) {
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
        };

        String mode();

        String bold(String text);

        String italic(String text);

        String underline(String text);

        String strike(String text);

        String spoiler(String text);

        String code(String text);

        String codeBlock(String text);

        String link(String text, String url);

        default String linkUser(String text, String username) {
            return link(text, String.format("tg://user?id=<%s>", username));
        }
    }
}