package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FunctionalUtil {

    static <T> T itself(T self) {
        return self;
    }

    static <T> void with(@Nullable T it, @NotNull Consumer<T> consumer) {
        if (it != null) consumer.accept(it);
    }

    static <T, R> R with(@Nullable T it, @NotNull Function<T, R> function, R defaultValue) {
        return it == null ? defaultValue : function.apply(it);
    }

    static <T> boolean is(T o, Predicate<? super T> p) {
        return p.test(o);
    }

    static <T> boolean isNot(T o, Predicate<? super T> p) {
        return !p.test(o);
    }

    static <T> Predicate<T> not(Predicate<T> p) {
        return o -> !p.test(o);
    }

    static <T> Predicate<T> and(Predicate<T> p, Predicate<T> q) {
        return o -> p.test(o) && q.test(o);
    }

    static <T> Predicate<T> or(Predicate<T> p, Predicate<T> q) {
        return o -> p.test(o) || q.test(o);
    }
}
