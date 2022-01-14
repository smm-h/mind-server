package ir.smmh.nile.adj;

import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static ir.smmh.util.NumberPredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialTest {

    @Test
    void equality() {
        List<String> fruitList;
        String[] fruitArray;
        fruitList = new LinkedList<>();
        fruitList.add("apple");
        fruitList.add("orange");
        fruitList.add("banana");
        fruitArray = new String[]{"apple", "orange", "banana"};
        assertEquals(Sequential.of(fruitArray), Sequential.of(fruitList));
    }

    @Test
    void countPredicateAndFilterOutOfPlace() {
        Sequential<Integer> s = Sequential.of(RandomUtil.generateRandomIntArray(70, 90));
        assertEquals(s.count(PRIME), s.filterOutOfPlace(PRIME).getLength());
    }

    @Test
    void applyOutOfPlace() {
        Sequential<Integer> s = Sequential.of(RandomUtil.generateRandomIntArray(20, 70));
        Sequential<Integer> p = s.applyOutOfPlace(x -> x + 1);
        assertEquals(s.count(ODD), p.count(EVEN));
    }

    @Test
    void findAll() {
        int m = 5;
        Sequential<Integer> s = Sequential.of(RandomUtil.generateRandomIntArray(40, m * 2));
        System.out.println(s);
        Sequential<Integer> f = s.findAll(m);
        System.out.println(f);
        Sequential<Integer> r = new Sequential.View.Reference<>(s, (x) -> f.toIntArray(FunctionalUtil::itself));
        System.out.println(r);
        assertEquals(f.getLength(), r.getLength());
        for (int x : r)
            assertEquals(x, m);
    }

    @Test
    void inReverse() {
        Sequential<Integer> s = Sequential.of(RandomUtil.generateRandomIntArray(10, 70));
        Sequential<Integer> p = Sequential.of(s.inReverse());
        Sequential<Integer> q = (new Sequential.View.Reversed<>(s));
        System.out.println(s);
        System.out.println(p);
        System.out.println(q);
        assertEquals(p, q);
    }
}