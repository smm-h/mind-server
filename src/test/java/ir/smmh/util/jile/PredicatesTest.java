package ir.smmh.util.jile;

import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import static ir.smmh.util.FunctionalUtil.*;
import static ir.smmh.util.NumberPredicates.*;
import static ir.smmh.util.StringPredicates.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredicatesTest {
    @Test
    final void numbers() {
        assertTrue(is(1, WHOLE), "1 is not whole");
        assertTrue(isNot(1.5, WHOLE), "1.5 is whole");
        assertTrue(is(3, ODD), "3 is not odd");
        assertTrue(is(4, EVEN), "4 is not even");
        assertTrue(is(5, PRIME), "5 is not prime");
    }

    @Test
    final void logic() {
        assertTrue(is(2, and(EVEN, PRIME)), "2 is not even and prime");
        assertTrue(is(3, and(ODD, PRIME)), "3 is not odd and prime");
        assertTrue(is(4, and(EVEN, not(PRIME))), "4 is not even and non-prime");
        assertTrue(is(9, and(ODD, not(PRIME))), "9 is not odd and non-prime");
        for (int i = 0; i < 100; i++) {
            assertTrue(is(RandomUtil.generate.nextInt(), or(EVEN, ODD)), "random integer is neither even nor odd");
        }
    }

    @Test
    final void string() {
        assertTrue(is("", EMPTY), "empty string is not empty");
        assertTrue(isNot("    ", EMPTY), "whitespace is empty");
        assertTrue(is("", BLANK), "empty string is not blank");
        assertTrue(is("    ", BLANK), "whitespace is not blank");
        assertTrue(is("madam", PALINDROME), "\"madam\" is not a palindrome");
        assertTrue(isNot("mister", PALINDROME), "\"mister\" is a palindrome");
    }
}