package ir.smmh.nile.adj;

import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ir.smmh.util.NumberPredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialTest {

    @Test
    void equality() {
        List<String> fruitList;
        String[] fruitArray;
        fruitList = new ArrayList<>();
        fruitList.add("apples");
        fruitList.add("oranges");
        fruitList.add("bananas");
        fruitArray = new String[]{"apples", "oranges", "bananas"};
        assertEquals(Sequential.of(fruitArray), Sequential.of(fruitList));
    }

    @Test
    void permutation() {
        Multitude permutations = Sequential.Mutable.of("123").getPermutations();
        assertEquals(permutations.getSize(), 6);
    }

    @Test
    void countPredicateAndFilterOutOfPlace() {
        Sequential<Integer> s = Sequential.of(RandomUtil.generateRandomIntArray(70, 90));
        assertEquals(s.count(PRIME), s.filterOutOfPlace(PRIME).getSize());
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
        assertEquals(f.getSize(), r.getSize());
        for (int x : r)
            assertEquals(x, m);
    }

    @Test
    void inReverse() {
        Sequential<Integer> s = Sequential.of(RandomUtil.generateRandomIntArray(10, 70));
        Sequential<Integer> p = new SequentialImpl<>(s.inReverse());
        Sequential<Integer> q = (new Sequential.View.Reversed<>(s));
        System.out.println(s);
        System.out.println(p);
        System.out.println(q);
        assertEquals(p, q);
    }
}