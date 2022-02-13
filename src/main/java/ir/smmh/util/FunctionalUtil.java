package ir.smmh.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface FunctionalUtil {

    static <T> T itself(T self) {
        return self;
    }

    static <T> T with(@Nullable T it, @NotNull T defaultValue) {
        return it == null ? defaultValue : it;
    }

    static <T> T with(@Nullable T it, @NotNull Supplier<? extends T> supplier) {
        return it == null ? supplier.get() : it;
    }

    static <T> void with(@Nullable T it, @NotNull Consumer<T> consumer) {
        if (it != null) consumer.accept(it);
    }

    static <T, R> R with(@Nullable T it, @NotNull Function<? super T, ? extends R> function, R defaultValue) {
        return it == null ? defaultValue : function.apply(it);
    }

    static <T> boolean is(T o, Predicate<? super T> p) {
        return p.test(o);
    }

    static <T> boolean isNot(T o, Predicate<? super T> p) {
        return !p.test(o);
    }

    static <T> Predicate<T> not(Predicate<? super T> p) {
        return o -> !p.test(o);
    }

    static <T> Predicate<T> and(Predicate<? super T> p, Predicate<? super T> q) {
        return o -> p.test(o) && q.test(o);
    }

    static <T> Predicate<T> or(Predicate<? super T> p, Predicate<? super T> q) {
        return o -> p.test(o) || q.test(o);
    }

    @FunctionalInterface
    interface OnEventListener {
        void onEvent();
    }

    @SuppressWarnings("NonExceptionNameEndsWithException")
    @FunctionalInterface
    interface OnEventListenerWithException<E extends Throwable> {
        void onEventWithException() throws E;
    }

    @FunctionalInterface
    interface RecursivelySpecific<T> {
        @Contract("->this")
        T specificThis();
    }

    @FunctionalInterface
    interface ToFloatFunction<T> {
        float applyAsFloat(T object);
    }

    @FunctionalInterface
    interface ToCharFunction<T> {
        char applyAsChar(T object);
    }

    @FunctionalInterface
    interface ToStringFunction<T> {
        String applyAsString(T object);
    }

    @FunctionalInterface
    interface SupplierMayFail<T> extends Supplier<T> {
        @Contract("_, !null -> !null")
        static <T> T getSafe(SupplierMayFail<? extends T> supplier, T valueOnFailure) {
            try {
                return supplier.getUnsafe();
            } catch (Throwable throwable) {
                return valueOnFailure;
            }
        }

        @Nullable
        @Override
        default T get() {
            return getSafe(this, null);
        }

        @SuppressWarnings("ProhibitedExceptionDeclared")
        T getUnsafe() throws Throwable;
    }
}
