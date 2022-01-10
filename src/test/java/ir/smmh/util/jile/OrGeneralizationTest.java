package ir.smmh.util.jile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class OrGeneralizationTest {

    // TODO test this
    @Test
    void generalize() {
        Number[] n = new Number[4];
        n[0] = Or.generalize(make(4));
        n[1] = Or.generalize(make(4.52));
        n[2] = Or.generalize(make(1.18));
        n[3] = Or.generalize(make(9));
        assertEquals(sum(n), 19.70);
    }

    double sum(Number... numbers) {
        double sum = 0;
        for (Number number : numbers)
            sum += number.doubleValue();
        return sum;
    }

    abstract Or<Integer, Double> make(Integer x);

    abstract Or<Integer, Double> make(Double x);
}
