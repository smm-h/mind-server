package ir.smmh.lingu.impl;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.TokenizerMaker;
import ir.smmh.lingu.processors.SingleProcessor;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.jile.OpenFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TokenizerMakerImpl extends LanguageImpl implements TokenizerMaker {
    private static TokenizerMaker instance;

    public TokenizerMakerImpl() {
        super("Tokenizer Maker", "nlx", new MultiprocessorImpl());

        TokenizerImpl meta = new TokenizerImpl();

        meta.define(new Kept("single_quotes", "'", "'"));
        meta.define(new Kept("double_quotes", "\"", "\""));

        meta.define(new Kept("comment", "#", "\n"));
        meta.ignore("comment");
        meta.define(new Kept("single_line_comment", "//", "\n"));
        meta.ignore("single_line_comment");
        meta.define(new Kept("multi_line_comment", "/*", "*/"));
        meta.ignore("multi_line_comment");

        meta.define(new Streak("whitespace", " \t\r\n\f"));
        meta.ignore("whitespace");

        meta.define(new Streak("identifier", "[0-9][A-Z][a-z]_-"));

        // meta.define(new Verbatim("ext"));
        meta.define(new Verbatim("import"));
        meta.define(new Verbatim("keep"));
        meta.define(new Verbatim("ignore"));
        meta.define(new Verbatim("verbatim"));
        meta.define(new Verbatim("streak"));
        meta.define(new Verbatim("..."));
        meta.define(new Verbatim("as"));

        getProcessor().extend(meta);

        getProcessor().extend(new SingleProcessor() {

            @Override
            public void process(@NotNull Code code) {

                Map<Token.Individual, OpenFile> links = new HashMap<>();
                Iterator<Token.Individual> iterator = TokenizerImpl.tokenized.read(code).iterator();

                while (iterator.hasNext()) {
                    Token.Individual token = iterator.next();
                    if (token.is("verbatim <import>")) {
                        token = iterator.next();
                        links.put(token, OpenFile.of(token.getData()));
                    }
                }

                // TODO update instead of over-writing
                CodeImpl.links.write(code, links);
            }
        });
    }

    public static TokenizerMaker getInstance() {
        return instance == null ? (instance = new TokenizerMakerImpl()) : instance;
    }

    // TODO get rid of this
    public static String escape(String unescaped) {
        return unescaped.replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t").replaceAll("\\\\r", "\r").replaceAll("\\\\f", "\f");
    }

    @Override

    public @NotNull TokenizerImpl makeFromCode(Code code) {
        // Tokenizer tokenizer = augment(new Tokenizer(), code);
        // port.write(code, tokenizer);
        // return tokenizer;
        return augment(new TokenizerImpl(), code);
    }

    private TokenizerImpl augment(@NotNull TokenizerImpl beingMade, Code code) {

        CodeProcess making = new CodeProcessImpl(code, "making a tokenizer");

        // System.out.println(language.name + " <+ " + code.getTitle());

        String[] _string = {"single_quotes", "double_quotes"};
        // System.out.println(tokens.toString().replaceAll(", ", ""));
        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);
        // System.out.println(tokens);
        Iterator<Token.Individual> iterator = tokens.iterator();
        Token.Individual head, tail;
        while (iterator.hasNext()) {
            head = iterator.next();
            String opener, closer, title, characters;
            switch (head.getTypeString()) {
                case "verbatim <import>":
                    tail = iterator.next();
                    switch (tail.getTypeString()) {
                        case "single_quotes":
                        case "double_quotes":
                            try {
                                augment(beingMade, new CodeImpl(OpenFile.of("nlx/" + tail.getData() + ".nlx")));
                            } catch (Exception e) {
                                // process.add(meta.new ImportFailed());
                                System.out.println("import failed");
                            }
                            break;
                        default:
                            making.issue(new UnexpectedToken(head, tail, "string"));
                    }
                    break;
                // case "verbatim <ext>":
                // tail = iterator.next();
                // switch (tail.type.toString()) {
                // case "single_quotes":
                // case "double_quotes":
                // if (!isImporting())
                // Languages.getInstance().associateExtWithLanguage(tail.data, language);
                // break;
                // default:
                // process.add(meta.new UnexpectedToken(head, tail, "string"));
                // }
                // break;
                case "verbatim <ignore>":
                    tail = iterator.next();
                    if (!tail.is("identifier")) {
                        making.issue(new UnexpectedToken(tail, "identifier"));
                        break;
                    }
                    beingMade.ignore(tail.getData());
                    break;
                case "verbatim <keep>":
                    tail = iterator.next();
                    if (!tail.is(_string)) {
                        making.issue(new UnexpectedToken(tail, "string"));
                        break;
                    }
                    opener = escape(tail.getData());
                    tail = iterator.next();
                    if (!tail.is("verbatim <...>")) {
                        making.issue(new UnexpectedToken(tail, "<...>"));
                        break;
                    }
                    tail = iterator.next();
                    if (!tail.is(_string)) {
                        making.issue(new UnexpectedToken(tail, "string"));
                        break;
                    }
                    closer = escape(tail.getData());
                    tail = iterator.next();
                    if (!tail.is("verbatim <as>")) {
                        making.issue(new UnexpectedToken(tail, "<as>"));
                        break;
                    }
                    tail = iterator.next();
                    if (!tail.is("identifier")) {
                        making.issue(new UnexpectedToken(tail, "identifier"));
                        break;
                    }
                    title = tail.getData();
                    beingMade.define(new Kept(title, opener, closer));
                    break;
                case "verbatim <streak>":
                    tail = iterator.next();
                    switch (tail.getTypeString()) {
                        case "single_quotes":
                        case "double_quotes":
                            characters = tail.getData();
                            if (!tail.is(_string)) {
                                making.issue(new UnexpectedToken(tail, "string"));
                                break;
                            }
                            tail = iterator.next();
                            if (!tail.is("verbatim <as>")) {
                                making.issue(new UnexpectedToken(tail, "<as>"));
                                break;
                            }
                            tail = iterator.next();
                            if (!tail.is("identifier")) {
                                making.issue(new UnexpectedToken(tail, "identifier"));
                                break;
                            }
                            title = tail.getData();
                            beingMade.define(new Streak(title, escape(characters)));
                            break;
                        default:
                            making.issue(new UnexpectedToken(head, tail, "string|identifier"));
                    }
                    break;
                case "verbatim <verbatim>":
                    tail = iterator.next();
                    switch (tail.getTypeString()) {
                        case "single_quotes":
                        case "double_quotes":
                            beingMade.define(new Verbatim(escape(tail.getData())));
                            break;
                        default:
                            making.issue(new UnexpectedToken(head, tail, "string"));
                    }
                    break;
                default:
                    making.issue(new UnexpectedToken(head));
            }
        }
        making.finishSilently();
        return beingMade;
    }

    public abstract static class Definition {
        public final String title;

        public Definition(String title) {
            this.title = title;
        }

        @Override
        public final String toString() {
            return title;
        }

        public abstract int getPriority();
    }

    // private static final String NEGATOR = "(anything-except)";

    public static class Verbatim extends Definition implements Comparable<Verbatim> {
        public final String data;

        public Verbatim(String data) {
            super("verbatim <" + data + ">");
            this.data = data;
        }

        public int compareTo(Verbatim other) {
            return this.data.compareTo(other.data);
        }

        public boolean equals(Verbatim other) {
            return data.equals(other.data);
        }

        @Override
        public int getPriority() {
            return 30;
        }
    }

    public static class Streak extends Definition implements Comparable<Streak> {
        public final Set<Character> characters;

        public Streak(String title, String characters) {
            this(title, characters.toCharArray());
        }

        public Streak(String title, char[] characters) {
            this(title, new HashSet<>(Sequential.of(characters).asList()));
        }

        public Streak(String title, Set<Character> characters) {
            super(title);
            this.characters = characters;
        }

        public int compareTo(Streak other) {
            int comparison = Integer.compare(this.characters.size(), other.characters.size());
            if (comparison == 0)
                comparison = Integer.compare(this.characters.hashCode(), other.characters.hashCode());
            return comparison;
        }

        @Override
        public int getPriority() {
            return 20;
        }
    }

    public static class Kept extends Definition implements Comparable<Kept> {
        public final String opener, closer;

        public Kept(String title, String opener, String closer) {
            super(title);
            this.closer = closer;
            this.opener = opener;
        }

        public int compareTo(Kept other) {
            int comparison = -Integer.compare(this.opener.length(), other.opener.length());
            if (comparison == 0)
                comparison = this.opener.compareTo(other.opener);
            return comparison;
        }

        @Override
        public int getPriority() {
            return 10;
        }
    }
}