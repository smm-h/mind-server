package ir.smmh.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

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
}
