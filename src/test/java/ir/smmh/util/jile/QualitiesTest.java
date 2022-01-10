package ir.smmh.util.jile;

import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import static ir.smmh.util.jile.Qualities.Number.*;
import static ir.smmh.util.jile.Qualities.String.*;
import static ir.smmh.util.jile.Quality.*;

class QualitiesTest {
    @Test
    void numbers() {
        assert is(1, WHOLE);
        assert isNot(1.5, WHOLE);
        assert is(3, ODD);
        assert is(4, EVEN);
        assert is(5, PRIME);
    }

    @Test
    void logic() {
        assert is(2, and(EVEN, PRIME));
        assert is(3, and(ODD, PRIME));
        assert is(4, and(EVEN, not(PRIME)));
        assert is(9, and(ODD, not(PRIME)));
        for (int i = 0; i < 100; i++) {
            assert is(RandomUtil.generate.nextInt(), or(EVEN, ODD));
        }
    }

    @Test
    void string() {
        assert is("", EMPTY);
        assert isNot("    ", EMPTY);
        assert is("", BLANK);
        assert is("    ", BLANK);
        assert is("madam", PALINDROME);
        assert isNot("mister", PALINDROME);
    }
}