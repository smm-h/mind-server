package ir.smmh.nile.adj;

import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.nile.verbs.*;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.impl.MutableImpl;
import ir.smmh.util.impl.ViewImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.*;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public interface Sequential<T> extends Iterable<T>, ReverseIterable<T>, CanContain<T>, CanClone<Sequential<T>>, CanGetAtIndex<T> {

    static <T> Sequential<T> of(List<T> list) {

        return new AbstractSequential<>() {
            @Override
            public T getAtIndex(int index) throws IndexOutOfBoundsException {
                return list.get(index);
            }

            @Override
            public int getSize() {
                return list.size();
            }
        };
    }

    static Sequential<Integer> of(int[] array) {
        return new AbstractSequential<>() {

            @Override
            public Integer getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static Sequential<Float> of(float[] array) {
        return new AbstractSequential<>() {

            @Override
            public Float getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static Sequential<Long> of(long[] array) {
        return new AbstractSequential<>() {

            @Override
            public Long getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static Sequential<Double> of(double[] array) {
        return new AbstractSequential<>() {

            @Override
            public Double getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static Sequential<Byte> of(byte[] array) {
        return new AbstractSequential<>() {

            @Override
            public Byte getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static Sequential<Character> of(char[] array) {
        return new AbstractSequential<>() {

            @Override
            public Character getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static Sequential<Boolean> of(boolean[] array) {
        return new AbstractSequential<>() {

            @Override
            public Boolean getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static <T> Sequential<T> of(T[] array) {
        return new AbstractSequential<>() {

            @Override
            public T getAtIndex(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getSize() {
                return array.length;
            }
        };
    }

    static <T> Sequential<T> empty() {
        return new AbstractSequential<>() {
            @Override
            public T getAtIndex(int index) throws IndexOutOfBoundsException {
                throw new IndexOutOfBoundsException();
            }

            @Override
            public int getSize() {
                return 0;
            }
        };
    }

    static Sequential<Character> of(String string) {
        return Sequential.of(string.toCharArray()); // TODO optimize
    }

    @Override
    default boolean hasIndex(int index) {
        return index >= 0 && index < getSize();
    }

    default boolean[] toBooleanArray(Predicate<T> toBoolean) {
        boolean[] array = new boolean[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toBoolean.test(getAtIndex(i));
        }
        return array;
    }

    default long[] toLongArray(ToLongFunction<T> toLong) {
        long[] array = new long[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toLong.applyAsLong(getAtIndex(i));
        }
        return array;
    }

    default T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = getAtIndex(i);
        }
        return array;
    }

    default float[] toFloatArray(FunctionalUtil.ToFloatFunction<T> toFloat) {
        float[] array = new float[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toFloat.applyAsFloat(getAtIndex(i));
        }
        return array;
    }

    default double[] toDoubleArray(ToDoubleFunction<T> toDouble) {
        double[] array = new double[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toDouble.applyAsDouble(getAtIndex(i));
        }
        return array;
    }

    default char[] toCharArray(FunctionalUtil.ToCharFunction<T> toChar) {
        char[] array = new char[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toChar.applyAsChar(getAtIndex(i));
        }
        return array;
    }

    default int[] toIntArray(ToIntFunction<T> toInt) {
        int[] array = new int[getSize()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toInt.applyAsInt(getAtIndex(i));
        }
        return array;
    }

    default int count(Predicate<? super T> toTest) {
        int count = 0;
        for (T element : this) {
            if (toTest.test(element)) {
                count++;
            }
        }
        return count;
    }

    default Sequential<T> filterOutOfPlace(Predicate<? super T> toTest) {
        Sequential.Mutable.VariableSize<T> filtered = Sequential.Mutable.VariableSize.of(new ArrayList<>(count(toTest)));
        for (T element : this) {
            if (toTest.test(element)) {
                filtered.append(element);
            }
        }
        return filtered;
    }

    default <R> Sequential<R> applyOutOfPlace(Function<? super T, ? extends R> toReplace) {
        Sequential.Mutable.VariableSize<R> applied = Sequential.Mutable.VariableSize.of(new ArrayList<>(getSize()));
        for (T element : this) {
            applied.append(toReplace.apply(element));
        }
        return applied;
    }

    default @NotNull List<T> asList() {
        return new AbstractList<>() {
            @Override
            public int size() {
                return getSize();
            }

            @Override
            public T get(int index) {
                return getAtIndex(index);
            }
        };
    }

    @Override
    default boolean isEmpty() {
        return getSize() == 0;
    }

    default boolean contains(T toCheck) {
        return findFirst(toCheck) != -1;
    }

    default int count(T toCount) {
        int count = 0;
        for (T element : this) {
            if (Objects.equals(element, toCount)) {
                count++;
            }
        }
        return count;
    }

    default int findFirst(T toFind) {
        int i = 0;
        for (T element : this) {
            if (Objects.equals(element, toFind)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    default int findLast(T toFind) {
        int i = getSize();
        for (T element : inReverse()) {
            i--;
            if (Objects.equals(element, toFind)) {
                return i;
            }
        }
        return -1;
    }

    default int findNth(T toFind, int n) {
        if (n == 0)
            return findFirst(toFind);
        if (n == -1)
            return findLast(toFind);
        Sequential<Integer> all = findAll(toFind);
        if (n < 0)
            n += all.getSize();
        return n < 0 || n >= all.getSize() ? -1 : all.getAtIndex(n);
    }

    default Sequential<Integer> findAll(T toFind) {
        Sequential.Mutable.VariableSize<Integer> all = Sequential.Mutable.VariableSize.of(new ArrayList<>(count(toFind)));
        int length = getSize();
        for (int i = 0; i < length; i++) {
            if (Objects.equals(getAtIndex(i), toFind)) {
                all.append(i);
            }
        }
        return all;
    }

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return new ObverseIterator<>(this);
    }

    @NotNull
    @Override
    default Iterable<T> inReverse() {
        return () -> new ReverseIterator<>(this);
    }

    @Override
    T getAtIndex(int index) throws IndexOutOfBoundsException;

    interface Mutable<T> extends Sequential<T>, CanSetAtIndex<T> {

        static <T> Sequential<T> of(List<T> list) {

            return new AbstractMutableSequential<>() {
                @Override
                public T getAtIndex(int index) throws IndexOutOfBoundsException {
                    return list.get(index);
                }

                @Override
                public int getSize() {
                    return list.size();
                }

                @Override
                public void setAtIndex(int index, @Nullable T toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    list.set(index, toSet);
                }
            };
        }

        static Sequential<Integer> of(int[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Integer getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Integer toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static Sequential<Float> of(float[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Float getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Float toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static Sequential<Long> of(long[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Long getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Long toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static Sequential<Double> of(double[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Double getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Double toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static Sequential<Byte> of(byte[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Byte getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Byte toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static Sequential<Character> of(char[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Character getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Character toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static Sequential<Boolean> of(boolean[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public Boolean getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable Boolean toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        static <T> Sequential<T> of(T[] array) {
            return new AbstractMutableSequential<>() {

                @Override
                public T getAtIndex(int index) throws IndexOutOfBoundsException {
                    return array[index];
                }

                @Override
                public int getSize() {
                    return array.length;
                }

                @Override
                public void setAtIndex(int index, @Nullable T toSet) throws IndexOutOfBoundsException {
                    validateIndex(index);
                    array[index] = Objects.requireNonNull(toSet);
                }
            };
        }

        default void applyInPlace(Function<T, T> toReplace) {
            if (!isEmpty()) {
                preMutate();
                for (int i = 0; i < getSize(); i++) {
                    setAtIndex(i, toReplace.apply(getAtIndex(i)));
                }
                postMutate();
            }
        }

        default void applyInPlace(Consumer<T> toApply) {
            if (!isEmpty()) {
                preMutate();
                for (T element : this) {
                    toApply.accept(element);
                }
                postMutate();
            }
        }

        @NotNull
        @Override
        default Iterator<T> iterator() {
            return new ObverseIterator.Mutable<>(this);
        }

        @NotNull
        @Override
        default Iterable<T> inReverse() {
            return () -> new ReverseIterator.Mutable<>(this);
        }

        interface VariableSize<T> extends Mutable<T>, CanAppendTo<T>, CanRemoveIndexFrom<T> {

            static <T> Sequential.Mutable.VariableSize<T> of(List<T> list) {
                return new SequentialImpl<>(list);
            }

            static <T> Sequential.Mutable.VariableSize<T> of(T[] array) {
                List<T> list = new ArrayList<>(array.length);
                list.addAll(Arrays.asList(array));
                return of(list);
            }

            default void filterInPlace(Predicate<? super T> toTest) {
                if (!isEmpty()) {
                    boolean mutated = false;
                    for (int i = 0; i < getSize(); i++) {
                        T element = getAtIndex(i);
                        if (!toTest.test(element)) {
                            if (!mutated) {
                                preMutate();
                                mutated = true;
                            }
                            removeIndexFrom(i);
                        }
                    }
                    if (mutated) {
                        postMutate();
                    }
                }
            }
        }
    }

    interface View<T> extends ir.smmh.util.View.Injected<Sequential<T>>, Sequential<T> {

        default void addExpirationHandler(Consumer<Sequential<T>> handler) {
            Sequential<T> core = getCore();
            if (core instanceof Sequential.Mutable) {
                ((Sequential.Mutable<T>) core).getOnPreMutateListeners().addDisposable(() -> handler.accept(clone(false)));
            }
        }

        @Override
        default T getAtIndex(int index) throws IndexOutOfBoundsException {
            return with(getCore(), s -> s.getAtIndex(transformIndex(index)), null);
        }

        int transformIndex(int index);

        interface Referential<T> extends View<T> {

            int[] getIndices();

            @Override
            default int getSize() {
                return getIndices().length;
            }

            @Override
            default int transformIndex(int index) {
                return getIndices()[index];
            }

            interface ReferenceComputer<T> {
                int[] computeReference(Sequential<T> sequential);
            }
        }

        class Reference<T> extends AbstractView<T> implements Referential<T> {
            private int[] indices;

            public Reference(Sequential<T> sequential, ReferenceComputer<T> computer) {
                super(sequential);
                this.indices = computer.computeReference(sequential);
                getOnExpireListeners().add(() -> this.indices = new int[0]);
            }

            @Override
            public int[] getIndices() {
                return indices;
            }
        }

        class AllButOne<T> extends AbstractView<T> {

            private final int except;

            public AllButOne(Sequential<T> sequential, int except) {
                super(sequential);
                this.except = except;
            }

            @Override
            public int getSize() {
                return with(getCore(), Sequential::getSize, 1) - 1;
            }

            @Override
            public int transformIndex(int index) {
                return index >= except ? index + 1 : index;
            }
        }

        class Conditional<T> extends Reference<T> {

            public Conditional(Sequential<T> sequential, Predicate<? super T> condition) {
                super(sequential, s -> {

                    int index = 0;
                    int total = s.getSize();

                    int foundIndex = 0;
                    int foundTotal = s.count(condition);

                    int[] reference = new int[foundTotal];

                    while (foundIndex < foundTotal && index < total) {
                        if (condition.test(s.getAtIndex(index))) {
                            reference[foundIndex++] = index;
                        }
                        index++;
                    }

                    return reference;
                });
            }
        }

        class Reversed<T> extends AbstractView<T> {

            public Reversed(Sequential<T> sequential) {
                super(sequential);
            }

            @Override
            public int getSize() {
                return with(getCore(), Sequential::getSize, 0);
            }

            @Override
            public int transformIndex(int index) {
                return getSize() - index - 1;
            }
        }

        class Ranged<T> extends AbstractView<T> {

            private final int start, end;

            /**
             * A sequential ranged view on another sequential object
             * starting from start and ending at the end
             *
             * @param start Inclusive starting index
             */
            public Ranged(Sequential<T> sequential, int start) {
                this(sequential, start, sequential.getSize());
            }

            /**
             * A sequential ranged view on another sequential object
             *
             * @param start Inclusive starting index
             * @param end   Non-inclusive ending index
             */
            public Ranged(Sequential<T> sequential, int start, int end) {
                super(sequential);
                this.start = start;
                this.end = end;
            }

            @Override
            public int getSize() {
                return end - start;
            }

            @Override
            public int transformIndex(int index) {
                return index + start;
            }
        }
    }

    abstract class AbstractMutableSequential<T> extends AbstractSequential<T> implements Sequential.Mutable<T>, ir.smmh.util.Mutable.Injected {

        private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);

        @Override
        public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
            return injectedMutable;
        }
    }

    abstract class AbstractSequential<T> implements Sequential<T> {
        @Override
        public Sequential<T> specificThis() {
            return this;
        }

        @Override
        public Sequential<T> clone(boolean deepIfPossible) {
            return new SequentialImpl<>(asList());
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (T element : this)
                joiner.add(element.toString());
            return joiner.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Sequential) {
                Sequential<?> other = (Sequential<?>) obj;
                if (getSize() == other.getSize()) {
                    for (int i = 0; i < getSize(); i++) {
                        if (!Objects.equals(getAtIndex(i), other.getAtIndex(i))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }

    class ObverseIterator<S extends Sequential<T>, T> implements Iterator<T> {

        protected final S sequential;
        protected int i;

        public ObverseIterator(S sequential) {
            this.sequential = sequential;
            this.i = 0;
        }

        @Override
        public boolean hasNext() {
            return i < sequential.getSize();
        }

        @Override
        public T next() {
            return sequential.getAtIndex(i++);
        }

        static class Mutable<S extends Sequential.Mutable<T>, T> extends ObverseIterator<S, T> {
            public Mutable(S sequential) {
                super(sequential);
            }

            static class VariableSize<S extends Sequential.Mutable.VariableSize<T>, T> extends Mutable<S, T> {
                public VariableSize(S sequential) {
                    super(sequential);
                }

                @Override
                public void remove() {
                    sequential.removeIndexFrom(i);
                }
            }
        }
    }

    class ReverseIterator<S extends Sequential<T>, T> implements Iterator<T> {

        protected final S sequential;
        protected int index;

        public ReverseIterator(S sequential) {
            this.sequential = sequential;
            this.index = sequential.getSize();
        }

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            return sequential.getAtIndex(--index);
        }

        static class Mutable<S extends Sequential.Mutable<T>, T> extends ReverseIterator<S, T> {
            public Mutable(S sequential) {
                super(sequential);
            }

            static class VariableSize<S extends Sequential.Mutable.VariableSize<T>, T> extends Mutable<S, T> {

                public VariableSize(S sequential) {
                    super(sequential);
                }

                @Override
                public void remove() {
                    sequential.removeIndexFrom(index);
                }
            }
        }
    }

    /**
     * A read-only partial view on another sequential.
     *
     * @param <T> Type of data
     * @see Ranged
     * @see Reversed
     * @see Conditional
     */

    abstract class AbstractView<T> extends AbstractSequential<T> implements View<T> {

        private final ir.smmh.util.View<Sequential<T>> injectedView;

        public AbstractView(Sequential<T> sequential) {
            this.injectedView = new ViewImpl<>(sequential);
        }

        @Override
        public @NotNull ir.smmh.util.View<Sequential<T>> getInjectedView() {
            return injectedView;
        }
    }
}