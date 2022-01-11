package ir.smmh.util.jile;

import ir.smmh.Backward;

import static ir.smmh.util.jile.Quality.is;

public interface Qualities {
    interface Class {
        Quality<java.lang.Class<?>> ANNOTATION = java.lang.Class::isAnnotation;
        Quality<java.lang.Class<?>> ANONYMOUS_CLASS = java.lang.Class::isAnonymousClass;
        Quality<java.lang.Class<?>> ARRAY = java.lang.Class::isArray;
        Quality<java.lang.Class<?>> ENUM = java.lang.Class::isEnum;
        Quality<java.lang.Class<?>> INTERFACE = java.lang.Class::isInterface;
        Quality<java.lang.Class<?>> LOCAL_CLASS = java.lang.Class::isLocalClass;
        Quality<java.lang.Class<?>> MEMBER_CLASS = java.lang.Class::isMemberClass;
        Quality<java.lang.Class<?>> PRIMITIVE = java.lang.Class::isPrimitive;
        Quality<java.lang.Class<?>> SYNTHETIC = java.lang.Class::isSynthetic;
    }

    interface Number {

        Quality<java.lang.Number> WHOLE = n -> n.doubleValue() == n.intValue();

        Quality<java.lang.Number> EVEN = n -> is(n, WHOLE) && n.intValue() % 2 == 0;

        /*
         * if n is odd, n % 2 may be 1 or -1.
         * https://en.wikipedia.org/wiki/Modulo_operation#Common_pitfalls
         */ Quality<java.lang.Number> ODD = n -> is(n, WHOLE) && n.intValue() % 2 != 0;

        Quality<java.lang.Number> PRIME = n -> {
            if (is(n, WHOLE)) {
                int i = n.intValue(), c = 2;
                while (c <= i / 2)
                    if (i % c++ == 0)
                        return false;
                return true;
            } else {
                return false;
            }
        };
    }

    interface String {

        Quality<java.lang.String> EMPTY = java.lang.String::isEmpty;

        Quality<java.lang.String> BLANK = Backward::isBlank;

        Quality<java.lang.String> PALINDROME = s -> {
            if (is(s, EMPTY))
                return true;
            int n = s.length() - 1;
            int h = MathUtil.ceil(n / 2.0);
            for (int i = 0; i < h; i++)
                if (s.charAt(i) != s.charAt(n - i))
                    return false;
            return true;
        };
    }
}
