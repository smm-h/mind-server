package ir.smmh.nile.adj;

import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class SequentialCloneTest<T> {

    final Sequential<T> data;

    protected SequentialCloneTest(Sequential<T> data) {
        this.data = data;
    }

    @Test
    void cloneTest() {
        System.out.println(data);
        Sequential<T> clonedData = data.clone(false);
        System.out.println(clonedData);
        assertEquals(data, clonedData);
    }

    static class Int extends SequentialCloneTest<Integer> {
        protected Int() {
            super(Sequential.of(RandomUtil.generateRandomIntArray(10, 40)));
        }
    }

    static class Char extends SequentialCloneTest<Character> {
        protected Char() {
            super(Sequential.of("Hello, World!".toCharArray()));
        }
    }

    static class Str extends SequentialCloneTest<String> {

        private static final String[] stringArray = {"apples", "oranges", "bananas"};

        protected Str() {
            super(Sequential.of(stringArray));
        }
    }
}
