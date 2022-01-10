package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.FatOr;

public class FatOrGeneralizationTest extends OrGeneralizationTest {

    @Override
    Or<Integer, Double> make(Integer x) {
        return new FatOr<>(x, true);
    }

    @Override
    Or<Integer, Double> make(Double x) {
        return new FatOr<>(x, false);
    }
}
