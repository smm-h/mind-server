package ir.smmh.lingu;

import ir.smmh.lingu.impl.SinglePassParser;
import ir.smmh.lingu.impl.TokenizerImpl;
import ir.smmh.lingu.impl.TokenizerMakerImpl;
import ir.smmh.lingu.impl.UnexpectedToken;

import java.util.HashSet;
import java.util.Set;

/**
 * Limitations:
 * <ul>
 * <li>Only works with {@link TokenizerImpl.Verbatim} keywords.*</li>
 * <li>Keywords have to be entirely made up of lowercase letters.</li>
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
public abstract class KeywordParser extends SinglePassParser {

    private final Set<String> keywords = new HashSet<>();
    private final String expectation;

    public KeywordParser(ParsibleKeywords[] values) {
        StringBuilder e = new StringBuilder();
        for (ParsibleKeywords value : values) {
            String keyword = value.toString().toLowerCase();
            keywords.add("verbatim <" + keyword + ">");
            if (!keyword.equals("not_a_keyword") && !keyword.equals("no_more_tokens")) {
                if (!e.toString().equals(""))
                    e.append("|");
                e.append(keyword);
            }
        }
        expectation = e.toString();
    }

    /**
     * Implement this interface only with enums whose values correspond to your
     * keywords but are all in caps. Also you must include these two values:
     * {@code NO_MORE_TOKENS}, {@code NOT_A_KEYWORD}.
     *
     * @see KeywordParser
     */
    public interface ParsibleKeywords {
    }

    public abstract class KeywordWalker extends CodeWalker {
        public KeywordWalker(CodeProcess process) {
            super(process);
        }

        public abstract ParsibleKeywords next();

        public String getNextKeyword() {
            if (hasNext()) {

                Token token = poll();

                if (token.is(keywords)) {
                    return token.getData().toUpperCase();
                } else {
                    getProcess().issue(new UnexpectedToken(token, expectation));
                    return "NOT_A_KEYWORD";
                }
            } else {
                return "NO_MORE_TOKENS";
            }
        }
    }
}