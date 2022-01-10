package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.SlimOr;

public class SlimOrGeneralizationTest extends OrGeneralizationTest {

    @Override
    Or<Integer, Double> make(Integer x) {
        return new SlimOr<>(x, true);
    }

    @Override
    Or<Integer, Double> make(Double x) {
        return new SlimOr<>(x, false);
    }
}
