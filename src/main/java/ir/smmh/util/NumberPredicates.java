package ir.smmh.util;

import java.util.function.Predicate;

public interface NumberPredicates {

    Predicate<Number> WHOLE = n -> n.doubleValue() == n.intValue();

    Predicate<Number> EVEN = n -> FunctionalUtil.is(n, WHOLE) && n.intValue() % 2 == 0;

    /*
     * if n is odd, n % 2 may be 1 or -1.
     * https://en.wikipedia.org/wiki/Modulo_operation#Common_pitfalls
     */ Predicate<Number> ODD = n -> FunctionalUtil.is(n, WHOLE) && n.intValue() % 2 != 0;

    Predicate<Number> PRIME = n -> {
        if (FunctionalUtil.is(n, WHOLE)) {
            int i = Math.abs(n.intValue()), c = 2;
            if (i < 2) return false;
            while (i / 2 >= c)
                if (i % c++ == 0)
                    return false;
            return true;
        } else {
            return false;
        }
    };
}
