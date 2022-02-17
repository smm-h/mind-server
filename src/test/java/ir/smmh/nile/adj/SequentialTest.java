package ir.smmh.nile.adj;

import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ir.smmh.util.NumberPredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"MagicNumber", "ClassWithoutConstructor"})
class SequentialTest {

    @Test
    final void equality() {
        List<String> fruitList;
        String[] fruitArray;
        fruitList = new ArrayList<>(3);
        fruitList.add("apples");
        fruitList.add("oranges");
        fruitList.add("bananas");
        fruitArray = new String[]{"apples", "oranges", "bananas"};
        assertEquals(Sequential.of(fruitArray), Sequential.of(fruitList), "Sequentials made from a list and an array " +
                "do not equate.");
    }

    @Test
    final void permutation() {
        Multitude permutations = Sequential.Mutable.of("123").getPermutations();
        assertEquals(6, permutations.getSize(), "The number of permutations is not 6 as expected.");
    }

    @Test
    final void countPredicateAndFilterOutOfPlace() {
        Sequential<Integer> seq = Sequential.of(RandomUtil.generateRandomIntArray(70, 90));
        assertEquals(seq.count(PRIME), seq.filterOutOfPlace(PRIME).getSize(), "The number of primes do not equate");
    }

    @Test
    final void applyOutOfPlace() {
        Sequential<Integer> seq = Sequential.of(RandomUtil.generateRandomIntArray(20, 70));
        Sequential<Integer> p = seq.applyOutOfPlace(x -> x + 1);
        assertEquals(seq.count(ODD), p.count(EVEN), "Something went very wrong");
    }

    @Test
    final void findAll() {
        final int m = 5;
        Sequential<Integer> seq = Sequential.of(RandomUtil.generateRandomIntArray(40, m * 2));
        System.out.println(seq);
        Sequential<Integer> all = seq.findAll(m);
        System.out.println(all);
        Sequential<Integer> ref = new Sequential.View.Reference<>(seq, (x) -> all.toIntArray(FunctionalUtil::itself));
        System.out.println(ref);
        assertEquals(all.getSize(), ref.getSize(), "The number of " + m + "'s do not append up.");
        for (int x : ref)
            assertEquals(m, x, () -> "A " + m + " was instead a " + x);
    }

    @Test
    final void inReverse() {
        Sequential<Integer> seq = Sequential.of(RandomUtil.generateRandomIntArray(10, 70));
        Sequential<Integer> p = new SequentialImpl<>(seq.inReverse());
        Sequential<Integer> q = (Sequential.View.reversed(seq));
        System.out.println(seq);
        System.out.println(p);
        System.out.println(q);
        assertEquals(p, q, "Reverse iteration does not equate reversed view.");
    }
}