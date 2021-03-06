package ir.smmh.util;

import ir.smmh.nile.adj.Multitude;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface FunctionalUtil {

    @Contract("_->_")
    static @NotNull <T> T itself(@NotNull T self) {
        return self;
    }

    @SuppressWarnings("unchecked")
    static <T, C> C cast(T self) {
        return (C) self;
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

    @Contract("_, _, !null -> !null")
    static <T, R> R with(@Nullable T it, @NotNull Function<? super T, ? extends R> function, @Nullable R defaultValue) {
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

    static <T extends Comparable<T>> @NotNull Iterable<T> sort(Iterable<? extends T> input) {
        List<T> list = new ArrayList<>(capacityNeededFor(input, 10));
        for (T i : input)
            with(i, list::add);
        Collections.sort(list);
        return list;
    }

    static Integer capacityNeededFor(Iterable<?> iterable) {
        if (iterable instanceof Collection<?>) {
            return ((Collection<?>) iterable).size();
        } else if (iterable instanceof Multitude) {
            return ((Multitude) iterable).getSize();
        } else {
            return null;
        }
    }

    static int capacityNeededFor(Iterable<?> iterable, int defaultCapacity) {
        Integer size = capacityNeededFor(iterable);
        return size == null ? defaultCapacity : size;
    }

    static int capacityNeededForIterateIfNull(Iterable<?> iterable) {
        Integer size = capacityNeededFor(iterable);
        if (size == null) {
            int count = 0;
            for (Object i : iterable) {
                count++;
            }
            return count;
        } else {
            return size;
        }
    }

    static boolean isEmpty(Iterable<?> iterable) {
        Integer size = capacityNeededFor(iterable);
        if (size == null) {
            return !iterable.iterator().hasNext();
        } else {
            return size == 0;
        }
    }

    static <T, R> @NotNull Iterable<R> convert(Iterable<? extends T> input, Function<T, R> convertor) {
        List<R> list = new ArrayList<>(capacityNeededFor(input, 10));
        for (T i : input)
            with(convertor.apply(i), list::add);
        return list;
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
        static <T> T getSafe(SupplierMayFail<? extends T> supplier, @Nullable T valueOnFailure) {
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
