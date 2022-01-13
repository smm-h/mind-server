package ir.smmh.util;

import ir.smmh.Backward;
import ir.smmh.util.jile.MathUtil;

import java.util.function.Predicate;

public interface StringPredicates {

    Predicate<String> EMPTY = String::isEmpty;

    Predicate<String> BLANK = Backward::isBlank;

    Predicate<String> PALINDROME = s -> {
        if (FunctionalUtil.is(s, EMPTY))
            return true;
        int n = s.length() - 1;
        int h = MathUtil.ceil(n / 2.0);
        for (int i = 0; i < h; i++)
            if (s.charAt(i) != s.charAt(n - i))
                return false;
        return true;
    };
}
