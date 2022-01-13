package ir.smmh.nile.adj;

import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static ir.smmh.util.NumberPredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialTest {

    List<String> fruitList;
    String[] fruitArray;
    int[] randomIntegers;
    Sequential<Integer> s, p, q;

    @BeforeEach
    void beforeEach() {
        fruitList = new LinkedList<>();
        fruitList.add("apple");
        fruitList.add("orange");
        fruitList.add("banana");
        fruitArray = new String[]{"apple", "orange", "banana"};
        randomIntegers = RandomUtil.generateRandomIntArray(10, 99);
    }

    @Test
    void equality() {
        assertEquals(Sequential.of(fruitArray), Sequential.of(fruitList));
    }

    @Test
    void countPredicate() {
        s = Sequential.of(randomIntegers);
        assertEquals(s.count(PRIME), s.filterOutOfPlace(PRIME).getLength());
    }

    @Test
    void filterOutOfPlace() {
    }

    @Test
    void applyOutOfPlace() {
        s = Sequential.of(randomIntegers);
        p = s.applyOutOfPlace(x -> x + 1);
        assertEquals(s.count(ODD), p.count(EVEN));
    }

    @Test
    void asList() {
    }

    @Test
    void contains() {
    }

    @Test
    void count() {
    }

    @Test
    void findAll() {
    }

    @Test
    void inReverse() {
    }
}