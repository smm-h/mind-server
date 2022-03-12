package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.FatOr;

public class FatOrGeneralizationTest extends OrGeneralizationTest {

    @Override
    final Or<Integer, Double> make(Integer x) {
        return FatOr.either(x, null);
    }

    @Override
    final Or<Integer, Double> make(Double x) {
        return FatOr.either(null, x);
    }
}
