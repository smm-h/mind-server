package ir.smmh.util;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.impl.CodeImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HTMLWriter implements MarkupWriter {

    protected static MarkupFragment tag(MarkupFragment fragment, String tag) {
        return new MarkupFragment(tag(fragment.getData(), tag));
    }

    protected static String tag(String text, String tag) {
        return "<" + tag + ">" + text + "</" + tag + ">";
    }

    protected static MarkupFragment tag(MarkupFragment fragment, String tag, String... attributes) {
        return new MarkupFragment(tag(fragment.getData(), tag, attributes));
    }

    protected static String tag(String text, String tag, String... attributes) {
        int n = attributes.length;
        if (NumberPredicates.ODD.test(n)) throw new IllegalArgumentException("attributes must be in pairs");
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < n) {
            builder
                    .append(" ")
                    .append(attributes[i++])
                    .append("=\"")
                    .append(escape(attributes[i++]))
                    .append("\"");
        }
        return "<" + tag + builder + ">" + text + "</" + tag + ">";
    }

    protected static String escape(String string) {
        return string
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#39;")
                ;
    }

    @Override
    public @NotNull Document createDocument() {
        return new HTMLDocument();
    }

    @Override
    public @NotNull List createList(boolean ordered) {
        return new HTMLList(ordered);
    }

    @Override
    public MarkupFragment plain(String text) {
        return new MarkupFragment(text);
    }

    @Override
    public MarkupFragment bold(MarkupFragment fragment) {
        return tag(fragment, "b");
    }

    @Override
    public MarkupFragment italic(MarkupFragment fragment) {
        return tag(fragment, "i");
    }

    @Override
    public MarkupFragment underline(MarkupFragment fragment) {
        return tag(fragment, "u");
    }

    @Override
    public MarkupFragment strike(MarkupFragment fragment) {
        return tag(fragment, "s");
    }

    @Override
    public MarkupFragment code(String text) {
        return tag(plain(text), ("code"));
    }

    @Override
    public MarkupFragment link(MarkupFragment fragment, String url) {
        return tag(fragment, "a", "href", url);
    }

    private static class HTMLList implements MarkupWriter.List {

        private final StringBuilder builder = new StringBuilder();
        private final boolean ordered;

        public HTMLList(boolean ordered) {
            this.ordered = ordered;
            builder.append(ordered ? "<ol>" : "<ul>");
        }

        @Override
        public @NotNull MarkupFragment build() {
            builder.append(ordered ? "</ol>" : "</ul>");
            return new MarkupFragment(builder.toString());
        }

        @Override
        public @NotNull List append(MarkupFragment item) {
            builder.append("<li>").append(item.getData()).append("</li>");
            return this;
        }

        @Override
        public @NotNull List nest(List innerList) {
            append(innerList.build());
            return this;
        }
    }

    private static class HTMLSection extends AbstractSection {

        @Override
        public @NotNull Section sectionBegin(MarkupFragment title) {
            write(tag(title, "h" + Math.min(6, ++depth)));
            return this;
        }

        @Override
        public @NotNull Section sectionEnd() {
            depth--;
            return this;
        }

        @Override
        public @NotNull Section writeParagraph(MarkupFragment item) {
            write(tag(item, "p"));
            return this;
        }

        @Override
        public @NotNull Section writeCodeBlock(String codeString) {
            write(tag(codeString, "pre"));
            return this;
        }

        @Override
        public @NotNull Section writeList(List list, @Nullable MarkupFragment caption) {
            write("<p>");
            if (caption != null) write(caption);
            write(list.build());
            write("</p>");
            return this;
        }
    }

    private static class HTMLDocument extends HTMLSection implements MarkupWriter.Document {
        @Override
        public @NotNull Code toCode() {
            return new CodeImpl(build().getData(), "html");
        }
    }
}
