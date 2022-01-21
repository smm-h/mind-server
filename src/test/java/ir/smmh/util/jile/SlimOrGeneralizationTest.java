package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.SlimOr;

public class SlimOrGeneralizationTest extends OrGeneralizationTest {

    @Override
    final Or<Integer, Double> make(Integer x) {
        return new SlimOr<>(x, true);
    }

    @Override
    final Or<Integer, Double> make(Double x) {
        return new SlimOr<>(x, false);
    }
}
