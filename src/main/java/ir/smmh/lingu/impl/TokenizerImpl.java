package ir.smmh.lingu.impl;

import ir.smmh.Backward;
import ir.smmh.lingu.*;
import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.lingu.impl.TokenizerMakerImpl.Definition;
import ir.smmh.lingu.processors.SingleProcessor;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// TODO normalizations e.g. − to - and → to ->
public class TokenizerImpl extends SingleProcessor implements Tokenizer {

    private final HashMap<Character, TreeSet<Kept>> kepts = new HashMap<>();

    // each character may belong to zero, one, or more streaks
    private final HashMap<Character, TreeSet<Streak>> streaksOfCharacters = new HashMap<>();

    // TODO updated but never queried
    private final HashSet<Character> verbatimCharacters = new HashSet<>();
    private final HashMap<String, TreeSet<Verbatim>> verbatims = new HashMap<>();
    private final HashSet<String> ignorableNames = new HashSet<>();
    private final PriorityQueue<Definition> schedule = new PriorityQueue<>(Comparator.comparingInt(Definition::getPriority));
    private final List<String> namesToIgnore = new ArrayList<>();

    public boolean ignore(String name) {
        if (ignorableNames.contains(name)) {
            namesToIgnore.add(name);
            // System.out.println("IGNORE: " + name);
            ignorableNames.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public void process(@NotNull Code code) {

        List<Token.Individual> stripped = new ArrayList<>();
        List<Token.Individual> drawable = new ArrayList<>();

        for (Token.Individual token : tokenize(code)) {
            drawable.add(token);
            if (!shouldBeIgnored(token))
                stripped.add(token);
        }

        CodeImpl.syntax.write(code, drawable);

        TokenizerImpl.tokenized.write(code, stripped);
    }

    public void schedule(@NotNull Definition definition) {
        schedule.add(definition);
    }

    public void defineAsScheduled() {
        while (!schedule.isEmpty()) {
            Definition definition = schedule.poll();
            assert definition != null;
            define(definition);
        }
    }

    private void addIgnorableName(String name) {
        ignorableNames.add(name);
    }

    private boolean canMake(List<IndividualToken> tokens, int index, List<IndividualToken> pattern) {
        for (int i = 0; i < pattern.size(); i++) {
            if (index + i >= tokens.size())
                return false;
            if (!pattern.get(i).equals(tokens.get(index + i)))
                return false;
        }
        return true;
    }

    public boolean shouldBeIgnored(Token.Individual token) {
        return namesToIgnore.contains(token.getTypeString());
    }

    // TODO make this the convention

    public IndividualTokenType define(Definition definition) {

        if (definition instanceof TokenizerMakerImpl.Streak)
            return define((TokenizerMakerImpl.Streak) definition);

        else if (definition instanceof TokenizerMakerImpl.Kept)
            return define((TokenizerMakerImpl.Kept) definition);

        else if (definition instanceof TokenizerMakerImpl.Verbatim)
            return define((TokenizerMakerImpl.Verbatim) definition);

        else
            throw new RuntimeException("undefined");
    }

    public Streak define(TokenizerMakerImpl.Streak definition) {
        Streak type = new Streak(definition);
        addIgnorableName(definition.toString());
        for (char c : definition.characters) {
            if (!streaksOfCharacters.containsKey(c)) {
                streaksOfCharacters.put(c, new TreeSet<>());
            }
            Objects.requireNonNull(streaksOfCharacters.get(c)).add(type);
        }
        return type;
    }

    public Verbatim define(TokenizerMakerImpl.Verbatim definition) {
        // addIgnorableName(verbatim.toString());
        // VERBATIMS SHOULD NOT BE IGNORED
        // TODO repeated verbatims should not go through
        // but should still return the TokenType
        Verbatim type = new Verbatim(definition, makeupStreaks(0, definition.data));
        for (IndividualToken token : type.pattern) {
            if (token.getType() instanceof NonStreakCharacter) {
                verbatimCharacters.add(token.data.charAt(0));
            }
        }

        String key = type.pattern.get(0).data;
        if (!verbatims.containsKey(key)) {
            verbatims.put(key, new TreeSet<>());
        }
        Objects.requireNonNull(verbatims.get(key)).add(type);
        return type;
    }

    public Kept define(TokenizerMakerImpl.Kept definition) {
        Kept type = new Kept(definition);
        addIgnorableName(definition.toString());
        Character key = definition.opener.charAt(0);
        if (!kepts.containsKey(key)) {
            kepts.put(key, new TreeSet<>());
        }
        Objects.requireNonNull(kepts.get(key)).add(type);
        return type;
    }

    @Override
    public List<Token.Individual> tokenize(Code code) {

        // start the process
        CodeProcess tokenizing = new CodeProcessImpl(code, "tokenizing");

        // get the contents of the code as a string
        String string = code.getOpenFile().read();

        // create a new list to put the tokens in
        List<Token.Individual> tokens = new ArrayList<>();

        // keep track of where (in which kept, if any) we are in the code
        Kept where = null;

        // keep track of which character we are looking at
        int flag = 0;

        // keep track of what portion of the code we have added all the tokens of
        int done = 0;

        // loop through all of the characters in the code
        while (flag < string.length()) {

            // if we are not inside a kept
            if (where == null) {

                // search for a kept to enter
                Kept found = null;

                // get the character at the forward flag
                char character = string.charAt(flag);

                // if we have a kept that starts with that character
                if (kepts.containsKey(character)) {

                    // search all kepts that start with that character
                    for (Kept kept : Objects.requireNonNull(kepts.get(character))) {

                        // figure out where the opener of that kept would end if it had appeared
                        int endIndex = flag + kept.opener.length();

                        // if that opener can appear and does appear
                        if (endIndex <= string.length() && string.substring(flag, endIndex).equals(kept.opener)) {

                            // we found our kept
                            found = kept;

                            // so stop the search
                            break;
                        }
                    }
                }

                // if we found a kept that we must enter
                if (found != null) {

                    // take whatever is before it, break it into tokens, and add them
                    for (IndividualToken token : makeupVerbatims(makeupStreaks(done, string.substring(done, flag)))) {
                        if (token.getType() instanceof NonStreakCharacter)
                            tokenizing.issue(new UnknownCharacter(token));
                        tokens.add(token);
                    }

                    // enter that kept
                    where = found;

                    // add the opener to this kept
                    tokens.add(where.keeper.new IndividualToken(where.opener, flag));

                    // move the flag forward
                    flag += where.opener.length() - 1;

                    // everything until here is added so also move the backward flag
                    done = flag + 1;
                }
            }

            // if we are inside a kept
            else {

                // figure out where its closer would end if it had appeared
                int endIndex = flag + where.closer.length();

                // if that closer can appear and does appear
                if (endIndex <= string.length() && string.substring(flag, endIndex).equals(where.closer)) {

                    // add the kept
                    tokens.add(where.new IndividualToken(string.substring(done, flag), done));

                    // also add its closer
                    tokens.add(where.keeper.new IndividualToken(where.closer, done));

                    // TODO test a kept with an at least 3 characters long closer

                    // move the flag forward
                    flag = endIndex - 1;

                    // everything until here is added so also move the backward flag
                    done = flag + 1;

                    // exit the kept
                    where = null;
                }
            }

            // move the flag over to the next character
            flag++;
        }

        // when the flag reaches the end, not everything has been added
        if (done < string.length()) {

            // either a non-kept portion remains
            if (where == null) {

                // break it into tokens and add them
                for (IndividualToken token : makeupVerbatims(makeupStreaks(done, string.substring(done, flag)))) {
                    if (token.getType() instanceof NonStreakCharacter)
                        tokenizing.issue(new UnknownCharacter(token));
                    tokens.add(token);
                }
            }

            // or an unclosed kept remains
            else {

                // unless its closer is entirely whitespace,
                if (!Backward.isBlank(where.closer))

                    // attach a mishap to its opener
                    tokenizing.issue(new UnclosedKept(tokens.get(tokens.size() - 1)));

                // and add it
                tokens.add(where.new IndividualToken(string.substring(done, flag), done));
            }
        }

        // finishSilently the process
        tokenizing.finishSilently();

        // and return the tokens
        return tokens;
    }

    public List<IndividualToken> makeupVerbatims(List<IndividualToken> tokens) {
        int index = 0, length;
        IndividualToken token;
        PriorityQueue<Verbatim> possibles;
        Verbatim longestPossible;
        while (index < tokens.size()) {

            // get t[i]
            token = tokens.get(index);

            // boolean reporting = token.data.equals("turn") || token.data.equals("{");
            // String report = "";

            // if (reporting)
            // report += "found at index: " + index;

            // V = all verbatims that start with t[i], sorted by length
            if (verbatims.containsKey(token.data)) {
                possibles = new PriorityQueue<>(verbatims.get(token.data));
                // System.out.println(possibles);

                // if (reporting)
                // report += ", possibles: " + possibles;

                // do v = V.pop() while v cannot be made and V is not empty
                while (!possibles.isEmpty()) {
                    longestPossible = possibles.remove();
                    // System.out.println(longestPossible);

                    // if (token.data.equals(".")) {
                    // if (index == 1) {
                    // System.out.println(tokens);
                    // System.out.println(longestPossible.pattern);
                    // System.out.println(index);
                    // for (int i = 0; i < 3; i++) {
                    // Token temp;
                    // // System.out.print(tokens.get(i + index));
                    // System.out.print("P: ");
                    // temp = longestPossible.pattern.get(i);
                    // System.out.print(temp.data);
                    // System.out.print(" ");
                    // System.out.print(temp.type);
                    // System.out.print("\tA: ");
                    // temp = tokens.get(i + index);
                    // System.out.print(temp.data);
                    // System.out.print(" ");
                    // System.out.print(temp.type);
                    // System.out.print("\n");
                    // }
                    // }
                    // }

                    // if v can be made, make it, and i += len(v)
                    if (canMake(tokens, index, longestPossible.pattern)) {
                        length = longestPossible.pattern.size();
                        for (int i = 0; i < length; i++) {
                            tokens.remove(index);
                        }
                        tokens.add(index, makeToken(longestPossible, token.position));
                        index += length - 1;
                        break;
                    }
                }
            }
            index++;
            // if (reporting)
            // report += ", new index: " + index;
            // if (reporting)
            // System.out.println(report);
        }
        return tokens;
    }

    private IndividualToken makeToken(TokenizerImpl.Verbatim verbatim, int position) {
        return verbatim.new IndividualToken(verbatim.data, position);
    }

    private IndividualToken makeToken(TokenizerImpl.NonStreakCharacter nsc, int position) {
        return nsc.new IndividualToken(Character.toString(nsc.data), position);
    }

    private List<IndividualToken> makeupStreaks(int offset, String string) {
        // String report = "\t" + "[" + string + "]===";
        string += (char) 0;
        char character;
        StringBuilder data = new StringBuilder();
        TreeSet<Streak> last, curr = new TreeSet<>();
        int index = 0;
        List<IndividualToken> tokens = new ArrayList<>();
        while (index < string.length()) {
            character = string.charAt(index);

            // if (character == '\n')
            // character = '#';

            last = curr;

            if (streaksOfCharacters.containsKey(character)) {
                curr = new TreeSet<>(streaksOfCharacters.get(character));
            } else {
                curr = new TreeSet<>();
                if (data.toString().equals("")) {
                    tokens.add(makeToken(new NonStreakCharacter(character), offset + index));
                    // report += "<" + character + ">";
                }
            }

            if (!last.isEmpty()) {
                curr.retainAll(last);
            }

            if (curr.isEmpty()) {
                if (!last.isEmpty()) {
                    tokens.add(last.first().new IndividualToken(data.toString(), offset + index - data.length()));
                    // report += "[" + data + "]";
                    index--;
                }
                data = new StringBuilder();
            } else {
                data.append(character);
            }

            index++;
        }
        tokens.remove(tokens.size() - 1);
        // System.out.println(report);
        return tokens;
    }

    public static class NonStreakCharacter extends IndividualTokenType {

        final char data;

        public NonStreakCharacter(char c) {
            super("unknown_character");
            this.data = c;
        }
    }

    public static class Verbatim extends IndividualTokenType implements Comparable<Verbatim> {

        public final String data;
        public final List<IndividualToken> pattern;

        public Verbatim(TokenizerMakerImpl.Verbatim definition, List<IndividualToken> pattern) {
            super(definition.title);
            this.data = definition.data;
            this.pattern = pattern;
        }

        public int compareTo(Verbatim other) {
            int comparison = -Integer.compare((this.data.length()), other.data.length());
            if (comparison == 0)
                comparison = this.data.compareTo(other.data);
            return comparison;
        }

        public boolean equals(Verbatim other) {
            return data.equals(other.data);
        }
    }

    public static class Streak extends IndividualTokenType implements Comparable<Streak> {

        public final Set<Character> characters = new HashSet<>();

        public Streak(TokenizerMakerImpl.Streak definition) {
            super(definition.title);
            characters.addAll(definition.characters);
        }

        public int compareTo(Streak other) {
            int comparison = Integer.compare((this.characters.size()), other.characters.size());
            if (comparison == 0)
                comparison = Integer.compare(this.characters.hashCode(), other.characters.hashCode());
            return comparison;
        }
    }

    public abstract static class TokenizerMishap extends MishapImpl.Caused {
        public TokenizerMishap(Token.Individual token, boolean fatal) {
            super(token, fatal);
        }
    }

    public static class UnknownCharacter extends TokenizerMishap {

        public UnknownCharacter(Token.Individual token) {
            super(token, true);
            assert token.getType() instanceof NonStreakCharacter;
        }

        public String getReport() {
            int cp = token.getData().codePointAt(0);
            if (cp < 32)
                return "Unknown non-printable character `" + StringUtil.codepointToText(token.getData().codePointAt(0)) + "`";
            else if (cp > 128)
                return "Unknown non-ASCII character `" + StringUtil.codepointToText(token.getData().codePointAt(0)) + "`";
            else
                return "Unknown character `" + token.getData() + "`";
        }
    }

    public class Kept extends IndividualTokenType implements Comparable<Kept> {

        public final String opener, closer;
        private final Keeper keeper;

        // public String (IndividualToken token)
        // return opener + token.data + closer;

        public Kept(TokenizerMakerImpl.Kept definition) {
            super(definition.title);
            this.closer = definition.closer;
            this.opener = definition.opener;
            keeper = new Keeper(definition.title + "_keeper");
        }

        public int compareTo(Kept other) {
            int comparison = -Integer.compare(this.opener.length(), other.opener.length());
            if (comparison == 0)
                comparison = this.opener.compareTo(other.opener);
            return comparison;
        }

        public class Keeper extends IndividualTokenType {
            public Keeper(String title) {
                super(title);
                namesToIgnore.add(title);
            }
        }
    }

    public class UnclosedKept extends TokenizerMishap {
        public UnclosedKept(Token.Individual token) {
            super(token, true);
            assert token.getType() instanceof Kept.Keeper;
        }

        public String getReport() {
            return "Opened but never closed";
        }
    }
}