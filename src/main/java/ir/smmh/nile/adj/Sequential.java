package ir.smmh.nile.adj;

import ir.smmh.nile.verbs.CanAppendTo;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanRemoveElementFrom;
import ir.smmh.nile.verbs.CanRemoveIndexFrom;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface Sequential<T> extends Iterable<T>, ReverseIterable<T>, CanContain<T> {

    static <T> Sequential<T> of(List<T> list) {

        return new Sequential<>() {
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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        return new Sequential<>() {

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
        if (n == 0) return findFirst(toFind);
        if (n == -1) return findLast(toFind);
        List<Integer> all = findAll(toFind);
        if (n < 0) n += all.size();
        return n < 0 || n >= all.size() ? -1 : all.get(n);
    }

    default List<Integer> findAll(T toFind) {
        List<Integer> all = new ArrayList<>(count(toFind));
        int length = getLength();
        for (int i = 0; i < length; i++) {
            if (Objects.equals(getAt(i), toFind)) {
                all.add(i);
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

    interface Mutable<T> extends Sequential<T>, CanAppendTo<T>, CanRemoveElementFrom<T>, CanRemoveIndexFrom<T> {
        void set(int index, T toSet);

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
        protected int i;

        public ReverseIterator(S sequential) {
            this.sequential = sequential;
            this.i = sequential.getLength();
        }

        @Override
        public boolean hasNext() {
            return i < sequential.getLength();
        }

        @Override
        public T next() {
            return sequential.getAt(i++);
        }

        static class Mutable<T> extends ReverseIterator<Sequential.Mutable<T>, T> {
            public Mutable(Sequential.Mutable<T> sequential) {
                super(sequential);
            }

            @Override
            public void remove() {
                sequential.removeIndexFrom(i);
            }
        }
    }
}
