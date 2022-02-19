package ir.smmh.nile.adj;

import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"AbstractClassWithoutAbstractMethods", "PackageVisibleInnerClass"})
abstract class SequentialCloneTest<T> {

    private final Sequential<T> data;

    SequentialCloneTest(Sequential<T> data) {
        super();
        this.data = data;
    }

    @Test
    final void cloneTest() {
        System.out.println(data);
        Sequential<T> clonedData = data.clone(false);
        System.out.println(clonedData);
        assertEquals(data, clonedData, "Cloned data not equal to data");
    }

    static class Int extends SequentialCloneTest<Integer> {
        Int() {
            super(Sequential.of(RandomUtil.generateRandomIntArray(10, 40)));
        }
    }

    static class Char extends SequentialCloneTest<Character> {
        Char() {
            super(Sequential.of("Hello, World!".toCharArray()));
        }
    }

    static class Str extends SequentialCloneTest<String> {

        private static final String[] stringArray = {"apples", "oranges", "bananas"};

        Str() {
            super(Sequential.of(stringArray));
        }
    }
}
