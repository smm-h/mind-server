package ir.smmh.lingu.impl;

import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.CollectiveTokenType.CollectiveToken;
import ir.smmh.lingu.IndividualTokenType;
import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.processors.SingleProcessor;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Limitations:
 * <ul>
 * <li>Only works with {@link TokenizerImpl.Verbatim} keywords.*</li>
 * <li>Keywords have to be made up entirely of lowercase letters.</li>
 * </ul>
 * <p>
 * Footnote:
 * <ul>
 * <li>*: These tokens are defined via {@link TokenizerMakerImpl.Verbatim} objects,
 * which in turn are generated, usually from either
 * {@code verbatim 'somekeyword'} statements in {@code *.nlx} files or
 * single-quoted literal values in patterns of {@code *.ncx} files.</li>
 * </ul>
 */
public abstract class SinglePassParser extends SingleProcessor {

    private final Map<String, CollectiveToken> namedGroups = new HashMap<>();

    public void nameGroup(String name, @NotNull CollectiveToken block) {
        namedGroups.put(name, block);
    }

    public CollectiveToken findGroup(String name) {
        return namedGroups.get(name);
    }

    public interface Resolver {
        boolean canBeResolved(String identifier);
    }

    public static abstract class SinglePassParserMishap extends MishapImpl.Caused {

        public SinglePassParserMishap(Token.Individual token, boolean fatal) {
            super(token, fatal);
        }
    }

    public static class IdentifierNotResolved extends SinglePassParserMishap {

        public IdentifierNotResolved(Token.Individual token) {
            super(token, true);
        }

        @Override
        public String getReport() {
            return "Identifier `" + token.getData() + "` could not be resolved.";
        }
    }

    public abstract class CodeWalker {

        private final CodeProcess process;
        private final Stack<Token.Collective> blocks = new Stack<>();
        private final Stack<Integer> blockIndices = new Stack<>();
        IndividualToken DUMMY_TOKEN = new IndividualTokenType("DUMMY").new IndividualToken("", 0);
        /**
         * must never be null
         */
        private Token[] array = new Token[0];
        private int index;

        public CodeWalker(CodeProcess process) {
            this.process = process;
        }

        public CodeProcess getProcess() {
            return process;
        }

        public int getDepth() {
            return blocks.size();
        }

        // public class CannotEnterNullGroup extends SinglePassParserMishap {

        // public CannotEnterNullGroup(IndividualToken token) {
        // super(token, true);
        // }

        // @Override
        // public String getReport() {
        // return "Cannot enter null group";
        // }
        // }

        public void enter(@NotNull Token.Collective node) {

            // push the new block on the stack
            blocks.push(node);

            // remember the position of the index for the outer block
            blockIndices.push(index);

            // reset the index for the inner block
            index = 0;

            // remake the array
            remakeArray();
        }

        public void reenter() {
            index = 0;
        }

        public void exit() {

            // pop the inner block off the stack
            blocks.pop();

            // remember where the index was when we entered that other block
            index = blockIndices.pop();

            // remake the array
            remakeArray();
        }

        /**
         * If peek is what it "must be", meaning if it is not issue an appropriate
         * mishap.
         */
        public boolean peekMustBe(String peekingFor, String... types) {
            Token token = peek(peekingFor);
            if (token == null) {
                return false;
            } else if (token.is(types)) {
                return true;
            } else {
                process.issue(new UnexpectedToken(token.getFirstHandle(), String.join("|", types)));
                return false;
            }
        }

        public Token peek(String peekingFor) {
            return peek(peekingFor, false);
        }

        /**
         * Returns the next available token. However, if the end of the block has been
         * reached, if {@code optional} is false a {@code null} is returned, and if it
         * is true a dummy token will be returned, to avoid unnecessary
         * {@code NullPointerException}s. So either set optional to true, or prepare for
         * getting a {@code null}.
         */
        @Nullable
        @Contract("_, true -> !null")
        public Token peek(String peekingFor, boolean optional) {
            if (array.length == 0 || index >= array.length) {
                if (optional) {
                    return DUMMY_TOKEN;
                } else {
                    process.issue(new UnexpectedEndOfBlock(peekingFor));
                    // index++;
                    return null;
                }
            } else {
                return array[index];
            }
        }

        /**
         * <b>IMPORTANT</b> You must always call either {@code hasNext()} or
         * {@code peek}/{@code peekMustBe} before polling, and only do so if
         * {@code hasNext()} returns true, or {@code peek}/{@code peekMustBe} returns
         * non-null.
         */
        public Token poll() {
            return array[index++];
        }

        public boolean hasNext() {
            return index < array.length;
        }

        private void remakeArray() {

            if (blocks.isEmpty()) {
                array = new Token[0];

            } else {
                List<Token> list = blocks.peek().getChildren();
                array = new Token[list.size()];

                int i = 0;
                for (Token token : list)
                    array[i++] = token;
            }
        }

        /**
         * Use when exactly one or the other of two verbatims is expected.
         */
        public Boolean parseDichotomy(String vtrue, String vfalse) {
            if (peekMustBe(vtrue + "|" + vfalse, "verbatim <" + vtrue + ">", "verbatim <" + vfalse + ">")) {
                return poll().is("verbatim <" + vtrue + ">");
            } else {
                return null;
            }
        }

        /**
         * Either returns a group, or return null and issues an approriate mishap, i.e.
         * it is safe to ignore null outcomes.
         */

        public CollectiveToken parseGroup(String type) {
            return parseGroup(type, null);
        }

        /**
         * Either returns a group, or return null and issues an approriate mishap, i.e.
         * it is safe to ignore null outcomes. You can also name the block.
         */

        public CollectiveToken parseGroup(String type, @Nullable String name) {
            if (peekMustBe(type, "group_type <" + type + ">")) {
                CollectiveToken group = (CollectiveToken) poll();
                if (name != null)
                    nameGroup(name, group);
                return group;
            } else {
                return null;
            }
        }

        /**
         * Either returns null and issues a fatal mishap, or returns a group identifier.
         * This means you can safely ignore a null outcome.
         */
        public String parseGroupIdentifier() {
            return parseIdentifier(namedGroups);
        }

        /**
         * Use when expecting an identifier.
         */
        public String parseIdentifier() {
            return parseIdentifier((Resolver) null);
        }

        /**
         * Use when expecting an identifier. Provide the appropriate {@link Map} object
         * to assert it must contain it.
         */
        public String parseIdentifier(Map<String, ?> map) {
            return parseIdentifier(map::containsKey);
        }

        /**
         * Use when expecting an identifier. Provide the appropriate {@link Resolver}
         * object to assert it must resolve it.
         */
        @Nullable
        public String parseIdentifier(@Nullable Resolver resolver) {
            if (peekMustBe("identifier", "identifier")) {
                Token token = poll();
                String identifier = token.getData();
                if (resolver != null && !resolver.canBeResolved(identifier)) {
                    getProcess().issue(new IdentifierNotResolved((IndividualToken) token));
                    return null;
                }
                return identifier;
            } else {
                return null;
            }
        }

        /**
         * Returns true if the sign is negative, and false if it is positive or no sign
         * is present.
         */
        public boolean parseSign() {
            if (peek("sign", true).is("verbatim <->", "verbatim <+>"))
                return poll().is("verbatim <->");
            return false;
        }

        @Nullable
        public Integer parseSignedInteger() {
            boolean negative = parseSign();
            Integer i = parseUnsignedInteger();
            return i == null ? null : i * (negative ? -1 : +1);
        }

        @Nullable
        public Float parseSignedFloat() {
            boolean negative = parseSign();
            Float f = parseUnsignedFloat();
            return f == null ? null : f * (negative ? -1 : +1);
        }

        @Nullable
        public Integer parseUnsignedInteger() {
            return peekMustBe("digits", "digits") ? (int) StringUtil.valueOfString(poll().getData()) : null;
        }

        @Nullable
        public Float parseUnsignedFloat() {

            // there must be digits
            if (peekMustBe("digits", "digits")) {
                String string = poll().getData();

                // there may be a dot
                if (peek("dot", true).is("verbatim <.>")) {
                    poll();

                    // if so, there must be digits again
                    if (peekMustBe("digits", "digits")) {
                        string += "." + poll().getData();
                    }
                }

                return StringUtil.valueOfString(string).floatValue();
            } else {
                return null;
            }
        }

        public class UnexpectedEndOfBlock extends SinglePassParserMishap {

            private final String expectation;

            public UnexpectedEndOfBlock(String expectation) {
                super(blocks.peek().getCloser(), true);
                this.expectation = expectation;
            }

            public String getReport() {
                return "Unexpected end of block; instead was expecting: `" + expectation + "`";
            }
        }
    }
}