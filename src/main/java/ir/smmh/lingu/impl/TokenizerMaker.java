package ir.smmh.lingu.impl;

import ir.smmh.jile.common.Common;
import ir.smmh.jile.common.Resource;
import ir.smmh.jile.common.Singleton;
import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Language;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.processors.Multiprocessor;
import ir.smmh.lingu.processors.SingleProcessor;

import java.util.*;

public class TokenizerMaker extends Language implements Singleton {

    private static TokenizerMaker singleton;

    public static TokenizerMaker singleton() {
        if (singleton == null) {
            singleton = new TokenizerMaker();
        }
        return singleton;
    }

    private final DefaultTokenizer meta;

    private TokenizerMaker() {
        super("Tokenizer Maker", "nlx", new Multiprocessor());

        meta = new DefaultTokenizer();

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

        processor.extend(meta);

        processor.extend(new SingleProcessor() {

            @Override
            public void process(CodeImpl code) {

                Map<Token.Individual, Resource> links = new HashMap<>();
                Iterator<Token.Individual> iterator = DefaultTokenizer.tokenized.read(code).iterator();

                while (iterator.hasNext()) {
                    Token.Individual token = iterator.next();
                    if (token.is("verbatim <import>")) {
                        token = iterator.next();
                        links.put(token, TokenizerMaker.singleton.find(token.getData()));
                    }
                }

                CodeImpl.links.write(code, links);
                // TODO update instead of over-writing
            }
        });
    }

    // public static final Port<Tokenizer> port = new Port<Tokenizer>();

    public final Maker<DefaultTokenizer> maker = new Maker<DefaultTokenizer>() {
        public DefaultTokenizer make(CodeImpl code) {
            // Tokenizer tokenizer = augment(new Tokenizer(), code);
            // port.write(code, tokenizer);
            // return tokenizer;
            return augment(new DefaultTokenizer(), code);
        }

        private DefaultTokenizer augment(DefaultTokenizer beingMade, CodeImpl code) {

            CodeProcess making = code.new Process("making a tokenizer");

            Objects.requireNonNull(beingMade);

            // System.out.println(language.name + " <+ " + code.getIdentity());

            String[] _string = {"single_quotes", "double_quotes"};
            // System.out.println(tokens.toString().replaceAll(", ", ""));
            List<Token.Individual> tokens = DefaultTokenizer.tokenized.read(code);
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
                                    augment(beingMade, new CodeImpl(Resource.of("nlx/" + tail.getData() + ".nlx")));
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
                    // Languages.singleton().associateExtWithLanguage(tail.data, language);
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
                        opener = Common.escape(tail.getData());
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
                        closer = Common.escape(tail.getData());
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
                                beingMade.define(new Streak(title, Common.escape(characters)));
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
                                beingMade.define(new Verbatim(Common.escape(tail.getData())));
                                break;
                            default:
                                making.issue(new UnexpectedToken(head, tail, "string"));
                        }
                        break;
                    default:
                        making.issue(new UnexpectedToken(head));
                }
            }
            making.finish();
            return beingMade;
        }
    };

    public abstract class Definition {
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

    public class Verbatim extends Definition implements Comparable<Verbatim> {
        public final String data;

        public Verbatim(String data) {
            super("verbatim <" + data + ">");
            this.data = data;
        }

        public int compareTo(Verbatim other) {
            int comparison = -((Integer) (this.data.length())).compareTo(other.data.length());
            if (comparison == 0)
                comparison = this.data.compareTo(other.data);
            return comparison;
        }

        public boolean equals(Verbatim other) {
            return data.equals(other.data);
        }

        @Override
        public int getPriority() {
            return 30;
        }
    }

    // private static final String NEGATOR = "(anything-except)";

    public class Streak extends Definition implements Comparable<Streak> {
        public final HashSet<Character> characters = new HashSet<Character>();

        public Streak(String title, String characters) {
            super(title);

            characters = characters.replaceAll("\\[A-Z\\]", "ABCDEFGHIJKLMNOPQRSTUVWXYZ").replaceAll("\\[a-z\\]", "abcdefghijklmnopqrstuvwxyz").replaceAll("\\[0-9\\]", "0123456789");

            // if (characters.contains(NEGATOR)) {
            // characters.replace(NEGATOR, "");
            // for (int i = 0; i < 256; i++)
            // this.characters.add((char) i);
            // for (int i = 0; i < characters.length(); i++)
            // this.characters.remove(characters.charAt(i));
            // } else {
            for (int i = 0; i < characters.length(); i++)
                this.characters.add(characters.charAt(i));
            // }
        }

        public int compareTo(Streak other) {
            int comparison = ((Integer) (this.characters.size())).compareTo(other.characters.size());
            if (comparison == 0)
                comparison = ((Integer) this.characters.hashCode()).compareTo(other.characters.hashCode());
            return comparison;
        }

        @Override
        public int getPriority() {
            return 20;
        }
    }

    public class Kept extends Definition implements Comparable<Kept> {
        public final String opener, closer;

        public Kept(String title, String opener, String closer) {
            super(title);
            this.closer = closer;
            this.opener = opener;
        }

        public int compareTo(Kept other) {
            int comparison = -((Integer) this.opener.length()).compareTo(other.opener.length());
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