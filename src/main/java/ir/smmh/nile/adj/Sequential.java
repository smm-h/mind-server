package ir.smmh.nile.adj;

import ir.smmh.nile.adj.impl.SequentialList;
import ir.smmh.nile.verbs.CanAppendTo;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanRemoveIndexFrom;
import ir.smmh.util.impl.ViewImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public interface Sequential<T> extends Iterable<T>, ReverseIterable<T>, CanContain<T> {

    static <T> Sequential<T> of(List<T> list) {

        return new AbstractSequential<>() {
            @Override
            public T getAt(int index) throws IndexOutOfBoundsException {
                return list.get(index);
            }

            @Override
            public int getLength() {
                return list.size();
            }
        };
    }

    static Sequential<Integer> of(int[] array) {
        return new AbstractSequential<>() {

            @Override
            public Integer getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static Sequential<Float> of(float[] array) {
        return new AbstractSequential<>() {

            @Override
            public Float getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static Sequential<Long> of(long[] array) {
        return new AbstractSequential<>() {

            @Override
            public Long getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static Sequential<Double> of(double[] array) {
        return new AbstractSequential<>() {

            @Override
            public Double getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static Sequential<Byte> of(byte[] array) {
        return new AbstractSequential<>() {

            @Override
            public Byte getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static Sequential<Character> of(char[] array) {
        return new AbstractSequential<>() {

            @Override
            public Character getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static Sequential<Boolean> of(boolean[] array) {
        return new AbstractSequential<>() {

            @Override
            public Boolean getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static <T> Sequential<T> of(T[] array) {
        return new AbstractSequential<>() {

            @Override
            public T getAt(int index) throws IndexOutOfBoundsException {
                return array[index];
            }

            @Override
            public int getLength() {
                return array.length;
            }
        };
    }

    static <T> Sequential<T> empty() {
        return new AbstractSequential<>() {
            @Override
            public T getAt(int index) throws IndexOutOfBoundsException {
                throw new IndexOutOfBoundsException();
            }

            @Override
            public int getLength() {
                return 0;
            }
        };
    }

    static <T> Sequential<T> of(Iterable<T> iterable) {
        Sequential.Mutable<T> sequential = Sequential.Mutable.of(new LinkedList<>());
        for (T element : iterable) {
            sequential.append(element);
        }
        return sequential;
    }

    default int[] toIntArray(ToInt<T> toInt) {
        int[] array = new int[getLength()];
        for (int i = 0; i < array.length; i++) {
            array[i] = toInt.toInt(getAt(i));
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
        Sequential.Mutable<T> filtered = Sequential.Mutable.of(new ArrayList<>(count(toTest)));
        for (T element : this) {
            if (toTest.test(element)) {
                filtered.append(element);
            }
        }
        return filtered;
    }

    default <R> Sequential<R> applyOutOfPlace(Function<? super T, ? extends R> toReplace) {
        Sequential.Mutable<R> applied = Sequential.Mutable.of(new ArrayList<>(getLength()));
        for (T element : this) {
            applied.append(toReplace.apply(element));
        }
        return applied;
    }

    default @NotNull List<T> asList() {
        return new AbstractList<>() {
            @Override
            public int size() {
                return getLength();
            }

            @Override
            public T get(int index) {
                return getAt(index);
            }
        };
    }

    @Override
    default boolean isEmpty() {
        return getLength() == 0;
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
        int i = getLength();
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
            n += all.getLength();
        return n < 0 || n >= all.getLength() ? -1 : all.getAt(n);
    }

    default Sequential<Integer> findAll(T toFind) {
        Sequential.Mutable<Integer> all = Sequential.Mutable.of(new ArrayList<>(count(toFind)));
        int length = getLength();
        for (int i = 0; i < length; i++) {
            if (Objects.equals(getAt(i), toFind)) {
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

    T getAt(int index) throws IndexOutOfBoundsException;

    int getLength();

    interface ToInt<T> {
        int toInt(T object);
    }


    interface Mutable<T> extends Sequential<T>, CanAppendTo<T>, CanRemoveIndexFrom<T>, ir.smmh.util.Mutable {

        static <T> Sequential.Mutable<T> of(List<T> list) {
            return new SequentialList<>(list);
        }

        static <T> Sequential.Mutable<T> of(T[] array) {
            List<T> list = new ArrayList<>(array.length);
            list.addAll(Arrays.asList(array));
            return of(list);
        }

        default void filterInPlace(Predicate<? super T> toTest) {
            if (!isEmpty()) {
                for (int i = 0; i < getLength(); i++) {
                    T element = getAt(i);
                    if (!toTest.test(element)) {
                        removeIndexFrom(i);
                        postMutate();
                    }
                }
            }
        }

        default void applyInPlace(Function<T, T> toReplace) {
            if (!isEmpty()) {
                for (int i = 0; i < getLength(); i++) {
                    set(i, toReplace.apply(getAt(i)));
                }
                postMutate();
            }
        }

        default void applyInPlace(Consumer<T> toApply) {
            if (!isEmpty()) {
                for (T element : this) {
                    toApply.accept(element);
                }
                postMutate();
            }
        }

        void set(int index, @Nullable T toSet);

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
    }

    interface View<T> extends ir.smmh.util.View.Injected<Sequential<T>>, Sequential<T> {


        @Override
        default T getAt(int index) throws IndexOutOfBoundsException {
            return with(getCore(), s -> s.getAt(transformIndex(index)), null);
        }

        int transformIndex(int index);

        interface Referential<T> extends View<T> {

            int[] getIndices();

            @Override
            default int getLength() {
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
            public int getLength() {
                return with(getCore(), Sequential::getLength, 1) - 1;
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
                    int total = s.getLength();

                    int foundIndex = 0;
                    int foundTotal = s.count(condition);

                    int[] reference = new int[foundTotal];

                    while (foundIndex < foundTotal && index < total) {
                        if (condition.test(s.getAt(index))) {
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
            public int getLength() {
                return with(getCore(), Sequential::getLength, 0);
            }

            @Override
            public int transformIndex(int index) {
                return getLength() - index - 1;
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
                this(sequential, start, sequential.getLength());
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
            public int getLength() {
                return end - start;
            }

            @Override
            public int transformIndex(int index) {
                return index + start;
            }
        }
    }

    abstract class AbstractSequential<T> implements Sequential<T> {
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
                if (getLength() == other.getLength()) {
                    for (int i = 0; i < getLength(); i++) {
                        if (!Objects.equals(getAt(i), other.getAt(i))) {
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
            return i < sequential.getLength();
        }

        @Override
        public T next() {
            return sequential.getAt(i++);
        }

        static class Mutable<T> extends ObverseIterator<Sequential.Mutable<T>, T> {
            public Mutable(Sequential.Mutable<T> sequential) {
                super(sequential);
            }

            @Override
            public void remove() {
                sequential.removeIndexFrom(i);
            }
        }
    }

    class ReverseIterator<S extends Sequential<T>, T> implements Iterator<T> {

        protected final S sequential;
        protected int index;

        public ReverseIterator(S sequential) {
            this.sequential = sequential;
            this.index = sequential.getLength();
        }

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            return sequential.getAt(--index);
        }

        static class Mutable<T> extends ReverseIterator<Sequential.Mutable<T>, T> {
            public Mutable(Sequential.Mutable<T> sequential) {
                super(sequential);
            }

            @Override
            public void remove() {
                sequential.removeIndexFrom(index);
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